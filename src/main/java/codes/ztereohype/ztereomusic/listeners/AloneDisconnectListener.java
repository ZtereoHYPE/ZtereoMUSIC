package codes.ztereohype.ztereomusic.listeners;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AloneDisconnectListener extends ListenerAdapter {
    //sorry reperak, i tried using a list of pairs but iterating over it to find it in onGuildVoiceLeave is too much effort
    private final Map<Guild, Long> aloneGuilds = new HashMap<>();

    public AloneDisconnectListener() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                checkIfAloneAfterThreshold();
            }
        }, 5000, 5000);
    }

    @Override public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        Guild guild = event.getGuild();
        Member leavingMember = event.getMember();
        Member ztereoBotMember = event.getGuild().getMember(ZtereoMUSIC.getInstance().getJda().getSelfUser());

        if (guild.getAudioManager().getConnectedChannel() == null) return; // if we're not connected ignore

        // If the bot gets disconnected, delete the trackManager for that guild and don't be alone
        if (leavingMember.equals(ztereoBotMember)) {
            TrackManagers.removeGuildTrackManager(guild);
            aloneGuilds.remove(guild);
            return;
        }

        // if there's only one member (and i'm still in), be alone and start a 5m timer
        if (guild.getAudioManager().getConnectedChannel().getMembers().size() == 1) {
            System.out.println("i'm alone");
            aloneGuilds.put(guild, System.currentTimeMillis());
        }
    }

    @Override public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        Guild guild = event.getGuild();

        if (guild.getAudioManager().getConnectedChannel() == null) return; // if we're not connected ignore

        // if there's no longer only one member don't be alone (i wrote this at 4am please tell me it works)
        if (guild.getAudioManager().getConnectedChannel().getMembers().size() > 1) {
            aloneGuilds.remove(guild);
        }
    }

    private void checkIfAloneAfterThreshold() {
        long time = System.currentTimeMillis();
        for (Map.Entry<Guild, Long> entry : aloneGuilds.entrySet()) {
            if ((time - entry.getValue()) > (long) 5 * 60 * 1000) {
                TrackManagers.removeGuildTrackManager(entry.getKey());
                aloneGuilds.remove(entry.getKey());
            }
        }
    }

    //Note: no need to check for moves as the bot currently never moves, only leaves and joins
}
