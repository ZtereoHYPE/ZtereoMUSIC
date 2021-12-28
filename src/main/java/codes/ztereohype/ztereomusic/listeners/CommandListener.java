package codes.ztereohype.ztereomusic.listeners;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

public class CommandListener extends ListenerAdapter {
    // TODO: load prefix from a database on a per-server basis
    private static final String PREFIX = ZtereoMUSIC.getInstance().getConfig().getPropreties().get("prefix");
    private static final Map<String, Command> COMMAND_MAP = ZtereoMUSIC.getInstance().getCommandMap();
    private static final Map<String, String> COMMAND_ALIASES = ZtereoMUSIC.getInstance().getCommandAliases();

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel messageChannel = event.getChannel();
        String content = message.getContentRaw();
        Guild guild = message.getGuild();

        // ignore messages from bots, dms, and that don't start with the prefix
        if (!content.startsWith(PREFIX)) return;
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        // parse the message
        String[] parsed = content.substring(PREFIX.length()).split(" +");
        String commandName = parsed[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);

        // check if the command exists
        Command command;
        if (COMMAND_MAP.containsKey(commandName)) {
            command = COMMAND_MAP.get(commandName);
        } else if (COMMAND_ALIASES.containsKey(commandName)) {
            command = COMMAND_MAP.get(COMMAND_ALIASES.get(commandName));
        } else {
            return;
        }

        // check if the command is allowed and stop at first failure (order is important)
        for (VoiceChecks checkEnum : command.getMeta().getChecks()) {
            if (!checkEnum.getCheck().getResult(message.getMember(), guild.getAudioManager().getConnectedChannel(), TrackManagers.getGuildTrackManager(guild, messageChannel))) {
                message.reply(checkEnum.getCheck().getErrorCode()).queue();
                return;
            }
        }

        // try to execute the command
        try {
            command.execute(event, args);
        } catch (Exception e) {
            //todo: nicer embed with error pls
            message.getChannel().sendMessage("uh oh something really bad happened and yeah so yeah everything is aborted and cancelled i give up this is too hard kthxbye").queue();
            throw e;
        }
    }
}
