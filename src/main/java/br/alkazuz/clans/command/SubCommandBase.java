package br.alkazuz.clans.command;

import br.alkazuz.clans.objects.ClanRoles;
import org.bukkit.command.CommandSender;

public class SubCommandBase {
    private final String name;
    private final String permission;
    private final String usage;
    private final String description;
    private final boolean admin;
    private ClanRoles[] clanRoles;

    public SubCommandBase(String name, String permission, String usage, String description, boolean admin, ClanRoles... clanRoles) {
        this.name = name;
        this.permission = permission;
        this.usage = usage;
        this.description = description;
        this.admin = admin;
        this.clanRoles = clanRoles;
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

    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }

    public boolean execute(CommandSender sender, String[] args) {
        return false;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public String getHelpMessage() {
        return "§c/" + this.usage + " §7- " + this.description;
    }

}
