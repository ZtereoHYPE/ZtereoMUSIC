package codes.ztereohype.ztereomusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.List;

public class TrackManager extends AudioEventAdapter {
    private final @Getter AudioPlayer player;
    public final List<AudioTrack> trackQueue = new ArrayList<>();
    private final MessageChannel infoChannel;

    public TrackManager(AudioPlayer player, MessageChannel infoChannel) {
        this.player = player;
        this.infoChannel = infoChannel;
    }

    public void queue(AudioTrack track) {
        trackQueue.add(track);
    }

    public void playNext() {
        // if the player was playing a track (probably means it's a skip), stop it
        if (player.getPlayingTrack() != null) {
            player.stopTrack();
        }

        if (trackQueue.isEmpty()) {
            infoChannel.sendMessage("The queue is empty!").queue();
            return;
        }

        AudioTrack nextTrack = trackQueue.get(0);
        trackQueue.remove(nextTrack);
        player.playTrack(nextTrack);
        infoChannel.sendMessage("Playing next track: " + nextTrack.getInfo().title).queue();
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {}

    @Override
    public void onPlayerResume(AudioPlayer player) {}

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {}

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            playNext();
        }

        if (endReason.equals(AudioTrackEndReason.CLEANUP)) {
            // todo: leave the vc?
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        infoChannel.sendMessage("Uh oh, a track did something strange. Skipping...").queue();
        trackQueue.remove(track);
        playNext();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        infoChannel.sendMessage("Unable to play track " + track.getInfo().title + ". Skipping...").queue();
        trackQueue.remove(track);
        playNext();
    }
}
