package br.alkazuz.clans.objects;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.manager.ClanManager;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.storage.DBCore;
import br.alkazuz.clans.utils.LocationUtils;
import br.alkazuz.clans.utils.WoolColors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import br.alkazuz.clans.utils.Helper;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Clan {
    public Integer id;
    private String name;
    private String tag;
    private WoolColors color;
    private String owner;
    private Location home;
    private double balance;
    private Timestamp createdAt;
    private boolean friendlyFire;
    private int warnings;
    private int points;
    private final List<String> invites = new ArrayList<>();

    public Clan(String tag, String name) {
        this.tag = Helper.cleanTag(tag);
        this.color = WoolColors.WHITE;
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.friendlyFire = false;
        this.warnings = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public String getColoredTag() {
        return String.format("§%s%s", color.getColorCode(), tag);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public WoolColors getColor() {
        return color;
    }

    public void setColor(WoolColors color) {
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public void addWarnings(int warnings) {
        this.warnings += warnings;
    }

    public void removeWarnings(int warnings) {
        this.warnings -= warnings;
    }

    public void removePoints(int points) {
        this.points -= points;
    }

    public boolean hasPoints(int points) {
        return this.points >= points;
    }

    public boolean hasBalance(double balance) {
        return this.balance >= balance;
    }

    public void addBalance(double balance) {
        this.balance += balance;
    }

    public void removeBalance(double balance) {
        this.balance -= balance;
    }

    public boolean isOwner(String playername) {
        return this.owner.equalsIgnoreCase(playername);
    }

    public void save() {
        Clans.debug("Saving clan " + this.tag);
        long start = System.currentTimeMillis();
        if (this.id == null) {
            this.create();
        } else {
            this.update();
        }
        Clans.debug("Saved clan " + this.tag + " in " + (System.currentTimeMillis() - start) + "ms");
    }

    public String displayNameWithTag() {
        return "§" + this.color.getColorCode() + "[" + this.tag + "] " + this.name;
    }

    public double getKDR() {
        List<ClanPlayer> members = ClanPlayerManager.getClanPlayersFromClan(this);
        int kills = 0;
        int deaths = 0;
        for (ClanPlayer member : members) {
            kills += member.getKills();
            deaths += member.getDeaths();
        }
        return deaths == 0 ? kills : (kills == 0 ? 0.0 : (double)kills / (double)deaths);
    }

    public void broadcast(String message) {
        for (ClanPlayer member : getOnlineMembers()) {
            Player player = Bukkit.getPlayer(member.getName());
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public int getKills() {
        List<ClanPlayer> members = ClanPlayerManager.getClanPlayersFromClan(this);
        int kills = 0;
        for (ClanPlayer member : members) {
            kills += member.getKills();
        }
        return kills;
    }

    public int getDeaths() {
        List<ClanPlayer> members = ClanPlayerManager.getClanPlayersFromClan(this);
        int deaths = 0;
        for (ClanPlayer member : members) {
            deaths += member.getDeaths();
        }
        return deaths;
    }

    public List<ClanPlayer> getOnlineMembers() {
        return ClanPlayerManager.getClanPlayersFromClan(this)
                .stream()
                .filter(cp -> Bukkit.getPlayer(cp.getName()) != null)
                .collect(java.util.stream.Collectors.toList());
    }

    public void delete() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement("DELETE FROM clans WHERE id=?");
        try {
            st.setInt(1, this.id);
            st.executeUpdate();
            ClanPlayerManager.getClanPlayersFromClan(this).forEach(cp -> cp.setClan(null));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete clan " + this.tag);
        }
    }

    private void create() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement(
                "INSERT INTO clans (tag, name, color, owner, home, balance, created_at, friendly_fire, points, warnings) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        try {
            st.setString(1, this.tag);
            st.setString(2, this.name);
            st.setString(3, this.color.name());
            st.setString(4, this.owner);
            st.setString(5, this.home == null ? null : LocationUtils.encodeLocation(this.home));
            st.setDouble(6, this.balance);
            st.setTimestamp(7, this.createdAt);
            st.setBoolean(8, this.friendlyFire);
            st.setInt(9, this.points);
            st.setInt(10, this.warnings);
            st.executeUpdate();
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create clan " + this.tag);
        }
    }

    private void update() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement("UPDATE clans SET tag=?, name=?, color=?, owner=?, home=?, balance=?, created_at=?, friendly_fire=?, points=?, warnings=? WHERE id=?");
        try {
            st.setString(1, this.tag);
            st.setString(2, this.name);
            st.setString(3, this.color.name());
            st.setString(4, this.owner);
            st.setString(5, this.home == null ? null : LocationUtils.encodeLocation(this.home));
            st.setDouble(6, this.balance);
            st.setTimestamp(7, this.createdAt);
            st.setBoolean(8, this.friendlyFire);
            st.setInt(9, this.points);
            st.setInt(10, this.warnings);
            st.setInt(11, this.id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update clan " + this.tag);
        }
    }


    public List<String> getInvites() {
        return invites;
    }
}
