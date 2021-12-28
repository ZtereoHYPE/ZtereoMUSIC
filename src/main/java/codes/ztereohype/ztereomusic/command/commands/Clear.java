package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Clear implements Command {
    private @Getter final CommandMeta meta;

    public Clear() {
        this.meta = CommandMeta.builder()
                                .name("clear")
                                .aliases(new String[] {"deleteall"})
                                .description("Clears the queue and stops playing.")
                                .isNsfw(false)
                                .isHidden(false)
                                .checks(new VoiceChecks[] { VoiceChecks.BOT_CONNECTED,
                                                            VoiceChecks.BOT_PLAYING,
                                                            VoiceChecks.USER_CONNECTED,
                                                            VoiceChecks.SAME_VC_IF_CONNECTED })
                                .build();
    }

    @Override
    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Guild guild = messageEvent.getGuild();
        MessageChannel messageChannel = messageEvent.getChannel();
        TrackManager trackManager = TrackManagers.getGuildTrackManager(guild, messageChannel);

        assert trackManager != null; // the command will not execute if it is anyway because of our VoiceChecks (BOT_PLAYING)

        int tracksLeft = trackManager.trackQueue.size();
        for (int i = 0; i < tracksLeft; i++) {
            trackManager.removeQueueItem(0);
        }
        trackManager.stop();
        messageChannel.sendMessage("The queue has been cleared").queue();
    }
}
