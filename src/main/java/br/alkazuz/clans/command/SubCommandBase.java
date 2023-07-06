package br.alkazuz.clans.command;

import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.objects.ClanRoles;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SubCommandBase {
    private final String name;
    private final String permission;
    private final String usage;
    private final String description;
    private final String[] aliases;
    private final boolean admin;
    private final boolean specialToRole;
    private final CommandTo clanCommand;
    private ClanRoles[] clanRoles;

    public SubCommandBase(String name, String permission, CommandTo clanCommand, String usage, String description, String[] aliases, boolean admin, boolean specialToRole, ClanRoles... clanRoles) {
        this.name = name;
        this.permission = permission;
        this.usage = usage;
        this.description = description;
        this.aliases = aliases;
        this.admin = admin;
        this.specialToRole = specialToRole;
        this.clanRoles = clanRoles;
        this.clanCommand = clanCommand;
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getDescription() {
        return this.description;
    }

    public ClanRoles[] getClanRoles() {
        return this.clanRoles;
    }

    public String errorExecute(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (this.isAdmin() && !player.hasPermission("clans.admin")) {
                return "§cVocê não tem permissão para executar este comando.";
            }
            ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());

            if (clanCommand == CommandTo.CLAN) {
                if (clanPlayer.getClan() == null) {
                    return "§cVocê precisa estar em um clan para executar este comando.";
                }
            } else if (clanCommand == CommandTo.NO_CLAN) {
                if (clanPlayer.getClan() != null) {
                    return "§cVocê precisa estar sem clan para executar este comando.";
                }
            }

            if (clanRoles != null) {

                if (clanPlayer == null) {
                    return "§cVocê não tem permissão para executar este comando.";
                }
                if (clanPlayer.getClan() == null) {
                    return "§cVocê precisa estar em um clan para executar este comando.";
                }
                if (Arrays.asList(clanRoles).contains(clanPlayer.getRole())) {
                    return "§cSeu cargo no clan não tem permissão para executar este comando.";
                }
            }
        }
        if (!sender.hasPermission(this.permission)) {
            return "§cVocê não tem permissão para executar este comando.";
        }
        return null;
    }

    public boolean execute(CommandSender sender, String[] args) {
        return true;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean isSpecialToRole() {
        return specialToRole;
    }

    public String getHelpMessage() {
        String color = "§f";
        if (this.isAdmin()) {
            color = "§c";
        } else if (this.specialToRole) {
            color = "§e";
        }

        return String.format(
                "%s/clan %s %s §7- %s", color, this.name, this.usage, this.description
        );
    }
    public enum CommandTo {
        CLAN, NO_CLAN, BOTH
    }
}
