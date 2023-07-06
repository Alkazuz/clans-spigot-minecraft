package br.alkazuz.clans.command.sub;

import br.alkazuz.clans.command.SubCommandBase;
import br.alkazuz.clans.config.Settings;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.objects.ClanRoles;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandInvite extends SubCommandBase {

        public SubcommandInvite() {
            super("convidar", "clans.cmd.invite", CommandTo.CLAN, "<jogador>", "Convida um jogador para o clan"
                    , new String[]{"invite", "convidar"}, false, false, ClanRoles.LEADER,
                    ClanRoles.SUBLEADER,
                    ClanRoles.CAPTAIN);
        }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            player.sendMessage("§cUtilize /clan convidar <jogador>");
            return true;
        }

        String targetName = args[1];
        Player target = player.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cJogador não encontrado.");
            return true;
        }

        if (target == player) {
            player.sendMessage("§cVocê não pode convidar você mesmo.");
            return true;
        }

        if (target.hasPermission("clans.cmd.invite.bypass") && !player.hasPermission("clans.cmd.invite.bypass")) {
            player.sendMessage("§cVocê não pode convidar este jogador.");
            return true;
        }

        ClanPlayer clanPlayerTarget = ClanPlayerManager.getClanPlayer(target.getName());
        if (clanPlayerTarget.getClan() != null) {
            player.sendMessage("§cEste jogador já está em um clan.");
            return true;
        }

        ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());
        if (ClanPlayerManager.getClanPlayersFromClan(clanPlayer.getClan()).size() >= Settings.MAX_CLAN_MEMBERS) {
            player.sendMessage("§cSeu clan já está cheio.");
            return true;
        }

        if (clanPlayer.getClan().getInvites().contains(target.getName())) {
            player.sendMessage("§cEste jogador já foi convidado.");
            return true;
        }

        clanPlayer.getClan().getInvites().add(target.getName());
        player.sendMessage("§aVocê convidou o jogador §f" + target.getName() + " §apara o clan.");
        target.sendMessage("§aVocê foi convidado para o clan §f" + clanPlayer.getClan().getName() + "§a.");
        target.sendMessage("§aUtilize §f/clan aceitar " + clanPlayer.getClan().getTag() + " §apara aceitar o convite.");
        clanPlayer.getClan().broadcast("§f" + player.getName() + " §aconvidou §f" + target.getName() + " §apara o clan.");

        return true;
    }
}
