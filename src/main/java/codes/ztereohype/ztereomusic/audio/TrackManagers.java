package codes.ztereohype.ztereomusic.audio;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;

public class TrackManagers {
    //note: maybe make infoChannel an optional? not sure how to make this better, ask rep
    @Nullable
    public static TrackManager getGuildTrackManager(Guild guild, @Nullable MessageChannel infoChannel) {
        long guildId = guild.getIdLong();

        TrackManager trackManager = ZtereoMUSIC.getInstance().getGuildTrackManagerMap().get(guildId);

        if (trackManager == null) {
            return null;
        }

        if (infoChannel != null) trackManager.setInfoChannel(infoChannel);

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
        AudioManager audioManager = guild.getAudioManager();
        TrackManager trackManager = ZtereoMUSIC.getInstance().getGuildTrackManagerMap().get(guildId);

        audioManager.closeAudioConnection();

        if (trackManager == null) return;

        trackManager.stop();
        ZtereoMUSIC.getInstance().getGuildTrackManagerMap().remove(guildId);
    }
}
