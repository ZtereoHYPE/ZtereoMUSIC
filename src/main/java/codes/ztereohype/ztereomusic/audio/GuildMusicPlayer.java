package codes.ztereohype.ztereomusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageChannel;

public class GuildMusicPlayer {
    private final @Getter AudioPlayer player;
    private final TrackManager trackManager;
    private final MessageChannel infoChannel;

    public GuildMusicPlayer(AudioPlayerManager playerManager, MessageChannel infoChannel) {
        player = playerManager.createPlayer();
        trackManager = new TrackManager(player, infoChannel);
        player.addListener(trackManager);
        this.infoChannel = infoChannel;
    }

    public AudioPlayerSendHandler getAudioSendHandler() {
        return new AudioPlayerSendHandler(this.player);
    }

    public void queue(AudioTrack track) {
        // change this to add to queue and call onTrackEnd!
        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
        } else {
            trackManager.queue(track);
            infoChannel.sendMessage("Queued " + track.getInfo().title).queue();
        }
    }

    public void clearQueue() {
        trackManager.trackQueue.clear();
    }

    public void pause() {
        infoChannel.sendMessage("Pausing...").queue();
        player.setPaused(true);
    }

    public void resume() {
        infoChannel.sendMessage("Resuming...").queue();
        player.setPaused(false);
    }

    public void skip() {
        infoChannel.sendMessage("Skipping...").queue();
        trackManager.playNext();
    }

    public void stop() {
        player.stopTrack();
    }
}
