package codes.ztereohype.ztereomusic;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.commands.*;
import codes.ztereohype.ztereomusic.database.Config;
import codes.ztereohype.ztereomusic.listeners.AloneDisconnectListener;
import codes.ztereohype.ztereomusic.listeners.CommandListener;
import codes.ztereohype.ztereomusic.networking.SpotifyApiHelper;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ZtereoMUSIC {
    public static final ZtereoMUSIC INSTANCE = new ZtereoMUSIC();

    private final Map<String, Command> commandMap = new HashMap<>();
    private final Map<String, String> commandAliases = new HashMap<>();

    private Config config;
    private JDA jda;

    private AudioPlayerManager playerManager;
    private Map<Long, TrackManager> guildTrackManagerMap = new HashMap<>();

    private ZtereoMUSIC() {}

    public static ZtereoMUSIC getInstance() {
        return ZtereoMUSIC.INSTANCE;
    }

    @SneakyThrows({ FileNotFoundException.class, LoginException.class,
                    InterruptedException.class, IOException.class })
    public static void main(String[] args) {
        ZtereoMUSIC ztereoMUSIC = ZtereoMUSIC.getInstance();

        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        );

        ztereoMUSIC.setConfig(Config.loadFrom("./config.json5"));
        ztereoMUSIC.setJda(JDABuilder.createDefault(ztereoMUSIC.getConfig().getPropreties().get("token"), intents)
                                    .enableCache(CacheFlag.VOICE_STATE)
                                    .build().awaitReady());

        ztereoMUSIC.setupAudio();
        ztereoMUSIC.setCommands();
        ztereoMUSIC.setListeners();
    }

    private void setCommands() {
        Ping ping = new Ping();
        this.getCommandMap().put(ping.getMeta().getName(), ping);

        Play play = new Play();
        this.getCommandMap().put(play.getMeta().getName(), play);

        Skip skip = new Skip();
        this.getCommandMap().put(skip.getMeta().getName(), skip);

        Pause pause = new Pause();
        this.getCommandMap().put(pause.getMeta().getName(), pause);

        Disconnect disconnect = new Disconnect();
        this.getCommandMap().put(disconnect.getMeta().getName(), disconnect);

        Clear clear = new Clear();
        this.getCommandMap().put(clear.getMeta().getName(), clear);

        Remove remove = new Remove();
        this.getCommandMap().put(remove.getMeta().getName(), remove);

        Queue queue = new Queue();
        this.getCommandMap().put(queue.getMeta().getName(), queue);

        for (String commandName : this.getCommandMap().keySet()) {
            for (String aliasName : this.getCommandMap().get(commandName).getMeta().getAliases()) {
                System.out.println("Loaded alias \"" + aliasName + "\" for command: " + commandName);
                this.getCommandAliases().put(aliasName, commandName);
            }
        }
    }

    private void setupAudio() {
        this.setPlayerManager(new DefaultAudioPlayerManager());
        AudioSourceManagers.registerRemoteSources(this.getPlayerManager());
        AudioSourceManagers.registerLocalSource(this.getPlayerManager());
        SpotifyApiHelper.startTokenTimer();
    }

    private void setListeners() {
        this.getJda().addEventListener(new CommandListener());
        this.getJda().addEventListener(new AloneDisconnectListener());
    }
}
