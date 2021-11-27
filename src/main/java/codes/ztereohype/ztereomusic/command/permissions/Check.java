package codes.ztereohype.ztereomusic.command.permissions;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public interface Check {
    boolean getResult(Member messageAuthor, VoiceChannel connectedChannel, TrackManager trackManager);
    String getErrorCode();
}