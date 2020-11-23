package amata1219.leon.gun.war.assist;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerData implements Serializable {
    private UUID uuid;
    private String first_login;
    private String last_logout;
    private int number_of_ender_inv;
    private List<String> ender_invs;
    private boolean moderator = false;
    private boolean checked = false;
    private boolean banned = false;
    private String banned_reason;

    public PlayerData(Player p) {
        this.uuid = p.getUniqueId();
        this.number_of_ender_inv = LGWAssist.getConfigUtility().getEnderInventorySize();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getFirstLogin() {
        return this.first_login;
    }

    public void setFirstLogin(String first_login) {
        this.first_login = first_login;
    }

    public String getLastLogout() {
        return this.last_logout;
    }

    public void setLastLogout(String last_logout) {
        this.last_logout = last_logout;
    }

    public int getNumberOfEnderInv() {
        return this.number_of_ender_inv;
    }

    public void setNumberOfEnderInv(int number_of_ender_inv) {
        this.number_of_ender_inv = number_of_ender_inv;
    }

    public List<String> getEnderInvs() {
        return this.ender_invs;
    }

    public void setEnderInvs(List<String> ender_invs) {
        this.ender_invs = ender_invs;
    }

    public boolean isModerator() {
        return this.moderator;
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isBanned() {
        return this.banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getBannedReason() {
        return this.banned_reason;
    }

    public void setBannedReason(String banned_reason) {
        this.banned_reason = banned_reason;
    }
}
