package br.alkazuz.clans.command;

import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.ClanPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandChatClan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cApenas jogadores podem executar este comando.");
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage("§cUtilize /cc <mensagem>");
            return true;
        }

        Player player = (Player) commandSender;
        ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());

        if (clanPlayer.getClan() == null) {
            player.sendMessage("§cVocê não tem um clan.");
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append(" ");
        }

        clanPlayer.getClan()
                .broadcast(clanPlayer.getDisplayName() + "§7: §b" + stringBuilder.toString());

        return true;
    }
}
