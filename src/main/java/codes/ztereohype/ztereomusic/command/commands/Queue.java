package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
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
    CommandMeta meta = new CommandMeta("queue", "See the queue", new String[]{"q"}, false, false, new VoiceChecks[]{ VoiceChecks.BOT_CONNECTED, VoiceChecks.USER_CONNECTED, VoiceChecks.SAME_VC_IF_CONNECTED });

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Guild guild = messageEvent.getGuild();
        VoiceChannel voiceChannel = Objects.requireNonNull(Objects.requireNonNull(messageEvent.getMember()).getVoiceState()).getChannel();
        MessageChannel messageChannel = messageEvent.getChannel();
        VoiceChannel connectedChannel = guild.getAudioManager().getConnectedChannel();

        TrackManager trackManager = TrackManagers.getGuildTrackManager(guild, messageChannel, connectedChannel, voiceChannel);

        StringBuilder messageBuilder = new StringBuilder();
        List<AudioTrack> trackList = trackManager.trackQueue;
        for (AudioTrack track: trackList) {
            messageBuilder.append(trackList.indexOf(track)).append(". ");
            messageBuilder.append(track.getInfo().title);
            messageBuilder.append(System.getProperty("line.separator"));
        }

        if (messageBuilder.length() == 0) {
            messageBuilder.append("There are no items in queue");
        }

        messageChannel.sendMessage(messageBuilder.toString()).queue();
    }

}
