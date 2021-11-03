package codes.ztereohype.ztereomusic.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// TODO: add categories/groups and perms but in a smart way pls
public interface Command {
    CommandMeta getMeta();
    void execute(MessageReceivedEvent messageRecievedEvent, String[] args);
}
