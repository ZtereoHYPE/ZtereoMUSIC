package codes.ztereohype.ztereomusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.List;

public class TrackScheduer extends AudioEventAdapter {

    private final AudioPlayer player;
    private final List<AudioTrack> trackQueue = new ArrayList<AudioTrack>();
    private AudioTrack currentTrack;
    private final MessageChannel infoChannel;

    public TrackScheduer(AudioPlayer player, MessageChannel infoChannel) {
        this.player = player;
        this.infoChannel = infoChannel;
    }

    public void queue(AudioTrack track) {
        // change this to add to queue and call onTrackEnd!
        if (this.currentTrack == null) {
            System.out.println("Playing song directly as there is no song playing rn");
            this.currentTrack = track;
            player.playTrack(track);
        } else {
            trackQueue.add(track);
            System.out.println("Added song to queue");
        }
    }

    public AudioPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        infoChannel.sendMessage("Pausing...").queue();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        infoChannel.sendMessage("Resuming...").queue();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        infoChannel.sendMessage("Starting track: " + track.getInfo().title).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        currentTrack = null;

        if (endReason.mayStartNext) {
            if (trackQueue.isEmpty()) {
                System.out.println("track finishd, no more tracks in queue!");
                infoChannel.sendMessage("no more tracks in queue!").queue();
            } else {
                System.out.println("track finishd, trying next track");
                AudioTrack nextTrack = trackQueue.get(0);
                trackQueue.remove(0);
                currentTrack = nextTrack;
                infoChannel.sendMessage("Playing next track: " + nextTrack.getInfo().title).queue();
                player.playTrack(currentTrack);
            }
        } else {
            System.out.println("track finishd, cant start next, bye");
            infoChannel.sendMessage("oopsie woopsie byebye").queue();
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
        // An already playing track threw an exception (track end event will still be received separately)
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }
}
