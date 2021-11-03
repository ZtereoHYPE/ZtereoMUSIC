package codes.ztereohype.ztereomusic.command;

import lombok.Getter;

@Getter
public class CommandMeta {
    private final String name;
    private final String description;
    private final String[] aliases;
    private final boolean isNsfw;
    private final boolean isHidden;

    public CommandMeta(String name, String description, String[] aliases, boolean isNsfw, boolean isHidden) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.isNsfw = isNsfw;
        this.isHidden = isHidden;
    }
}
