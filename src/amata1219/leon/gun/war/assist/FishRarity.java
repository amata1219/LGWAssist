package amata1219.leon.gun.war.assist;

public class FishRarity {
    private Rarity rarity;
    private double chance;

    public FishRarity(Rarity rarity, double chance) {
        this.rarity = rarity;
        this.chance = chance / 100.0D;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public double getChance() {
        return this.chance;
    }
}
