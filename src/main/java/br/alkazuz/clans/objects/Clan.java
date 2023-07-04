package br.alkazuz.clans.objects;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.storage.DBCore;
import br.alkazuz.clans.utils.LocationUtils;
import br.alkazuz.clans.utils.WoolColors;
import org.bukkit.Location;
import br.alkazuz.clans.utils.Helper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

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
    private int points;

    public Clan(String tag, String name) {
        this.tag = Helper.cleanTag(tag);
        this.color = WoolColors.WHITE;
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.friendlyFire = false;
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

    public boolean isMember(String playername) {
        return this.owner.equalsIgnoreCase(playername);
    }

    public void save() {
        if (this.id == null) {
            this.create();
        } else {
            this.update();
        }
    }

    public void delete() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement("DELETE FROM clans WHERE id=?");
        try {
            st.setInt(1, this.id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete clan " + this.tag);
        }
    }

    private void create() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement("INSERT INTO clans (tag, name, color, owner, home, balance, created_at, friendly_fire, points) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            st.setString(1, this.tag);
            st.setString(2, this.name);
            st.setString(3, this.color.name());
            st.setString(4, this.owner);
            st.setString(5, LocationUtils.encodeLocation(this.home));
            st.setDouble(6, this.balance);
            st.setTimestamp(7, this.createdAt);
            st.setBoolean(8, this.friendlyFire);
            st.setInt(9, this.points);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create clan " + this.tag);
        }
    }

    private void update() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement("UPDATE clans SET tag=?, name=?, color=?, owner=?, home=?, balance=?, created_at=?, friendly_fire=?, points=? WHERE id=?");
        try {
            st.setString(1, this.tag);
            st.setString(2, this.name);
            st.setString(3, this.color.name());
            st.setString(4, this.owner);
            st.setString(5, LocationUtils.encodeLocation(this.home));
            st.setDouble(6, this.balance);
            st.setTimestamp(7, this.createdAt);
            st.setBoolean(8, this.friendlyFire);
            st.setInt(9, this.points);
            st.setInt(10, this.id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update clan " + this.tag);
        }
    }

}
