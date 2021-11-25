package codes.ztereohype.ztereomusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CustomAudioLoadResultHandler implements AudioLoadResultHandler {
    private final TrackManager trackManager;
    private final MessageReceivedEvent messageEvent;

    public CustomAudioLoadResultHandler(TrackManager trackManager, MessageReceivedEvent messageEvent) {
        this.trackManager = trackManager;
        this.messageEvent = messageEvent;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        this.trackManager.queue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        for (AudioTrack track : playlist.getTracks()) {
            this.trackManager.queue(track);
        }
    }

    @Override
    public void noMatches() {
        this.messageEvent.getMessage().reply("I found no matches for that song!").queue();
    }

    @Override
    public void loadFailed(FriendlyException throwable) {
        this.messageEvent.getMessage().reply("everything blew up and died. i'm sorry.").queue();
    }
}

