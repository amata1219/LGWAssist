package amata1219.leon.gun.war.assist;

import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnderChestListener implements Listener {
    private DataUtility du;

    public EnderChestListener(DataUtility du) {
        this.du = du;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            e.setCancelled(true);
            Inventory inv = (Inventory)((List)this.du.getInvs().get(e.getPlayer().getUniqueId())).get(0);
            this.du.setDefault(inv, 0);
            e.getPlayer().openInventory(inv);
        }

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player)e.getWhoClicked();
            UUID uuid = p.getUniqueId();
            Inventory inv = e.getClickedInventory();
            if (inv != null && inv.getName().equals(uuid.toString())) {
                ItemStack item = e.getCurrentItem();
                if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
                    String s = item.getItemMeta().getDisplayName();
                    if (s != null) {
                        int i = this.getPageNumber(inv.getItem(49));
                        if (s.equalsIgnoreCase(ChatColor.WHITE + "前のページへ")) {
                            e.setCancelled(true);
                            if (i > 0) {
                                Inventory inventory = (Inventory)((List)this.du.getInvs().get(uuid)).get(i - 1);
                                this.du.setDefault(inventory, i - 1);
                                p.openInventory(inventory);
                            }

                            return;
                        }

                        if (s.equalsIgnoreCase(ChatColor.WHITE + "次のページへ")) {
                            e.setCancelled(true);
                            List<Inventory> invs = (List)this.du.getInvs().get(uuid);
                            if (i < invs.size() - 1) {
                                Inventory inventory = (Inventory)invs.get(i + 1);
                                this.du.setDefault(inventory, i + 1);
                                p.openInventory(inventory);
                            }

                            return;
                        }

                        if (s.matches(".*表示中のページ：.*")) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        if (p.hasMetadata("IS_OPENED_BY_ADMIN")) {
            Inventory inv = e.getInventory();
            if (LGWAssist.getPlugin().getServer().getPlayer(UUID.fromString(inv.getTitle())) == null) {
                PlayerData pd = this.du.readPlayerData(UUID.fromString(inv.getTitle()));
                pd.getEnderInvs().set(this.getPageNumber(inv.getItem(49)), this.du.inventoryToBase64(inv.getContents()));
                this.du.writePlayerData(pd);
            }

            p.removeMetadata("IS_OPENED_BY_ADMIN", LGWAssist.getPlugin());
        }

    }

    private int getPageNumber(ItemStack item) {
        return Integer.valueOf(item.getItemMeta().getDisplayName().split("：")[1]);
    }
}
