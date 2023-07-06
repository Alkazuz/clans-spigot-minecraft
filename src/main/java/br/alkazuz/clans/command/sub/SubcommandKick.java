package br.alkazuz.clans.command.sub;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.command.SubCommandBase;
import br.alkazuz.clans.config.Settings;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.objects.ClanRoles;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

public class SubcommandKick extends SubCommandBase {

        public SubcommandKick() {
            super("kick", "clans.cmd.kick", CommandTo.CLAN, "<jogador>", "Expulsa um jogador do clan."
                    , new String[]{"expulsar", "kick"}, false, true, ClanRoles.LEADER,
                    ClanRoles.SUBLEADER,
                    ClanRoles.CAPTAIN);
        }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            player.sendMessage("§cUtilize /clan expulsar <jogador>");
            return true;
        }

        Clan clan = ClanPlayerManager.getClanPlayer(player.getName()).getClan();
        ClanPlayer clanPlayerTarget = ClanPlayerManager.getClanPlayer(args[1]);
        ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());

        if (clanPlayerTarget == null) {
            player.sendMessage("§cEste jogador não existe.");
            return true;
        }

        if (clanPlayerTarget.getClan() == null) {
            player.sendMessage("§cEste jogador não está em um clan.");
            return true;
        }

        if (!clanPlayerTarget.getClan().equals(clan)) {
            player.sendMessage("§cEste jogador não está no seu clan.");
            return true;
        }

        if (clanPlayerTarget.getRole().getPriority() <= clanPlayer.getRole().getPriority()) {
            player.sendMessage("§cVocê não pode expulsar este jogador.");
            return true;
        }

        player.sendMessage("§eDigite o comando novamente para confirmar a expulsão. Você tem 10 segundos!");

        Clans.getInstance().getEventWaiter()
                .waitForEvent(PlayerCommandPreprocessEvent.class,
                        EventPriority.HIGH,
                        event -> event.getPlayer().equals(player) && event.getMessage().equalsIgnoreCase("/clan expulsar " + args[1]),
                        event -> {
                            event.setCancelled(true);
                            if (clanPlayerTarget.getClan() == null) {
                                player.sendMessage("§cEste jogador não está em um clan.");
                                return;
                            }

                            if (!clanPlayerTarget.getClan().equals(clan)) {
                                player.sendMessage("§cEste jogador não está no seu clan.");
                                return;
                            }

                            clanPlayerTarget.setClan(null);
                            clanPlayerTarget.save();
                            clan.broadcast("§eO jogador §f" + clanPlayerTarget.getName() + " §efoi expulso do clan.");
                        }, 10 * 20, () -> {
                            player.sendMessage("§cVocê não confirmou a expulsão a tempo.");
                        });

        return true;
    }

}
