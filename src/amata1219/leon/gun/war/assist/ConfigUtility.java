package amata1219.leon.gun.war.assist;

import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class ConfigUtility {
    private CustomConfig config;
    private CustomConfig fish;
    private int enderInvSize;

    public ConfigUtility(CustomConfig config, CustomConfig fish) {
        this.config = config;
        this.fish = fish;
        this.load();
    }

    public void load() {
        FileConfiguration c = this.config.getConfig();
        this.enderInvSize = c.getInt("EnderInventorySize");
    }

    public int getEnderInventorySize() {
        return this.enderInvSize;
    }

    public FishRarity getFishRarity(Rarity rarity) {
        return new FishRarity(rarity, this.fish.getConfig().getDouble(rarity.toString() + ".Chance"));
    }

    public List<ItemStack> getFishItems(Rarity rarity) {
        return (List<ItemStack>) this.fish.getConfig().getList(rarity.toString() + ".Items");
    }

}
