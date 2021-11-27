package codes.ztereohype.ztereomusic.command;

import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import lombok.Getter;

@Getter
public class CommandMeta {
    private final String name;
    private final String description;
    private final String[] aliases;
    private final boolean isNsfw;
    private final boolean isHidden;
    private final VoiceChecks[] checks;

    public CommandMeta(String name, String description, String[] aliases, boolean isNsfw, boolean isHidden, VoiceChecks[] checks) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.isNsfw = isNsfw;
        this.isHidden = isHidden;
        this.checks = checks;
    }
}
