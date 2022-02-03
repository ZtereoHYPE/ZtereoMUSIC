package codes.ztereohype.ztereomusic.database;

import lombok.Getter;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final static Json json5 = Json.json5();
    private @Getter final Map<String, String> propreties = new HashMap<>();
    private String path;

    public static Config loadFrom(String path) throws IOException {
        Config config = new Config();

        JsonNode tree = json5.parse(new File(path));
        config.path = path;

        for (String key : tree.keySet()) {
            config.getPropreties().put(key, tree.get(key).asString());
        }

        return config;
    }
}