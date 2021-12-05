package codes.ztereohype.ztereomusic.audio;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nullable;
import java.util.Objects;

public class TrackManagers {
    @Nullable
    public static TrackManager getGuildTrackManager(Guild guild, MessageChannel infoChannel) {
        long guildId = guild.getIdLong();

        TrackManager trackManager = ZtereoMUSIC.getInstance().getGuildTrackManagerMap().get(guildId);

        if (trackManager == null) {
            return null;
        }

        trackManager.setInfoChannel(infoChannel);

        guild.getAudioManager().setSendingHandler(trackManager.getAudioSendHandler());

        return trackManager;
    }

    public static TrackManager getOrCreateGuildTrackManager(Guild guild, MessageChannel infoChannel, VoiceChannel requestedChannel) {
        long guildId = guild.getIdLong();

        TrackManager trackManager = ZtereoMUSIC.getInstance().getGuildTrackManagerMap().get(guildId);

        if (trackManager == null) {
            trackManager = new TrackManager(ZtereoMUSIC.getInstance().getPlayerManager(), infoChannel, guild);
            ZtereoMUSIC.getInstance().getGuildTrackManagerMap().put(guildId, trackManager);
            guild.getAudioManager().openAudioConnection(requestedChannel);
        }

        trackManager.setInfoChannel(infoChannel);

        guild.getAudioManager().setSendingHandler(trackManager.getAudioSendHandler());

        return trackManager;
    }

    // stops player, disconnects from vc, and deletes the wrapper
    public static void removeGuildTrackManager(Guild guild) {
        long guildId = guild.getIdLong();
        TrackManager trackManager = ZtereoMUSIC.getInstance().getGuildTrackManagerMap().get(guildId);

        if (trackManager == null) return;

        trackManager.stop();
        guild.getAudioManager().closeAudioConnection();

        //todo: remove this when will be part of disconnection listener y fa fere ts tts sad
        ZtereoMUSIC.getInstance().getGuildTrackManagerMap().remove(guildId);
    }
}
