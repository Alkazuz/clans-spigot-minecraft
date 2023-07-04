package br.alkazuz.clans.manager;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.objects.ClanRoles;
import br.alkazuz.clans.storage.DBCore;

import java.sql.ResultSet;
import java.util.Map;

public class ClanPlayerManager {
    private static final Map<String, ClanPlayer> players = new java.util.HashMap<>();

    public static ClanPlayer getClanPlayer(String name) {
        return players.get(name.toLowerCase());
    }

    public static void setRemoveClan(Clan clan) {
        for (ClanPlayer player : players.values()) {
            if (player.getClan() == clan) {
                player.setClan(null);
                player.save();
            }
        }
    }
    public static void loadAll() {
        DBCore db = Clans.getInstance().database;
        try {
            ResultSet res = db.select("SELECT clans_players.*, clans.id AS clan_id, clans.name AS clan_name " +
                    "FROM clans_players LEFT JOIN clans ON clans_players.clan_id = clans.id;");
            while (res.next()) {
                Integer clanId = res.getInt("clan_id");
                Clan clan = ClanManager.getClanById(clanId);
                ClanPlayer player = new ClanPlayer(res.getString("player"), clan);
                player.setRole(ClanRoles.valueOf(res.getString("role")));
                player.setKills(res.getInt("kills"));
                player.setDeaths(res.getInt("deaths"));
                player.setLastOnline(res.getTimestamp("last_online"));
                players.put(player.getName().toLowerCase(), player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
