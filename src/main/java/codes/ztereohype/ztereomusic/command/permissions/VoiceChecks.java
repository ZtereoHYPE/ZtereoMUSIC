package codes.ztereohype.ztereomusic.command.permissions;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nullable;
import java.util.Objects;

public enum VoiceChecks {
    BOT_CONNECTED(new Check() {
        @Override
        public boolean getResult(Member messageAuthor, VoiceChannel connectedChannel, TrackManager trackManager) {
            return connectedChannel != null;
        }

        @Override
        public String getErrorCode() {
            return "I am not playing anything.";
        }
    }),

    BOT_PLAYING(new Check() {
        @Override
        public boolean getResult(Member messageAuthor, VoiceChannel connectedChannel, @Nullable TrackManager trackManager) {
            return trackManager != null && trackManager.getPlayer().getPlayingTrack() != null;
        }

        @Override
        public String getErrorCode() {
            return "I am not playing anything.";
        }
    }),

    USER_CONNECTED(new Check() {
        @Override
        public boolean getResult(Member messageAuthor, VoiceChannel connectedChannel, TrackManager trackManager) {
            if (messageAuthor.getVoiceState() == null) return false;
            return messageAuthor.getVoiceState().inVoiceChannel();
        }

        @Override
        public String getErrorCode() {
            return "You are not connected to a voice channel.";
        }
    }),

    SAME_VC_IF_CONNECTED(new Check() { // the "if connected" specifies to the bot: if the bot is not connected always return true since the condition should be ignored basically. if the user is not in vc and the bot is though...
        @Override
        public boolean getResult(Member messageAuthor, VoiceChannel connectedChannel, TrackManager trackManager) {
            if (connectedChannel == null) return true;
            if (messageAuthor.getVoiceState() == null) return false;
            return Objects.equals(messageAuthor.getVoiceState().getChannel(), connectedChannel);
        }

        @Override
        public String getErrorCode() {
            return "We are not in the same voice channel.";
        }
    }),

    // Note: this is currently unused but will be used for role-based permissions
    HAS_ROLE(new Check() {
        @Override
        public boolean getResult(Member messageAuthor, VoiceChannel connectedChannel, TrackManager trackManager) {
            return false;
        }

        @Override
        public String getErrorCode() {
            return "You don't have the *DJ role*.";
        }
    });

    private @Getter final Check check;

    VoiceChecks(Check check) {
        this.check = check;
    }
}
