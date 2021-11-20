package codes.ztereohype.ztereomusic.networking;

import codes.ztereohype.ztereomusic.ZtereoMUSIC;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import net.shadew.json.JsonPath;
import net.shadew.json.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class YoutubeSearch {
    public static String getVideoUrl(String title) throws IOException {
        String apiKey = ZtereoMUSIC.getInstance().getConfig().getPropreties().get("yt_api_key");
        String query;

        title = (title.contains(" ")) ? title.replace(" ","+") : title;
        query = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%22"+title+"%22&type=video&key="+apiKey;

        // todo: add safety here, sounds a bit unsafe to me tbh but idk
        InputStream inputStream = new URL(query).openStream();
        String jsonResponse = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        //todo: clean up this bullshit try/catch shit and error
        Json json = Json.json();
        JsonNode parsedResponse;
        JsonPath path = JsonPath.parse("items[0].id.videoId");
        String videoId;
        try {
            parsedResponse = json.parse(jsonResponse);
            videoId = parsedResponse.query(path).asString();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            videoId = "error";
        }

        return videoId;
    }
}
