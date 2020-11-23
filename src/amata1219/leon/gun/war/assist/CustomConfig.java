package amata1219.leon.gun.war.assist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomConfig {
    private FileConfiguration config;
    private final File configFile;
    private final String file;
    private final LGWAssist plugin;

    public CustomConfig(LGWAssist plugin) {
        this(plugin, "config.yml");
    }

    public CustomConfig(LGWAssist plugin, String fileName) {
        this.config = null;
        this.plugin = plugin;
        this.file = fileName;
        this.configFile = new File(plugin.getDataFolder(), this.file);
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.file, false);
        }

    }

    public void updateConfig() {
        this.saveConfig();
        this.reloadConfig();
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = this.plugin.getResource(this.file);
        if (defConfigStream != null) {
            this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
        }
    }

    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }

        return this.config;
    }

    public void saveConfig() {
        if (this.config != null) {
            try {
                this.getConfig().save(this.configFile);
            } catch (IOException var2) {
            }
        }
    }
}
