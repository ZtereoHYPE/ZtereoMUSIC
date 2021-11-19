package codes.ztereohype.ztereomusic.database;

import lombok.Getter;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import net.shadew.json.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private @Getter final Map<String, String> propreties = new HashMap<>();
    private String path;

    public static Config loadFrom(String path) throws JsonSyntaxException, FileNotFoundException {
        Config config = new Config();

        Json json5 = Json.json5();
        JsonNode tree = json5.parse(new File(path));

        config.path = path;

        for (String key : tree.keys()) {
            config.getPropreties().put(key, tree.get(key).asString());
        }

        return config;
    }
}