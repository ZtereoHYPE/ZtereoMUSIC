package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Remove implements Command {
    private @Getter final CommandMeta meta;

    public Remove() {
        this.meta = CommandMeta.builder()
                .name("remove")
                .aliases(new String[] {"delete"})
                .description("Remove the chosen item.")
                .isNsfw(false)
                .isHidden(false)
                .checks(new VoiceChecks[] { VoiceChecks.BOT_CONNECTED,
                        VoiceChecks.BOT_PLAYING,
                        VoiceChecks.USER_CONNECTED,
                        VoiceChecks.SAME_VC_IF_CONNECTED })
                .build();
    }

    @Override
    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Guild guild = messageEvent.getGuild();
        MessageChannel messageChannel = messageEvent.getChannel();

        // if there's the wrong amount of arguments send the usage
        if (args.length != 1) {
            messageChannel.sendMessage("Usage: `remove [index of song to remove]/first/last`. Use the `queue` command to find the index.").queue();
            return;
        }

        TrackManager trackManager = TrackManagers.getGuildTrackManager(guild, messageChannel);
        assert trackManager != null; // the command will not execute if it is anyway because of our VoiceChecks (BOT_PLAYING)

        if (trackManager.trackQueue.size() == 0) {
            messageChannel.sendMessage("There are no songs in queue.").queue();
        }

        Map<String, Integer> indexAliases = new HashMap<>();
        indexAliases.put("first", 1);
        indexAliases.put("last", trackManager.trackQueue.size());

        int parsedIndex;
        String index = args[0];

        // if there is an integer assume it's the index
        if (isNumeric(index)) {
            parsedIndex = Integer.parseInt(index);
        // if there is a known string get the index from the map
        } else if (indexAliases.containsKey(index.toLowerCase(Locale.ROOT))) {
            parsedIndex = indexAliases.get(index);
        } else {
            messageChannel.sendMessage("Usage: `remove [index of song to remove]/first/last`. Use the `queue` command to find the index.").queue();
            return;
        }

        if (parsedIndex > trackManager.trackQueue.size() || parsedIndex < 1) {
            messageChannel.sendMessage("That index is out of bounds.").queue();
            return;
        }

        String title = trackManager.removeQueueItem(parsedIndex - 1);
        messageChannel.sendMessage("Removed " + title).queue();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
