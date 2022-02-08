package codes.ztereohype.ztereomusic.command.commands;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import codes.ztereohype.ztereomusic.audio.CustomAudioLoadResultHandler;
import codes.ztereohype.ztereomusic.audio.TrackManager;
import codes.ztereohype.ztereomusic.audio.TrackManagers;
import codes.ztereohype.ztereomusic.command.Command;
import codes.ztereohype.ztereomusic.command.CommandMeta;
import codes.ztereohype.ztereomusic.command.permissions.VoiceChecks;
import codes.ztereohype.ztereomusic.networking.SpotifyApiHelper;
import codes.ztereohype.ztereomusic.networking.YoutubeSearch;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.shadew.util.data.Pair;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Play implements Command {
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https)://([a-z]+\\.[a-z]+)+/\\S+$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SPOTIFY_URL_PATTERN = Pattern.compile("^(?:https://open\\.spotify\\.com/(track|playlist)/)(\\S+(?:\\?si=\\S+))$");

    private final CommandMeta meta;

    public Play() {
        this.meta = CommandMeta.builder()
                .name("play")
                .description("Play music!")
                .aliases(new String[] { "p" })
                .isNsfw(false)
                .isHidden(false)
                .checks(new VoiceChecks[] { VoiceChecks.USER_CONNECTED, VoiceChecks.SAME_VC_IF_CONNECTED })
                .build();
    }

    @Override public CommandMeta getMeta() {
        return this.meta;
    }

    public void execute(MessageReceivedEvent messageEvent, String[] args) {
        Member author = Objects.requireNonNull(messageEvent.getMember());

        Guild guild = messageEvent.getGuild();
        VoiceChannel voiceChannel = Objects.requireNonNull(author.getVoiceState()).getChannel();
        MessageChannel messageChannel = messageEvent.getChannel();

        // if there are no args use as play/pause
        if (args.length == 0) {
            TrackManager trackManager = TrackManagers.getGuildTrackManager(guild, messageChannel);

            if (trackManager == null || !trackManager.getPlayer().isPaused()) {
                messageChannel.sendMessage("What should I play? Type the name of the song after the command or use a YouTube link!").queue();
                return;
            }

            trackManager.resume();
            messageChannel.sendMessage("Resuming...").queue();
            return;
        }

        String mergedArgs = String.join(" ", args);
        Matcher matchedUrls = URL_PATTERN.matcher(mergedArgs);
        Matcher matchedSpotifyUrl = SPOTIFY_URL_PATTERN.matcher(mergedArgs);
        boolean urlFound = matchedUrls.find();
        boolean spotifyUrlFound = matchedSpotifyUrl.find();

        if (spotifyUrlFound) {
            Pair<Boolean, String> songSearchQuery = SpotifyApiHelper.query(mergedArgs);

            if (songSearchQuery.first()) {
                mergedArgs = songSearchQuery.second();
            } else {
                messageChannel.sendMessage(songSearchQuery.second()).queue();
                return;
            }
        }

        String identifier;
        // spotify urls need to be queried through youtube
        if (!urlFound || spotifyUrlFound) {
            Pair<Boolean, String> query = YoutubeSearch.query(mergedArgs);
            if (query.first()) {
                identifier = query.second();
            } else {
                messageEvent.getChannel().sendMessage(query.second()).queue();
                return;
            }
        } else {
            identifier = mergedArgs;
        }

        TrackManager trackManager = TrackManagers.getOrCreateGuildTrackManager(guild, messageChannel, voiceChannel);

        ZtereoMUSIC.getInstance().getPlayerManager().loadItem(identifier, new CustomAudioLoadResultHandler(trackManager, messageChannel));
    }
}
