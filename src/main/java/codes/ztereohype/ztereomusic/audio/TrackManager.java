package codes.ztereohype.ztereomusic.audio;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import codes.ztereohype.ztereomusic.networking.YoutubeSearch;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.shadew.util.data.Pair;

import java.util.ArrayList;
import java.util.List;

public class TrackManager extends AudioEventAdapter {
    private final @Getter
    List<AudioTrack> trackQueue = new ArrayList<>();
    private final @Getter
    AudioPlayer player;
    private String hasRetriedId;
    private @Getter
    @Setter
    MessageChannel infoChannel;

    public TrackManager(AudioPlayerManager playerManager, MessageChannel infoChannel) {
        this.player = playerManager.createPlayer();
        this.infoChannel = infoChannel;

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

    public String removeQueueItem(int index) {
        String title = trackQueue.get(index).getInfo().title;
        trackQueue.remove(index);
        return title;
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

    @Override public void onPlayerPause(AudioPlayer player) {}

    @Override public void onPlayerResume(AudioPlayer player) {}

    @Override public void onTrackStart(AudioPlayer player, AudioTrack track) {}

    @Override public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        switch (endReason) {
            case FINISHED -> playNext();

            case LOAD_FAILED -> {
                System.out.println("tracc failed");
                String identifier;
                String trackTitle = track.getInfo().title;

                Pair<Boolean, String> query = YoutubeSearch.query(trackTitle);
                if (query.first()) {
                    identifier = query.second();
                } else {
                    infoChannel.sendMessage(query.second()).queue();
                    return;
                }

                if (!hasRetriedId.equals(identifier)) {
                    hasRetriedId = identifier;
                    infoChannel.sendMessage("Loading failed, retrying...").queue();
                    ZtereoMUSIC.getInstance().getPlayerManager().loadItem(identifier, new CustomAudioLoadResultHandler(this, infoChannel));
                }
            }
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a clone of this back to your queue
    }

    @Override public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        //        infoChannel.sendMessage("Uh oh, a track did something strange. Ask the owner to check for errors in console. ").queue();
        System.out.println(exception.getCause().getMessage());
        onTrackEnd(player, track, AudioTrackEndReason.LOAD_FAILED);
    }

    @Override public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        infoChannel.sendMessage("Unable to play track " + track.getInfo().title + ". Skipping...").queue();
        trackQueue.remove(track);
        playNext();
    }
}
