package codes.ztereohype.ztereomusic.database;

import lombok.Getter;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final static Json json5 = Json.json5();
    private @Getter
    final Properties propreties = new Properties();
    private String path;

    public static Config loadFrom(String path) throws IOException {
        Config config = new Config();

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        JsonNode tree = json5.parse(new File(path));
        config.path = path;

        for (String key : tree.keySet()) {
            config.getPropreties().setProperty(key, tree.get(key).asString());
        }

        return config;
    }
}