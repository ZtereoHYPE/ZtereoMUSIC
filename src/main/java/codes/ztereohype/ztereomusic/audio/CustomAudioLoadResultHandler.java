package codes.ztereohype.ztereomusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CustomAudioLoadResultHandler implements AudioLoadResultHandler {
    private final TrackManager trackManager;
    private final MessageChannel messageChannel;

    public CustomAudioLoadResultHandler(TrackManager trackManager, MessageChannel messageChannel) {
        this.trackManager = trackManager;
        this.messageChannel = messageChannel;
        trackManager.setInfoChannel(messageChannel);
    }

    @Override public void trackLoaded(AudioTrack track) {
        this.trackManager.queue(track);
    }

    @Override public void playlistLoaded(AudioPlaylist playlist) {
        for (AudioTrack track : playlist.getTracks()) {
            this.trackManager.getTrackQueue().add(track);
        }
        this.messageChannel.sendMessage("Queued " + playlist.getTracks().size() + " songs from: " + playlist.getName()).queue();
    }

    @Override public void noMatches() {
        this.messageChannel.sendMessage("I found no matches for that song!").queue();
    }

    @Override public void loadFailed(FriendlyException throwable) {
        this.messageChannel.sendMessage("Failed loading that audio. Try with a different source (eg. YouTube URL)").queue();
    }
}

