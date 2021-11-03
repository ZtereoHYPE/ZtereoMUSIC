package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.Bot;
import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class Skip implements Command {
    CommandMeta meta = new CommandMeta("skip", "Skip the current track!", new String[]{"next"}, false, false);

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Member author = Objects.requireNonNull(messageEvent.getMember());

        if (author.getVoiceState() == null) {
            messageEvent.getChannel().sendMessage("I was unable to access your information... strange...").queue();
            return;
        }

        if (!author.getVoiceState().inVoiceChannel()) {
            messageEvent.getMessage().reply("You are not in a voice channel!").queue();
            return;
        }

        Guild guild = messageEvent.getGuild();
        VoiceChannel voiceChannel = author.getVoiceState().getChannel();
        AudioManager manager = guild.getAudioManager();

        boolean isInVC = manager.isConnected();
        boolean isInSameVC = isInVC && Objects.equals(manager.getConnectedChannel(), voiceChannel);

        if (isInSameVC && Bot.trackScheduerMap.containsKey(voiceChannel)) {
            TrackManager trackManager = Bot.trackScheduerMap.get(voiceChannel);

            if (trackManager.getPlayer().getPlayingTrack() == null) {
                messageEvent.getMessage().reply("No track is playing.").queue();
                return;
            }

            trackManager.skip();

        } else {
            messageEvent.getMessage().reply("No track is playing...").queue();
        }

    }

}
