package amata1219.leon.gun.war.assist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PlayerDataListener implements Listener {
    private ConfigUtility cu;
    private DataUtility du;

    public PlayerDataListener(ConfigUtility cu, DataUtility du) {
        this.cu = cu;
        this.du = du;
    }

    public void addItemToEnderInv(UUID uuid, ItemStack item) {
        List<Inventory> inv = (List)this.du.getInvs().get(uuid);

        for(int i = 0; i < inv.size(); ++i) {
            if (((Inventory)inv.get(i)).firstEmpty() != -1) {
                ((Inventory)inv.get(i)).addItem(new ItemStack[]{item});
                return;
            }
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        PlayerData pd;
        ArrayList invs;
        int size;
        Inventory inv;
        if (this.du.existPlayerData(uuid)) {
            pd = this.du.readPlayerData(uuid);
            invs = new ArrayList();
            pd.getEnderInvs().forEach((i) -> {
                invs.add(this.du.inventoryFromBase64(i, uuid));
            });

            for(size = invs.size(); pd.getNumberOfEnderInv() < this.cu.getEnderInventorySize(); ++size) {
                inv = Bukkit.getServer().createInventory((InventoryHolder)null, 54, uuid.toString());
                inv = this.du.setDefault(inv, size);
                invs.add(inv);
                pd.setNumberOfEnderInv(pd.getNumberOfEnderInv() + 1);
            }

            this.du.getInvs().put(uuid, invs);
        } else {
            pd = new PlayerData(p);
            pd.setFirstLogin(this.du.getNow());
            invs = new ArrayList();

            for(size = 0; size < pd.getNumberOfEnderInv(); ++size) {
                inv = Bukkit.getServer().createInventory((InventoryHolder)null, 54, uuid.toString());
                inv = this.du.setDefault(inv, size);
                invs.add(inv);
            }

            this.du.getInvs().put(uuid, invs);
            List<String> invss = new ArrayList();
            Iterator var8 = ((List)this.du.getInvs().get(uuid)).iterator();

            while(var8.hasNext()) {
                inv = (Inventory)var8.next();
                invss.add(this.du.inventoryToBase64(inv.getContents()));
            }

            pd.setEnderInvs(invss);
        }

        this.du.getPlayerData().put(uuid, pd);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        PlayerData pd = (PlayerData)this.du.getPlayerData().get(uuid);
        List<String> invs = new ArrayList();
        Iterator var7 = ((List)this.du.getInvs().get(uuid)).iterator();

        while(var7.hasNext()) {
            Inventory inv = (Inventory)var7.next();
            invs.add(this.du.inventoryToBase64(inv.getContents()));
        }

        pd.setEnderInvs(invs);
        pd.setLastLogout(this.du.getNow());
        this.du.writePlayerData(pd);
        this.du.getPlayerData().remove(uuid);
    }
}
