package br.alkazuz.clans.command;

import br.alkazuz.clans.gui.GuiInventory;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.ClanPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cApenas jogadores podem executar este comando.");
            return true;
        }
        Player player = (Player) commandSender;
        ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());
        if (strings.length == 0) {
            if (clanPlayer.getClan() == null) {
                GuiInventory.openNoClan(player);
            } else {
                GuiInventory.openClan(player, clanPlayer);
            }
            return true;
        }

        SubCommandBase subCommandBase = SubCommands.getSubCommand(strings[0]);
        if (subCommandBase == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("§cComando não encontrado.\n");
            for (SubCommandBase subCommandBase1 : SubCommands.getSubCommands()) {
                String errorMessage = subCommandBase1.errorExecute(player);
                if (errorMessage != null) {
                    continue;
                }
                stringBuilder.append(subCommandBase.getHelpMessage() + "\n");
            }
            player.sendMessage(stringBuilder.toString());
            return true;
        }

        String errorMessage = subCommandBase.errorExecute(player);
        if (errorMessage != null) {
            player.sendMessage(errorMessage);
            return true;
        }

        if (!player.hasPermission(subCommandBase.getPermission())) {
            player.sendMessage("§cVocê não tem permissão para executar este comando.");
            return true;
        }

        if (!subCommandBase.execute(player, strings)) {
            player.sendMessage("§cUtilize /clan " + subCommandBase.getUsage());
        }

        return true;
    }
}
