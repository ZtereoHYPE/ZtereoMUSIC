package codes.ztereohype.ztereomusic;

import codes.ztereohype.ztereomusic.audio.TrackManager;
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
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.HashMap;
import java.util.Map;

public class Bot {
    private static @Getter Config config;
    private static @Getter JDA bot;

    private static @Getter final Map<String, Command> commandMap = new HashMap<>();
    private static @Getter final Map<String, String> commandAliases = new HashMap<>();

    public static AudioPlayerManager playerManager;
    public static Map<VoiceChannel, TrackManager> trackScheduerMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        config = new Config("./config.json5");
        bot = JDABuilder.createDefault(config.getPropreties().get("token")).build().awaitReady();

        setCommands();
        setupAudio();
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
            System.out.println("loading aliases from: " + commandName);
            for (String aliasName : commandMap.get(commandName).getMeta().getAliases()) {
                System.out.println("loaded " + aliasName + " from command " + commandName);
                commandAliases.put(aliasName, commandName);
            }
        }
    }

    public static void setupAudio() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    public static void setListeners() {
        bot.addEventListener(new CommandListener());
    }
}
