package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Pause implements Command {
    CommandMeta meta = new CommandMeta("pause", "Pause the playing music", new String[]{"resume"}, false, false, new VoiceChecks[]{ VoiceChecks.BOT_CONNECTED, VoiceChecks.BOT_PLAYING, VoiceChecks.USER_CONNECTED, VoiceChecks.SAME_VC_IF_CONNECTED });

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    @Override
    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Guild guild = messageEvent.getGuild();
        TrackManager trackManager = TrackManagers.getGuildTrackManager(guild);

        assert trackManager != null; // the command will not execute if it is anyway because of our VoiceChecks
        if (trackManager.getPlayer().isPaused()) {
            trackManager.resume();
        } else {
            trackManager.pause();
        }
    }
}
