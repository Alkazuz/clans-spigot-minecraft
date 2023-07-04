package br.alkazuz.clans.manager;

import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.storage.DBCore;
import br.alkazuz.clans.Clans;
import br.alkazuz.clans.utils.LocationUtils;
import br.alkazuz.clans.utils.WoolColors;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ClanManager {

    private static final Map<String, Clan> clans = new HashMap<>();

    public static Clan getClan(String tag) {
        return clans.get(tag);
    }

    public static void addClan(Clan clan) {
        clans.put(clan.getTag(), clan);
    }

    public static void removeClan(Clan clan) {
        clans.remove(clan.getTag());
    }

    public static boolean clanExists(String tag) {
        return clans.containsKey(tag);
    }

    public static Clan getClanByName(String name) {
        for (Clan clan : clans.values()) {
            if (clan.getName().equalsIgnoreCase(name)) {
                return clan;
            }
        }
        return null;
    }

    public static Clan getClanById(Integer id) {
        if (id == null) return null;
        for (Clan clan : clans.values()) {
            if (clan.id == id) {
                return clan;
            }
        }
        return null;
    }

    public static Map<String, Clan> getClans() {
        return clans;
    }

    public static void clear() {
        clans.clear();
    }

    public static void loadAll() {
        DBCore database = Clans.getInstance().database;
        try {
            ResultSet res = database.select("SELECT * FROM `clans`;");
            while (res.next()) {
                Clan clan = new Clan(res.getString("tag"), res.getString("name"));
                clan.setColor(WoolColors.valueOf(res.getString("color")));
                clan.setOwner(res.getString("owner"));
                clan.setHome(LocationUtils.decodeLocation(res.getString("home")));
                clan.setBalance(res.getDouble("balance"));
                clan.setCreatedAt(res.getTimestamp("created_at"));
                clan.setFriendlyFire(res.getBoolean("friendly_fire"));
                clan.setPoints(res.getInt("points"));
                addClan(clan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createClan(Clan clan) {
        if (clanExists(clan.getTag())) {
            throw new IllegalArgumentException("Já existe um clan com a tag " + clan.getTag() + "!");
        }

        if (getClanByName(clan.getName()) != null) {
            throw new IllegalArgumentException("Já existe um clan com o nome " + clan.getName() + "!");
        }

        try {
            clan.save();
            clans.put(clan.getTag(), clan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteClan(Clan clan) {
        try {
            clan.delete();
            clans.remove(clan.getTag());
            ClanPlayerManager.setRemoveClan(clan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
