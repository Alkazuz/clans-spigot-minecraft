package br.alkazuz.clans.objects;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.storage.DBCore;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class ClanPlayer {
    public Integer id;
    private String name;
    private Clan clan;
    private ClanRoles role;
    private int deaths;
    private int kills;
    private Timestamp lastOnline;

    public ClanPlayer(final String name, final Clan clan) {
        this.name = name;
        this.clan = clan;
        this.role = ClanRoles.RECRUIT;
        this.deaths = 0;
        this.kills = 0;
        this.lastOnline = new Timestamp(System.currentTimeMillis());
    }

    public String getName() {
        return this.name;
    }

    public Clan getClan() {
        return this.clan;
    }

    public void setClan(final Clan clan) {
        this.clan = clan;
    }

    public ClanRoles getRole() {
        return this.role;
    }

    public void setRole(final ClanRoles role) {
        this.role = role;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(final int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(final int kills) {
        this.kills = kills;
    }

    public Timestamp getLastOnline() {
        return this.lastOnline;
    }

    public void setLastOnline(final Timestamp lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isLeader() {
        return this.role == ClanRoles.LEADER;
    }

    public boolean isSubLeader() {
        return this.role == ClanRoles.SUBLEADER;
    }

    public boolean isCaptain() {
        return this.role == ClanRoles.CAPTAIN;
    }

    public boolean isMember() {
        return this.role == ClanRoles.MEMBER;
    }

    public boolean isRecruit() {
        return this.role == ClanRoles.RECRUIT;
    }

    public void promote() {
        if (this.isRecruit()) {
            this.setRole(ClanRoles.MEMBER);
        }
        else if (this.isMember()) {
            this.setRole(ClanRoles.CAPTAIN);
        }
        else if (this.isCaptain()) {
            this.setRole(ClanRoles.SUBLEADER);
        } else {
            throw new IllegalArgumentException("Jogador já está no cargo máximo!");
        }
    }

    public void demote() {
        if (this.isSubLeader()) {
            this.setRole(ClanRoles.CAPTAIN);
        }
        else if (this.isCaptain()) {
            this.setRole(ClanRoles.MEMBER);
        }
        else if (this.isMember()) {
            this.setRole(ClanRoles.RECRUIT);
        } else {
            throw new IllegalArgumentException("Jogador já está no cargo mínimo!");
        }
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public void addKill(final int amount) {
        this.kills += amount;
    }

    public void addDeath(final int amount) {
        this.deaths += amount;
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
        PreparedStatement st = db.prepareStatement("DELETE FROM `clans_players` WHERE `id` = ?");
        try {
            st.setInt(1, this.id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement("INSERT INTO `clans_players` (`name`, `clan_id`, `role`, `deaths`, `kills`, `last_online`) VALUES (?, ?, ?, ?, ?, ?)");
        try {
            st.setString(1, this.name);
            st.setInt(2, this.clan == null ? null : clan.id);
            st.setString(3, this.role.toString());
            st.setInt(4, this.deaths);
            st.setInt(5, this.kills);
            st.setTimestamp(6, this.lastOnline);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {
        DBCore db = Clans.getInstance().database;
        PreparedStatement st = db.prepareStatement("UPDATE `clans_players` SET `name` = ?, `clan_id` = ?, `role` = ?, `deaths` = ?, `kills` = ?, `last_online` = ? WHERE `id` = ?");
        try {
            st.setString(1, this.name);
            st.setInt(2, this.clan == null ? null : clan.id);
            st.setString(3, this.role.toString());
            st.setInt(4, this.deaths);
            st.setInt(5, this.kills);
            st.setTimestamp(6, this.lastOnline);
            st.setInt(7, this.id);
            st.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
