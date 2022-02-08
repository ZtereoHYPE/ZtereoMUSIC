package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;

public class Queue implements Command {
    private final CommandMeta meta;

    public Queue() {
        this.meta = CommandMeta.builder()
                .name("queue")
                .description("See the queue")
                .aliases(new String[] { "q" })
                .isNsfw(false)
                .isHidden(false)
                .checks(new VoiceChecks[] { VoiceChecks.BOT_CONNECTED,
                                            VoiceChecks.USER_CONNECTED,
                                            VoiceChecks.SAME_VC_IF_CONNECTED })
                .build();
    }

    @Override public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Guild guild = messageEvent.getGuild();
        VoiceChannel voiceChannel = Objects.requireNonNull(Objects.requireNonNull(messageEvent.getMember())
                                                                  .getVoiceState()).getChannel();
        MessageChannel messageChannel = messageEvent.getChannel();

        TrackManager trackManager = TrackManagers.getOrCreateGuildTrackManager(guild, messageChannel, voiceChannel);

        StringBuilder messageBuilder = new StringBuilder();
        List<AudioTrack> trackList = trackManager.trackQueue;
        for (AudioTrack track : trackList) {
            messageBuilder.append(trackList.indexOf(track) + 1).append(". ");
            messageBuilder.append(track.getInfo().title);
            messageBuilder.append(System.getProperty("line.separator"));
        }

        if (messageBuilder.length() == 0) {
            messageBuilder.append("There are no items in queue");
        }

        messageChannel.sendMessage(messageBuilder.toString()).queue();
    }

}
