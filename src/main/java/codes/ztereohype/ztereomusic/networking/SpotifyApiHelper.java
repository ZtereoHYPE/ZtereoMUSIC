package codes.ztereohype.ztereomusic.networking;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.shadew.json.Json;
import net.shadew.json.JsonPath;
import net.shadew.json.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyApiHelper {
    private static final String CLIENT_ID = ZtereoMUSIC.getInstance().getConfig().getPropreties().get("spotify_client_id");
    private static final String CLIENT_SECRET = ZtereoMUSIC.getInstance().getConfig().getPropreties().get("spotify_client_secret");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("(?:(?<=https://open\\.spotify\\.com/track/)|(?<=https://open\\.spotify\\.com/playlist/))(\\S+(?=\\?si=\\S))");

    private static String spotifyToken;

    private static final Json JSON = Json.json();

    public static void startTokenTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spotifyToken = null; //remove old outdated token
                Optional<String> parsedToken = getToken();

                if (parsedToken.isEmpty()) {
                    System.out.println("ERROR: Couldn't get a Spotify token. Spotify features will not work!");
                    return;
                }

                spotifyToken = parsedToken.get();
            }
        }, 0, 3599*1000);
    }

    @SneakyThrows
    private static Optional<String> getToken() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
                URI.create("https://accounts.spotify.com/api/token?grant_type=client_credentials"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(StandardCharsets.UTF_8.toString())))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        JsonPath tokenPath = JsonPath.parse("access_token");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String results = JSON.parse(response.body()).query(tokenPath).asString();
            return Optional.ofNullable(results);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<String> query(String songUrl, MessageChannel messageChannel) {
        if (spotifyToken == null) {
            System.out.println("Null Spotify token detected");
            messageChannel.sendMessage("I don't have a spotify token for now. Try again later.").queue();
            return Optional.empty();
        }

        if (songUrl.contains("/playlist/")) {
            messageChannel.sendMessage("Playlists aren't supported for now, please send the individual song links.").queue();
        }

        Matcher matchedSpotifyIdentifier = IDENTIFIER_PATTERN.matcher(songUrl);
        String spotifyIdentifier;
        if (matchedSpotifyIdentifier.find()) {
            spotifyIdentifier = matchedSpotifyIdentifier.group();
        } else {
            messageChannel.sendMessage("Could not parse Spotify link. Try entering the song title directly.").queue();
            return Optional.empty();
        }

        String query = "https://api.spotify.com/v1/tracks?ids="
                + spotifyIdentifier + "&market=ES"; //españaaaa

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(query))
                .GET()
                .header("Authorization", "Bearer " + spotifyToken)
                .header("Content-Type", "application/json")
                .build();

        JsonPath titlePath = JsonPath.parse("tracks[0].name");
        JsonPath authorPath = JsonPath.parse("tracks[0].artists[0].name");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String title = JSON.parse(response.body()).query(titlePath).asString();
            String author = JSON.parse(response.body()).query(authorPath).asString();
            String songSearchQuery = title + " " + author + "official audio";

            return Optional.of(songSearchQuery);
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            e.printStackTrace();
            messageChannel.sendMessage("Something wrong happened with the spotify request.").queue();
            return Optional.empty();
        }
    }
}
