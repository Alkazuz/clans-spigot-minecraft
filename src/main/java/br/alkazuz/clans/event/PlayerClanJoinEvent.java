package br.alkazuz.clans.event;

import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerClanJoinEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private ClanPlayer clanPlayer;
    private Clan clan;

    public PlayerClanJoinEvent(Player player, ClanPlayer clanPlayer, Clan clan) {
        this.player = player;
        this.clanPlayer = clanPlayer;
        this.clan = clan;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setClanPlayer(ClanPlayer clanPlayer) {
        this.clanPlayer = clanPlayer;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
