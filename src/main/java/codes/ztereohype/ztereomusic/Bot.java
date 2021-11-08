package codes.ztereohype.ztereomusic;

import codes.ztereohype.ztereomusic.audio.GuildMusicPlayer;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.commands.Ping;
import codes.ztereohype.ztereomusic.command.commands.Play;
import codes.ztereohype.ztereomusic.command.commands.Skip;
import codes.ztereohype.ztereomusic.database.Config;
import codes.ztereohype.ztereomusic.listeners.CommandListener;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.HashMap;
import java.util.Map;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;

public class Bot {
    private static @Getter Config config;
    private static @Getter JDA bot;

    private static @Getter final Map<String, Command> commandMap = new HashMap<>();
    private static @Getter final Map<String, String> commandAliases = new HashMap<>();

    public static AudioPlayerManager playerManager;
    public static Map<Long, GuildMusicPlayer> guildMusicPlayerMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        config = new Config("./config.json5");
        bot = JDABuilder.createDefault(config.getPropreties().get("token"), GUILD_MESSAGES, GUILD_VOICE_STATES).build().awaitReady();

        setupAudio();
        setCommands();
        setListeners();
    }

    public static void setCommands() {
        Ping ping = new Ping();
        commandMap.put(ping.getMeta().getName(), ping);

        Play play = new Play();
        commandMap.put(play.getMeta().getName(), play);

        Skip skip = new Skip();
        commandMap.put(skip.getMeta().getName(), skip);

        for (String commandName : commandMap.keySet()) {
            for (String aliasName : commandMap.get(commandName).getMeta().getAliases()) {
                commandAliases.put(aliasName, commandName);
            }
        }
    }

    public static void setupAudio() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static void setListeners() {
        bot.addEventListener(new CommandListener());
    }
}
