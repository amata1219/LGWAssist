package amata1219.leon.gun.war.assist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class LGWCommand implements TabExecutor {
    private DataUtility du;

    public LGWCommand(DataUtility du) {
        this.du = du;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("" + ChatColor.AQUA + ChatColor.BOLD + "LGWAssist > " + sender.getName() + ": " + "/lgwコマンド説明");
            sender.sendMessage(ChatColor.AQUA + "/lgw uuid [player]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーのUUIDを表示します。");
            sender.sendMessage(ChatColor.AQUA + "/lgw moderator [player]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーにModerator権限を付与します。");
            sender.sendMessage(ChatColor.AQUA + "/lgw demoderator [player]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーからModerator権限を剥奪します。");
            sender.sendMessage(ChatColor.AQUA + "/lgw enderinv [player] [page]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーのエンダーチェストの指定ページを開きます。");
            sender.sendMessage(ChatColor.AQUA + "/lgw fishing [legendary/epic/rare/common/base] [add/remove]");
            sender.sendMessage(ChatColor.WHITE + "- メインハンドに持ったアイテムを釣果の指定レアリティに追加、または削除します。");
            sender.sendMessage(ChatColor.AQUA + "/lgw fishing list");
            sender.sendMessage(ChatColor.WHITE + "- レアリティ別の釣果一覧を表示します。");
            sender.sendMessage(ChatColor.GRAY + "Developed by amata1219(Twitter@amata1219)");
            return true;
        } else {
            if (args[0].equalsIgnoreCase("uuid")) {
                if (args.length == 2) {
                    if (LGWAssist.getPlugin().getServer().getPlayerExact(args[1]) != null) {
                        sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "(" + LGWAssist.getPlugin().getServer().getPlayer(args[1]).getUniqueId().toString() + ")");
                        return true;
                    }

                    sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": 指定されたプレイヤーはオフラインまたは存在しません。");
                    return true;
                }
            } else {
                PlayerData pd;
                Player p;
                if (args[0].equalsIgnoreCase("moderator")) {
                    if (args.length == 2) {
                        if (LGWAssist.getPlugin().getServer().getPlayerExact(args[1]) != null) {
                            p = LGWAssist.getPlugin().getServer().getPlayer(args[1]);
                            pd = (PlayerData)this.du.getPlayerData().get(p.getUniqueId());
                            if (pd.isModerator()) {
                                sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": " + args[1] + "は既にModerator権限が付与されているため実行出来ません。");
                            } else {
                                pd.setModerator(true);
                                p.sendMessage(ChatColor.GREEN + "LGWAssist > " + args[1] + ": あなたにModerator権限が付与されました。");
                                sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "にModerator権限を付与しました。");
                            }

                            return true;
                        }

                        if (this.isExist(args[1])) {
                            pd = this.du.readPlayerData(LGWAssist.getPlugin().getServer().getOfflinePlayer(args[1]).getUniqueId());
                            if (pd.isModerator()) {
                                sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": " + args[1] + "は既にModerator権限が付与されているため実行出来ません。");
                            } else {
                                pd.setModerator(true);
                                sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "にModerator権限を付与しました。");
                                this.du.writePlayerData(pd);
                            }

                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("demoderator")) {
                    if (args.length == 2) {
                        if (LGWAssist.getPlugin().getServer().getPlayerExact(args[1]) != null) {
                            p = LGWAssist.getPlugin().getServer().getPlayer(args[1]);
                            pd = (PlayerData)this.du.getPlayerData().get(p.getUniqueId());
                            if (!pd.isModerator()) {
                                sender.sendMessage(ChatColor.RED + args[1] + "はModerator権限を付与されていないため実行出来ません。");
                            } else {
                                pd.setModerator(false);
                                p.sendMessage(ChatColor.GREEN + "LGWAssist > " + args[1] + ": あなたのModerator権限は剥奪されました。");
                                sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "のModerator権限を剥奪しました。");
                            }

                            return true;
                        }

                        if (this.isExist(args[1])) {
                            pd = this.du.readPlayerData(LGWAssist.getPlugin().getServer().getOfflinePlayer(args[1]).getUniqueId());
                            if (!pd.isModerator()) {
                                sender.sendMessage(ChatColor.RED + args[1] + "はModerator権限を付与されていないため実行出来ません。");
                            } else {
                                pd.setModerator(false);
                                sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "のModerator権限を剥奪しました。");
                                this.du.writePlayerData(pd);
                            }

                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("enderinv")) {
                    if (args.length == 3) {
                        if (!(sender instanceof Player)) {
                            return true;
                        }

                        p = (Player)sender;
                        boolean var12;
                        int i;
                        if (LGWAssist.getPlugin().getServer().getPlayerExact(args[1]) != null) {
                            var12 = false;

                            try {
                                i = Integer.valueOf(args[2]);
                            } catch (NumberFormatException var9) {
                                sender.sendMessage(ChatColor.RED + "ページ番号は半角数字で指定して下さい。");
                                return true;
                            }

                            p = LGWAssist.getPlugin().getServer().getPlayer(args[1]);
                            UUID uuid = p.getUniqueId();
                            if (i < ((List)this.du.getInvs().get(uuid)).size()) {
                                p.openInventory((Inventory)((List)this.du.getInvs().get(uuid)).get(i));
                                p.setMetadata("IS_OPENED_BY_ADMIN", new FixedMetadataValue(LGWAssist.getPlugin(), true));
                                sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "のインベントリ(ページ: " + i + ")を開きました。");
                                return true;
                            }

                            sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": " + args[1] + "はインベントリ(ページ: " + i + ")を持っていません。");
                            return true;
                        }

                        var12 = false;

                        try {
                            i = Integer.valueOf(args[2]);
                        } catch (NumberFormatException var10) {
                            sender.sendMessage(ChatColor.RED + "ページ番号は半角数字で指定して下さい。");
                            return true;
                        }

                        if (this.isExist(args[1])) {
                            pd = this.du.readPlayerData(LGWAssist.getPlugin().getServer().getOfflinePlayer(args[1]).getUniqueId());
                            if (i < pd.getEnderInvs().size()) {
                                Inventory inventory = this.du.inventoryFromBase64((String)pd.getEnderInvs().get(i), pd.getUUID());
                                p.openInventory(inventory);
                                p.setMetadata("IS_OPENED_BY_ADMIN", new FixedMetadataValue(LGWAssist.getPlugin(), true));
                                sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "のインベントリ(ページ: " + i + ")を開きました。");
                                return true;
                            }

                            sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": " + args[1] + "はインベントリ(ページ: " + i + ")を持っていません。");
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("fishing")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": ゲーム内から実行して下さい。");
                        return true;
                    }

                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "入力された値が不正です。");
                        return true;
                    }

                    p = (Player)sender;
                    ItemStack main = p.getInventory().getItemInMainHand();
                    if (args[1].equalsIgnoreCase("list")) {
                        sender.sendMessage("" + ChatColor.AQUA + ChatColor.BOLD + "LGWAssist > " + sender.getName() + ": " + "レアリティ別、釣果一覧");
                        sender.sendMessage(ChatColor.AQUA + "Legendary(" + ((FishRarity)LGWAssist.getFishUtility().getFishRarity().get(0)).getChance() * 100.0D + "%)");
                        Iterator var18 = this.getItems("Legendary").iterator();

                        while(true) {
                            ItemStack item;
                            while(var18.hasNext()) {
                                item = (ItemStack)var18.next();
                                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                                    sender.sendMessage(ChatColor.WHITE + "- " + item.getItemMeta().getDisplayName() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                } else {
                                    sender.sendMessage(ChatColor.WHITE + "- " + item.getType().toString() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                }
                            }

                            sender.sendMessage(ChatColor.AQUA + "Epic(" + ((FishRarity)LGWAssist.getFishUtility().getFishRarity().get(1)).getChance() * 100.0D + "%)");
                            var18 = this.getItems("Epic").iterator();

                            while(true) {
                                while(var18.hasNext()) {
                                    item = (ItemStack)var18.next();
                                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                                        sender.sendMessage(ChatColor.WHITE + "- " + item.getItemMeta().getDisplayName() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                    } else {
                                        sender.sendMessage(ChatColor.WHITE + "- " + item.getType().toString() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                    }
                                }

                                sender.sendMessage(ChatColor.AQUA + "Rare(" + ((FishRarity)LGWAssist.getFishUtility().getFishRarity().get(2)).getChance() * 100.0D + "%)");
                                var18 = this.getItems("Rare").iterator();

                                while(true) {
                                    while(var18.hasNext()) {
                                        item = (ItemStack)var18.next();
                                        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                                            sender.sendMessage(ChatColor.WHITE + "- " + item.getItemMeta().getDisplayName() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                        } else {
                                            sender.sendMessage(ChatColor.WHITE + "- " + item.getType().toString() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                        }
                                    }

                                    sender.sendMessage(ChatColor.AQUA + "Common(" + ((FishRarity)LGWAssist.getFishUtility().getFishRarity().get(3)).getChance() * 100.0D + "%)");
                                    var18 = this.getItems("Common").iterator();

                                    while(true) {
                                        while(var18.hasNext()) {
                                            item = (ItemStack)var18.next();
                                            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                                                sender.sendMessage(ChatColor.WHITE + "- " + item.getItemMeta().getDisplayName() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                            } else {
                                                sender.sendMessage(ChatColor.WHITE + "- " + item.getType().toString() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                            }
                                        }

                                        sender.sendMessage(ChatColor.AQUA + "Base(" + ((FishRarity)LGWAssist.getFishUtility().getFishRarity().get(4)).getChance() * 100.0D + "%)");
                                        var18 = this.getItems("Base").iterator();

                                        while(true) {
                                            while(var18.hasNext()) {
                                                item = (ItemStack)var18.next();
                                                if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                                                    sender.sendMessage(ChatColor.WHITE + "- " + item.getItemMeta().getDisplayName() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                                } else {
                                                    sender.sendMessage(ChatColor.WHITE + "- " + item.getType().toString() + ChatColor.RESET + "(Amount: " + item.getAmount() + ")");
                                                }
                                            }

                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (main == null || main.getType() == Material.AIR) {
                        sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": メインハンドにアイテムを手に持って下さい。");
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": メインハンドにアイテムを手に持った状態で、/lgw fishing [legendary/epic/rare/common/base] [add/remove] 一覧を表示するには、/lgw fishing list");
                        return true;
                    }

                    if ((args[1].equalsIgnoreCase("legendary") || args[1].equalsIgnoreCase("epic") || args[1].equalsIgnoreCase("rare") || args[1].equalsIgnoreCase("common") || args[1].equalsIgnoreCase("base")) && args.length == 3) {
                        Object items;
                        if (args[2].equalsIgnoreCase("add")) {
                            items = this.getItems(this.toRarity(args[1]));
                            if (items == null) {
                                items = new ArrayList();
                            }

                            ((List)items).add(main);
                            LGWAssist.getFishConfig().getConfig().set(this.toRarity(args[1]) + ".Items", items);
                            LGWAssist.getFishConfig().updateConfig();
                            LGWAssist.getFishUtility().load();
                            p.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": 指定アイテムの追加に成功しました。");
                            return true;
                        }

                        if (args[2].equalsIgnoreCase("remove")) {
                            items = this.getItems(this.toRarity(args[1]));
                            if (items == null) {
                                items = new ArrayList();
                            }

                            if (((List)items).remove(main)) {
                                LGWAssist.getFishConfig().getConfig().set(this.toRarity(args[1]) + ".Items", items);
                                LGWAssist.getFishConfig().updateConfig();
                                LGWAssist.getFishUtility().load();
                                p.sendMessage(ChatColor.GREEN + "LGWAssist > " + p.getName() + ": 指定アイテムの削除に成功しました。");
                                return true;
                            }

                            p.sendMessage(ChatColor.RED + "LGWAssist > " + p.getName() + ": 指定アイテムの削除に失敗しました。個数まで合わせて下さい。");
                        }
                    }
                }
            }

            sender.sendMessage(ChatColor.RED + "入力された値が不正です。");
            return true;
        }
    }

    public String toRarity(String s) {
        if (s.equalsIgnoreCase("legendary")) {
            return "Legendary";
        } else if (s.equalsIgnoreCase("epic")) {
            return "Epic";
        } else if (s.equalsIgnoreCase("rare")) {
            return "Rare";
        } else if (s.equalsIgnoreCase("common")) {
            return "Common";
        } else {
            return s.equalsIgnoreCase("base") ? "Base" : "Base";
        }
    }

    public List<ItemStack> getItems(String s) {
        return (List<ItemStack>) LGWAssist.getFishConfig().getConfig().getList(s + ".Items");
    }

    public boolean isExist(String name) {
        OfflinePlayer[] var5;
        int var4 = (var5 = LGWAssist.getPlugin().getServer().getOfflinePlayers()).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            OfflinePlayer off = var5[var3];
            if (off.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }
}
