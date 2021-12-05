package codes.ztereohype.ztereomusic.networking;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import net.shadew.json.Json;
import net.shadew.json.JsonPath;
import net.shadew.json.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class YoutubeSearch {
    private static final String API_KEY = ZtereoMUSIC.getInstance().getConfig().getPropreties().get("yt_api_key");
    private static final Json JSON = Json.json();

    public static Optional<String> query(String title) {
        String query = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%22"
                + title.replace(' ', '+') + "%22&type=video&key=" + API_KEY;

        String jsonResponse;
        try {
            jsonResponse = new String(new URL(query).openStream().readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        JsonPath resultsNumberPath = JsonPath.parse("pageInfo.totalResults");
        JsonPath videoPath = JsonPath.parse("items[0].id.videoId");

        try {
            int results = JSON.parse(jsonResponse).query(resultsNumberPath).asInt();
            if (results == 0) return Optional.empty();

            return Optional.ofNullable(JSON.parse(jsonResponse).query(videoPath).asString());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
