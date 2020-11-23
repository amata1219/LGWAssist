package amata1219.leon.gun.war.assist;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class LGWMDCommand implements TabExecutor {
    public LGWMDCommand() {
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("" + ChatColor.AQUA + ChatColor.BOLD + "LGWAssist > " + sender.getName() + ": " + "/lgwmdコマンド説明");
            sender.sendMessage(ChatColor.AQUA + "/lgwmd tp [player]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーと同座標にテレポートします。");
            sender.sendMessage(ChatColor.AQUA + "/lgwmd tp x y z");
            sender.sendMessage(ChatColor.WHITE + "- 指定座標にテレポートします。");
            sender.sendMessage(ChatColor.AQUA + "/lgwmd kick [player] [reason]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーをKickします。");
            sender.sendMessage(ChatColor.AQUA + "/lgwmd ban [player] [reason]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーをBANします。");
            sender.sendMessage(ChatColor.AQUA + "/lgwmd unban [player]");
            sender.sendMessage(ChatColor.WHITE + "- 指定プレイヤーのBANを解除します。");
            sender.sendMessage(ChatColor.AQUA + "/lgwmd fly ");
            sender.sendMessage(ChatColor.WHITE + "- トグル形式でFlyモードを切り替えます。");
            sender.sendMessage(ChatColor.AQUA + "/lgwmd sp");
            sender.sendMessage(ChatColor.WHITE + "- トグル形式でSpectatorモードを切り替えます。");
            sender.sendMessage(ChatColor.GRAY + "Developed by amata1219(Twitter@amata1219)");
            return true;
        } else {
            if (args[0].equalsIgnoreCase("tp")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": ゲーム内から実行して下さい。");
                    return true;
                }

                if (args.length == 2) {
                    if (LGWAssist.getPlugin().getServer().getPlayerExact(args[1]) != null) {
                        ((Player)sender).teleport(LGWAssist.getPlugin().getServer().getPlayer(args[1]));
                        sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": " + args[1] + "と同座標にテレポートしました。");
                        return true;
                    }
                } else if (args.length == 4) {
                    int x;
                    int y;
                    int z;
                    try {
                        x = Integer.parseInt(args[1]);
                        y = Integer.parseInt(args[2]);
                        z = Integer.parseInt(args[3]);
                    } catch (NumberFormatException var9) {
                        sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": 座標は半角数字で指定して下さい。");
                        return true;
                    }

                    ((Player)sender).teleport(new Location(((Player)sender).getWorld(), (double)x, (double)y, (double)z));
                    sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + sender.getName() + ": を指定座標(" + x + ", " + y + ", " + z + ")にテレポートしました。");
                    return true;
                }
            } else {
                Player p;
                if (args[0].equalsIgnoreCase("fly")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": ゲーム内から実行して下さい。");
                        return true;
                    }

                    p = (Player)sender;
                    if (p.getAllowFlight()) {
                        p.setAllowFlight(false);
                        sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + p.getName() + ": Flyモードを無効化しました。");
                        return true;
                    }

                    p.setAllowFlight(true);
                    sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + p.getName() + ": Flyモードを有効化しました。");
                    return true;
                }

                if (args[0].equalsIgnoreCase("sp")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": ゲーム内から実行して下さい。");
                        return true;
                    }

                    p = (Player)sender;
                    if (p.getGameMode() == GameMode.SPECTATOR) {
                        p.setGameMode(GameMode.SURVIVAL);
                        sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + p.getName() + ": Spectatorモードを無効化しました。");
                        return true;
                    }

                    p.setGameMode(GameMode.SPECTATOR);
                    sender.sendMessage(ChatColor.GREEN + "LGWAssist > " + p.getName() + ": Spectatorモードを有効化しました。");
                    return true;
                }
            }

            sender.sendMessage(ChatColor.RED + "LGWAssist > " + sender.getName() + ": 入力された値が不正です。");
            return true;
        }
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
