package codes.ztereohype.ztereomusic.audio;

import codes.ztereohype.ztereomusic.Bot;
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
            Bot.getInstance().getGuildMusicPlayerMap().remove(guildId);
        }

        GuildMusicPlayer musicPlayer = Bot.getInstance().getGuildMusicPlayerMap().get(guildId);

        if (musicPlayer == null) {
            musicPlayer = new GuildMusicPlayer(Bot.getInstance().getPlayerManager(), infoChannel);
            Bot.getInstance().getGuildMusicPlayerMap().put(guildId, musicPlayer);
            guild.getAudioManager().openAudioConnection(requestedChannel);
        }

        guild.getAudioManager().setSendingHandler(musicPlayer.getAudioSendHandler());

        return musicPlayer;
    }

    // stops player, disconnects from vc, and deletes the wrapper
    public static void removeGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicPlayer musicPlayer = Bot.getInstance().getGuildMusicPlayerMap().get(guildId);

        if (musicPlayer == null) return;

        musicPlayer.stop();
        guild.getAudioManager().closeAudioConnection();

        //todo: remove this when will be part of disconnection listener
        Bot.getInstance().getGuildMusicPlayerMap().remove(guildId);
    }
}
