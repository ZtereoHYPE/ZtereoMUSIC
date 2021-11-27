package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Skip implements Command {
    CommandMeta meta = new CommandMeta("skip", "Skip the current track!", new String[]{"next"}, false, false, new VoiceChecks[]{ VoiceChecks.BOT_CONNECTED, VoiceChecks.BOT_PLAYING, VoiceChecks.USER_CONNECTED, VoiceChecks.SAME_VC_IF_CONNECTED });

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Guild guild = messageEvent.getGuild();
        MessageChannel messageChannel = messageEvent.getChannel();
        TrackManager trackManager = TrackManagers.getGuildTrackManager(guild);

        assert trackManager != null; // the command will not execute if it is anyway because of our VoiceChecks (BOT_PLAYING)
        if (trackManager.getPlayer().getPlayingTrack() == null) {
            messageChannel.sendMessage("I am not even playing anything!").queue();
            return;
        }

        trackManager.skip();
    }
}
