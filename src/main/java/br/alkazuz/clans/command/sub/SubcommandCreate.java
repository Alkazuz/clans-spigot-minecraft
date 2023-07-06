package br.alkazuz.clans.command.sub;

import br.alkazuz.clans.command.SubCommandBase;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.services.ClanService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandCreate extends SubCommandBase {

        public SubcommandCreate() {
            super("criar", "clans.cmd.create", CommandTo.NO_CLAN, "", "Cria um novo clan"
                    , new String[]{"create", "criar"}, false, false, null);
        }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());
        String errorMessage = ClanService.verifyRequirements(player, clanPlayer);
        if (errorMessage != null) {
            player.sendMessage(errorMessage);
            return true;
        }
        ClanService.openClanCreationProcess(player, clanPlayer);
        return true;
    }
}
