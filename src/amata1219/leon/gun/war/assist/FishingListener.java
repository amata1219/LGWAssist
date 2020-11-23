package amata1219.leon.gun.war.assist;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

public class FishingListener implements Listener {
    public FishingListener() {
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getCaught() != null) {
            if (e.getState() == State.CAUGHT_FISH && e.getCaught() instanceof Item) {
                Player p = e.getPlayer();
                ItemStack item = LGWAssist.getFishUtility().fishing();
                e.getCaught().remove();
                if (p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItem(p.getLocation(), item);
                } else {
                    p.getInventory().addItem(new ItemStack[]{item});
                }
            }

        }
    }
}
