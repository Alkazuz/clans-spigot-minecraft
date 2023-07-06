package br.alkazuz.clans.manager;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.objects.ClanRoles;
import br.alkazuz.clans.storage.DBCore;


import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClanPlayerManager {
    private static final Map<String, ClanPlayer> players = new java.util.HashMap<>();

    public static ClanPlayer getClanPlayer(String name) {
        if (!players.containsKey(name.toLowerCase())) {
            ClanPlayer player = new ClanPlayer(name, null);
            players.put(name.toLowerCase(), player);
            return player;
        }
        return players.get(name.toLowerCase());
    }

    public static List<ClanPlayer> getClanPlayersFromClan(Clan clan) {
        return players.values()
                .stream().filter(player -> player.getClan() != null && player.getClan() == clan)
                .collect(Collectors.toList());
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
            ResultSet res = db.select("SELECT clans_players.*, clans.id AS clan_id, clans.name AS clan_name FROM clans_players LEFT JOIN clans ON clans_players.clan_id = clans.id WHERE clan_id IS NOT NULL;");
            while (res.next()) {
                Integer clanId = res.getInt("clan_id");
                Clan clan = ClanManager.getClanById(clanId);
                ClanPlayer player = new ClanPlayer(res.getString("name"), clan);
                player.id = res.getInt("id");
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
