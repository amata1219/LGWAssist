package amata1219.leon.gun.war.assist;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class DataUtility {
    private HashMap<UUID, PlayerData> player_data = new HashMap();
    private HashMap<UUID, List<Inventory>> invs = new HashMap();
    private String plugin_folder;

    public DataUtility() {
        this.plugin_folder = LGWAssist.getPlugin().getDataFolder() + File.separator;
    }

    public HashMap<UUID, PlayerData> getPlayerData() {
        return this.player_data;
    }

    public HashMap<UUID, List<Inventory>> getInvs() {
        return this.invs;
    }

    public void makePlayerDataFolder() {
        this.makeFolder(this.getFile("PlayerData"));
    }

    public void makePlayerDataFile(UUID uuid) {
        this.makeFile(this.getFile("PlayerData" + File.separator + uuid.toString() + ".txt"));
    }

    public boolean existPlayerData(UUID uuid) {
        return this.getFile("PlayerData" + File.separator + uuid.toString() + ".txt").exists();
    }

    public void writePlayerData(PlayerData pd) {
        String s = this.toBase64(pd);

        try {
            FileWriter fw = new FileWriter(this.plugin_folder + "PlayerData" + File.separator + pd.getUUID().toString() + ".txt");
            fw.write(s);
            fw.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public PlayerData readPlayerData(UUID uuid) {
        String s = null;
        String data = null;
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.plugin_folder + "PlayerData" + File.separator + uuid.toString() + ".txt"));

            while((data = br.readLine()) != null) {
                sb.append(data);
            }

            s = sb.toString();
            br.close();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return s != null ? this.fromBase64(s) : null;
    }

    public String getNow() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd(E)/HH:MM");
        return sdf.format(c.getTime());
    }

    public String toBase64(Object o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ObjectOutput oop = new ObjectOutputStream(baos);
            oop.writeObject(o);
            oop.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public PlayerData fromBase64(String s) {
        PlayerData p = null;

        try {
            p = (PlayerData)(new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(s)))).readObject();
        } catch (ClassNotFoundException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return p;
    }

    public String inventoryToBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);

            for(int i = 0; i < items.length; ++i) {
                dataOutput.writeObject(items[i]);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public Inventory inventoryFromBase64(String data, UUID uuid) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            dataInput.readInt();
            Inventory inventory = Bukkit.getServer().createInventory((InventoryHolder)null, 54, uuid.toString());

            for(int i = 0; i < inventory.getSize(); ++i) {
                inventory.setItem(i, (ItemStack)dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return null;
    }

    public Inventory setDefault(Inventory inv, int page) {
        inv.setItem(45, this.getPreviousPageButton());
        inv.setItem(49, this.getPageNumberIcon(page));
        inv.setItem(53, this.getNextPageButton());
        return inv;
    }

    public ItemStack getPreviousPageButton() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "前のページへ");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getNextPageButton() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "次のページへ");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getPageNumberIcon(int i) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "表示中のページ：" + i);
        item.setItemMeta(meta);
        return item;
    }

    public File getFile(String s) {
        return new File(this.plugin_folder + s);
    }

    public void makeFolder(File file) {
        if (!file.exists()) {
            file.mkdir();
        }

    }

    public void makeFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

    }
}
