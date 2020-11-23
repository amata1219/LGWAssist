package amata1219.leon.gun.war.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LGWAssist extends JavaPlugin {
    private static LGWAssist plugin;
    private static CustomConfig config;
    private static CustomConfig fish;
    private static DataUtility du;
    private static ConfigUtility cu;
    private static FishUtility fu;
    private HashMap<String, TabExecutor> commands;

    public LGWAssist() {
    }

    public void onEnable() {
        plugin = this;
        config = new CustomConfig(plugin);
        config.saveDefaultConfig();
        fish = new CustomConfig(plugin, "fish.yml");
        fish.saveDefaultConfig();
        du = new DataUtility();
        du.makePlayerDataFolder();
        cu = new ConfigUtility(config, fish);
        fu = new FishUtility(cu);
        this.commands = new HashMap();
        this.commands.put("lgw", new LGWCommand(du));
        this.commands.put("lgwmd", new LGWMDCommand());

        UUID uuid;
        PlayerData pd;
        for(Iterator var2 = this.getServer().getOnlinePlayers().iterator(); var2.hasNext(); du.getPlayerData().put(uuid, pd)) {
            Player p = (Player)var2.next();
            uuid = p.getUniqueId();
            ArrayList invs;
            if (du.existPlayerData(uuid)) {
                pd = du.readPlayerData(uuid);
                if (pd.isBanned()) {
                    p.kickPlayer(pd.getBannedReason());
                    return;
                }

                invs = new ArrayList();
                UUID finalUuid = uuid;
                pd.getEnderInvs().forEach((ix) -> {
                    invs.add(du.inventoryFromBase64(ix, finalUuid));
                });
                du.getInvs().put(uuid, invs);
            } else {
                pd = new PlayerData(p);
                pd.setFirstLogin(du.getNow());
                pd.setChecked(true);
                invs = new ArrayList();

                for(int i = 0; i < pd.getNumberOfEnderInv(); ++i) {
                    Inventory inv = this.getServer().createInventory((InventoryHolder)null, 54, uuid.toString());
                    du.setDefault(inv, i);
                    invs.add(inv);
                }

                du.getInvs().put(uuid, invs);
            }
        }

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerDataListener(cu, du), plugin);
        pm.registerEvents(new EnderChestListener(du), plugin);
        pm.registerEvents(new FishingListener(), plugin);
    }

    public void onDisable() {
        Iterator var2 = this.getServer().getOnlinePlayers().iterator();

        while(var2.hasNext()) {
            Player p = (Player)var2.next();
            UUID uuid = p.getUniqueId();
            PlayerData pd = (PlayerData)du.getPlayerData().get(uuid);
            List<String> invs = new ArrayList();
            Iterator var7 = ((List)du.getInvs().get(uuid)).iterator();

            while(var7.hasNext()) {
                Inventory inv = (Inventory)var7.next();
                invs.add(du.inventoryToBase64(inv.getContents()));
            }

            pd.setEnderInvs(invs);
            pd.setLastLogout(du.getNow());
            du.writePlayerData(pd);
            du.getPlayerData().remove(uuid);
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return ((TabExecutor)this.commands.get(command.getName())).onCommand(sender, command, label, args);
    }

    public static LGWAssist getPlugin() {
        return plugin;
    }

    public static CustomConfig getCustomConfig() {
        return config;
    }

    public static CustomConfig getFishConfig() {
        return fish;
    }

    public static DataUtility getDataUtility() {
        return du;
    }

    public static ConfigUtility getConfigUtility() {
        return cu;
    }

    public static FishUtility getFishUtility() {
        return fu;
    }
}
