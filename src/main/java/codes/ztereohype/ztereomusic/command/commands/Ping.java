package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements Command {
    private final CommandMeta meta;

    public Ping() {
        this.meta = CommandMeta.builder()
                               .name("ping")
                               .description("A command to get pinged")
                               .aliases(new String[] { "pong", "pog" })
                               .isNsfw(false)
                               .isHidden(false)
                               .build();
    }

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        messageEvent.getMessage().reply("get ping'd lolmao").queue();
    }

}
