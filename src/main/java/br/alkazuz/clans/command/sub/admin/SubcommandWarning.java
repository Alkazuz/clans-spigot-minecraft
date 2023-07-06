package br.alkazuz.clans.command.sub.admin;

import br.alkazuz.clans.command.SubCommandBase;
import br.alkazuz.clans.manager.ClanManager;
import br.alkazuz.clans.objects.Clan;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandWarning extends SubCommandBase {
    public SubcommandWarning() {
        super("alerta", "clans.cmd.warning", CommandTo.BOTH, "<add/remover> <tag>", "Adiciona/remove um alerta de um clan", new String[]{"aviso"}, true, false, null);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 3) {
            player.sendMessage("§cUtilize /clan alerta <add/remover> <tag>");
            return true;
        }

        Clan clan = ClanManager.getClan(args[2]);
        String action = args[1];
        if (clan == null) {
            player.sendMessage("§cClan não encontrado.");
            return true;
        }

        if (action.equalsIgnoreCase("add")) {
            clan.setWarnings(clan.getWarnings() + 1);
            player.sendMessage("§aVocê adicionou um alerta ao clan §f" + clan.getTag() + "§a.");
        } else if (action.equalsIgnoreCase("remover")) {
            clan.setWarnings(clan.getWarnings() - 1);
            player.sendMessage("§aVocê removeu um alerta do clan §f" + clan.getTag() + "§a.");
        } else {
            player.sendMessage("§cUtilize /clan alerta <add/remover> <tag>");
        }

        clan.save();
        return true;
    }

}
