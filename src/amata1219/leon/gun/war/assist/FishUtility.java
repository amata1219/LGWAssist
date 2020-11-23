package amata1219.leon.gun.war.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FishUtility {
    private ConfigUtility cu;
    private List<FishRarity> rarities = new ArrayList();
    private HashMap<Rarity, List<ItemStack>> table = new HashMap();
    private final Random random = new Random();

    public FishUtility(ConfigUtility c) {
        this.cu = c;
        this.load();
    }

    public void load() {
        this.rarities.add(this.cu.getFishRarity(Rarity.Legendary));
        this.rarities.add(this.cu.getFishRarity(Rarity.Epic));
        this.rarities.add(this.cu.getFishRarity(Rarity.Rare));
        this.rarities.add(this.cu.getFishRarity(Rarity.Common));
        this.rarities.add(this.cu.getFishRarity(Rarity.Base));
        this.table.put(Rarity.Base, this.cu.getFishItems(Rarity.Base));
        this.table.put(Rarity.Common, this.cu.getFishItems(Rarity.Common));
        this.table.put(Rarity.Rare, this.cu.getFishItems(Rarity.Rare));
        this.table.put(Rarity.Epic, this.cu.getFishItems(Rarity.Epic));
        this.table.put(Rarity.Legendary, this.cu.getFishItems(Rarity.Legendary));
    }

    public ItemStack fishing() {
        return this.getRandomFish(this.getRandomRarity());
    }

    public ItemStack getRandomFish(Rarity rarity) {
        List<ItemStack> items = (List)this.table.get(rarity);
        if (items.isEmpty()) {
            return new ItemStack(Material.RAW_FISH);
        } else {
            return items.size() == 1 ? (ItemStack)items.get(0) : (ItemStack)items.get(this.random.nextInt(items.size() - 1));
        }
    }

    public Rarity getRandomRarity() {
        double randomVar = Math.random();
        Iterator var4 = this.rarities.iterator();

        while(var4.hasNext()) {
            FishRarity rarity = (FishRarity)var4.next();
            if (rarity.getChance() >= randomVar) {
                return rarity.getRarity();
            }
        }

        return Rarity.Base;
    }

    public List<FishRarity> getFishRarity() {
        return this.rarities;
    }
}
