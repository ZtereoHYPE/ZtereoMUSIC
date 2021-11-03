package codes.ztereohype.ztereomusic;

import codes.ztereohype.ztereomusic.audio.TrackScheduer;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.commands.Ping;
import codes.ztereohype.ztereomusic.command.commands.Playtest;
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
    public static Map<VoiceChannel, TrackScheduer> trackScheduerMap = new HashMap<>();

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

        Playtest playtest = new Playtest();
        commandMap.put(playtest.getMeta().getName(), playtest);

        for (String commandName : commandMap.keySet()) {
            for (String aliasName : commandMap.get(commandName).getMeta().getAliases()) {
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
