package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class Pause implements Command {
    private final CommandMeta meta;

    public Pause() {
        this.meta = CommandMeta.builder()
                               .name("pause")
                               .description("Pause the playing music")
                               .aliases(new String[] { "resume" })
                               .isNsfw(false)
                               .isHidden(false)
                               .build();
    }

    @Override
    public CommandMeta getMeta() {
        return this.meta;
    }

    @Override
    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Member author = Objects.requireNonNull(messageEvent.getMember());

        if (author.getVoiceState() == null) {
            messageEvent.getChannel().sendMessage("I was unable to access your information... strange...").queue();
            return;
        }

        // todo: make this part of a perms system (only people in same vc or vc at all have perms on music control)
        /* Note to self: Things to check before executing command
            - if he's in vc
            - if we are in vc
            - if we are in the same vc
         */
        if (!author.getVoiceState().inVoiceChannel()) {
            messageEvent.getMessage().reply("You are not in a voice channel!").queue();
            return;
        }

        Guild guild = messageEvent.getGuild();
        VoiceChannel voiceChannel = author.getVoiceState().getChannel();
        MessageChannel messageChannel = messageEvent.getChannel();
        AudioManager manager = guild.getAudioManager();

        if (manager.getConnectedChannel() == null) {
            messageChannel.sendMessage("I am not even playing anything!").queue();
            return;
        }

        // Check if we are in the same vc
        if (!Objects.equals(author.getVoiceState().getChannel(), manager.getConnectedChannel())) {
            messageChannel.sendMessage("We aren't in the same channel").queue();
            return;
        }

        TrackManager trackManager = TrackManagers.getGuildTrackManager(guild, messageChannel, manager.getConnectedChannel(), voiceChannel);

        // Check if we are playing anything
        if (trackManager.getPlayer().getPlayingTrack() == null) {
            messageChannel.sendMessage("I am not even playing anything!").queue();
            return;
        }

        if (trackManager.getPlayer().isPaused()) {
            trackManager.resume();
        } else {
            trackManager.pause();
        }
    }
}
