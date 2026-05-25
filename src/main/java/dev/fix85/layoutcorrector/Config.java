package dev.fix85.layoutcorrector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {
    public boolean enabled = true;
    public boolean correctLayout = true;
    public String chatPrefix = ".";
    public Map<String, String> aliases = new LinkedHashMap<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Config INSTANCE;

    public static Config get() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("layoutcorrector.json");
    }

    public static void load() {
        Path path = file();
        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                Config loaded = GSON.fromJson(json, Config.class);
                if (loaded != null) INSTANCE = loaded;
            } else {
                INSTANCE = defaults();
                save();
            }
        } catch (IOException e) {
            INSTANCE = defaults();
        }
        if (INSTANCE.aliases == null) INSTANCE.aliases = new LinkedHashMap<>();
        if (INSTANCE.chatPrefix == null || INSTANCE.chatPrefix.isEmpty()) INSTANCE.chatPrefix = ".";
    }

    public static void save() {
        try {
            Files.createDirectories(file().getParent());
            Files.writeString(file(), GSON.toJson(get()));
        } catch (IOException ignored) {}
    }

    public static void resetToDefaults() {
        INSTANCE = defaults();
        save();
    }

    private static Config defaults() {
        Config c = new Config();
        c.aliases.put("спавн", "spawn");
        c.aliases.put("ах", "ah");
        c.aliases.put("ртп", "rtp");
        c.aliases.put("ес", "ec");
        return c;
    }
}
