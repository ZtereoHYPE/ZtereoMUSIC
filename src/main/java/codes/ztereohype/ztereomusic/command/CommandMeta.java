package codes.ztereohype.ztereomusic.command;

import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class CommandMeta {
    private final String name;
    private final String description;
    private final String[] aliases;
    private final boolean isNsfw;
    private final boolean isHidden;
    private final VoiceChecks[] checks;
}
