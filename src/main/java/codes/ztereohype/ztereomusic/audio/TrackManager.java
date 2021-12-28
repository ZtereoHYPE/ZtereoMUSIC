package codes.ztereohype.ztereomusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.List;

public class TrackManager extends AudioEventAdapter {
    private final @Getter AudioPlayer player;
    public final List<AudioTrack> trackQueue = new ArrayList<>();
    private @Getter @Setter MessageChannel infoChannel;
    private final Guild guild;

    public TrackManager(AudioPlayerManager playerManager, MessageChannel infoChannel, Guild guild) {
        this.player = playerManager.createPlayer();
        this.infoChannel = infoChannel;
        this.guild = guild;

        player.addListener(this);
    }

    public AudioPlayerSendHandler getAudioSendHandler() {
        return new AudioPlayerSendHandler(this.player);
    }

    public void queue(AudioTrack track) {
        // change this to add to queue and call onTrackEnd!
        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
            infoChannel.sendMessage("Playing: " + track.getInfo().title).queue();
        } else {
            trackQueue.add(track);
            infoChannel.sendMessage("Queued: " + track.getInfo().title).queue();
        }
    }

    public void removeQueueItem(int index) {
        trackQueue.remove(index);
    }

    public void pause() {
        player.setPaused(true);
    }

    public void resume() {
        player.setPaused(false);
    }

    public void skip() {
        playNext();
    }

    public void stop() {
        player.stopTrack();
    }

    private void playNext() {


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

//        if (endReason.equals(AudioTrackEndReason.CLEANUP)) {
//            TrackManagers.removeGuildTrackManager(guild);
//        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        infoChannel.sendMessage("Uh oh, a track did something strange. Ask the owner to check for errors in console. Skpping...").queue();
        System.out.println(exception.getCause().getMessage());
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        infoChannel.sendMessage("Unable to play track " + track.getInfo().title + ". Skipping...").queue();
        trackQueue.remove(track);
        playNext();
    }
}
