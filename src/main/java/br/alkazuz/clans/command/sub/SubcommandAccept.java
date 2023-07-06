package br.alkazuz.clans.command.sub;

import br.alkazuz.clans.command.SubCommandBase;
import br.alkazuz.clans.config.Settings;
import br.alkazuz.clans.event.PlayerClanJoinEvent;
import br.alkazuz.clans.manager.ClanManager;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandAccept extends SubCommandBase {

    public SubcommandAccept() {
        super("aceitar", "clans.cmd.accept", CommandTo.NO_CLAN, "<tag>", "Aceita um convite para o clan"
                , new String[]{"accept", "aceitar"}, false, false, null);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            sender.sendMessage("§cUtilize /clan aceitar <tag>");
            return true;
        }

        Clan clan = ClanManager.getClan(args[1]);

        if (clan == null) {
            sender.sendMessage("§cClan não encontrado.");
            return true;
        }

        if (!clan.getInvites().contains(sender.getName())) {
            sender.sendMessage("§cVocê não foi convidado para este clan.");
            return true;
        }

        if (ClanPlayerManager.getClanPlayersFromClan(clan).size() >= Settings.MAX_CLAN_MEMBERS) {
            sender.sendMessage("§cEste clan já está cheio.");
            return true;
        }

        PlayerClanJoinEvent event = new PlayerClanJoinEvent(player, ClanPlayerManager.getClanPlayer(sender.getName()), clan);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return true;
        }

        ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(sender.getName());
        clanPlayer.setClan(clan);
        clan.getInvites().remove(sender.getName());
        clan.broadcast("§aO jogador §f" + sender.getName() + " §aentrou no clan.");
        sender.sendMessage("§aVocê entrou no clan §f" + clan.getTag() + "§a.");

        clanPlayer.save();
        return true;
    }

}
