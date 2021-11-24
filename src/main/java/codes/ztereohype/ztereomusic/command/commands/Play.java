package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import codes.ztereohype.ztereomusic.audio.CustomAudioLoadResultHandler;
import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.networking.YoutubeSearch;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Play implements Command {
    CommandMeta meta = new CommandMeta("play", "Play music!", new String[]{"p"}, false, false);

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
        MessageChannel messageChannel = messageEvent.getChannel();
        AudioManager manager = guild.getAudioManager();
        AudioPlayerManager playerManager = ZtereoMUSIC.getInstance().getPlayerManager();

        // check if args merged are/have url, if so try to feed it into lava, else try to youtube api the fuck out of it.
        String mergedArgs = String.join(" ", args);

        Pattern urlPattern = Pattern.compile("^(http|https)://([a-z]+\\.[a-z]+)+/\\S+$", Pattern.CASE_INSENSITIVE);
        Matcher matchedUrls = urlPattern.matcher(mergedArgs);
        boolean urlFound = matchedUrls.find();

        String identifier;
        if (!urlFound) {
            Optional<String> query = YoutubeSearch.query(mergedArgs);
            if (query.isPresent()) {
                identifier = query.get();
            } else {
                messageEvent.getChannel().sendMessage("I found no matches for that song!").queue();
                return;
            }
        } else {
            // set identifier to the parsed url
            identifier = mergedArgs;
        }

        TrackManager trackManager = TrackManagers.getGuildTrackManager(guild, messageChannel, manager.getConnectedChannel(), voiceChannel);

        playerManager.loadItem(identifier, new CustomAudioLoadResultHandler(trackManager, messageEvent));
    }
}
