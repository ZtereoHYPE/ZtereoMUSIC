package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements Command {
    CommandMeta meta = new CommandMeta("ping", "A command to get pinged", new String[]{"pong", "pog"}, false, false);

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        messageEvent.getMessage().reply("get ping'd lolmao").queue();
    }

}
