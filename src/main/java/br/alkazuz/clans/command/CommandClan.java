package br.alkazuz.clans.command;

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

        if (strings.length == 0) {
            //GuiInventory.openMain(player);
            return true;
        }

        SubCommandBase subCommandBase = SubCommands.getSubCommand(strings[0]);
        if (subCommandBase == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("§cComando não encontrado.\n");
            for (SubCommandBase subCommandBase1 : SubCommands.getSubCommands()) {
                if (player.hasPermission(subCommandBase1.getPermission())) {
                    stringBuilder.append("§f/terreno ").append(subCommandBase1.getName()).append(" §7- ").append(subCommandBase1.getDescription()).append("\n");
                }
            }
            player.sendMessage(stringBuilder.toString());
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
