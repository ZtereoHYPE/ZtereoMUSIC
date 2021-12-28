package codes.ztereohype.ztereomusic.listeners;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class VcLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        Guild guild = event.getGuild();
        Member leavingMember = event.getMember();
        Member ztereoBotMember = event.getGuild().getMember(ZtereoMUSIC.getInstance().getJda().getSelfUser());

        if (leavingMember.equals(ztereoBotMember)) {
            TrackManagers.removeGuildTrackManager(guild);
        }
    }
}
