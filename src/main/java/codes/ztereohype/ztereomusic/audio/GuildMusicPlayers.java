package codes.ztereohype.ztereomusic.audio;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Objects;

public class GuildMusicPlayers {
    public static GuildMusicPlayer getGuildAudioPlayer(Guild guild, MessageChannel infoChannel, VoiceChannel connectedChannel, VoiceChannel requestedChannel) {
        long guildId = guild.getIdLong();
        boolean isInSameVC = Objects.equals(connectedChannel, requestedChannel);

        // If I get called in a different vc I delete the old manager
        if (!isInSameVC) {
            ZtereoMUSIC.getInstance().getGuildMusicPlayerMap().remove(guildId);
        }

        GuildMusicPlayer musicPlayer = ZtereoMUSIC.getInstance().getGuildMusicPlayerMap().get(guildId);

        if (musicPlayer == null) {
            musicPlayer = new GuildMusicPlayer(ZtereoMUSIC.getInstance().getPlayerManager(), infoChannel);
            ZtereoMUSIC.getInstance().getGuildMusicPlayerMap().put(guildId, musicPlayer);
            guild.getAudioManager().openAudioConnection(requestedChannel);
        }

        guild.getAudioManager().setSendingHandler(musicPlayer.getAudioSendHandler());

        return musicPlayer;
    }

    // stops player, disconnects from vc, and deletes the wrapper
    public static void removeGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicPlayer musicPlayer = ZtereoMUSIC.getInstance().getGuildMusicPlayerMap().get(guildId);

        if (musicPlayer == null) return;

        musicPlayer.stop();
        guild.getAudioManager().closeAudioConnection();

        //todo: remove this when will be part of disconnection listener
        ZtereoMUSIC.getInstance().getGuildMusicPlayerMap().remove(guildId);
    }
}
