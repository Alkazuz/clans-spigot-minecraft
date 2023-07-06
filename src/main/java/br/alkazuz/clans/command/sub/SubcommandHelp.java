package br.alkazuz.clans.command.sub;

import br.alkazuz.clans.command.SubCommandBase;
import br.alkazuz.clans.command.SubCommands;
import org.bukkit.command.CommandSender;

public class SubcommandHelp extends SubCommandBase {

        public SubcommandHelp() {
            super("ajuda", "clans.cmd.help", CommandTo.BOTH, "", "Mostra todos os comandos do sistema de clans", new String[] { "help", "ajuda", "?" }, false, false, null);
        }

        @Override
        public boolean execute(CommandSender sender, String[] args) {
            StringBuilder sb = new StringBuilder();
            sb.append("§e§lClans §7- §fComandos\n");
            sb.append("§7§m-----------------------------\n");
            for (SubCommandBase subCommandBase : SubCommands.getSubCommands()) {
                String errorMessage = subCommandBase.errorExecute(sender);
                if (errorMessage != null) {
                    continue;
                }
                sb.append(subCommandBase.getHelpMessage() + "\n");
            }
            sender.sendMessage(sb.toString());
            return true;
        }
}
