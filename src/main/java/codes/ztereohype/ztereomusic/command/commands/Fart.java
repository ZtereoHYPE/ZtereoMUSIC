package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import codes.ztereohype.ztereomusic.audio.CustomAudioLoadResultHandler;
import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class Fart implements Command {
    private final CommandMeta meta;

    public Fart() {
        this.meta = CommandMeta.builder()
                .name("fart")
                .description("A command to get fart with extra reverb")
                .aliases(new String[] { "shit" })
                .isNsfw(false)
                .isHidden(false)
                .checks(new VoiceChecks[] { VoiceChecks.USER_CONNECTED, VoiceChecks.SAME_VC_IF_CONNECTED })
                .build();
    }

    @Override public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        VoiceChannel voiceChannel = Objects.requireNonNull(messageEvent.getMember().getVoiceState()).getChannel();

        TrackManager trackManager = TrackManagers.getOrCreateGuildTrackManager(messageEvent.getGuild(), messageEvent.getChannel(), voiceChannel);

        ZtereoMUSIC.getInstance()
                   .getPlayerManager()
                   .loadItem("https://www.youtube.com/watch?v=ITEm5v4xw3o", new CustomAudioLoadResultHandler(trackManager, messageEvent.getChannel()));
    }
}