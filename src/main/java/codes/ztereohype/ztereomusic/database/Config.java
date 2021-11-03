package codes.ztereohype.ztereomusic.database;

import lombok.Getter;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private @Getter final Map<String, String> propreties = new HashMap<>();
    private final String path;

    public Config(String pathname) throws Exception {
        Json json5 = Json.json5();
        JsonNode tree = json5.parse(new File(pathname));
        this.path = pathname;

        for (String key : tree.keys()) {
            propreties.put(key, tree.get(key).asString());
        }
    }
}