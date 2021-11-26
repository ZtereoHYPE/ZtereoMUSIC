package codes.ztereohype.ztereomusic.command;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class CommandMeta {
    private final String name;
    private final String description;
    private final String[] aliases;
    private final boolean isNsfw;
    private final boolean isHidden;
}
