package br.alkazuz.clans.command;


import br.alkazuz.clans.command.sub.*;
import br.alkazuz.clans.command.sub.admin.SubcommandPoints;
import br.alkazuz.clans.command.sub.admin.SubcommandWarning;

import java.util.ArrayList;
import java.util.List;

public class SubCommands {
    private static final List<SubCommandBase> subCommands = new ArrayList<>();

    public static void load() {
        subCommands.add(new SubcommandCreate());
        subCommands.add(new SubcommandAccept());
        subCommands.add(new SubcommandHelp());

        // role
        subCommands.add(new SubcommandInvite());
        subCommands.add(new SubcommandKick());

        //admin
        subCommands.add(new SubcommandWarning());
        subCommands.add(new SubcommandPoints());
    }

    public static List<SubCommandBase> getSubCommands() {
        return subCommands;
    }

    public static void registerSubCommand(SubCommandBase subCommandBase) {
        subCommands.add(subCommandBase);
    }

    public static SubCommandBase getSubCommand(String string) {
        for (SubCommandBase subCommandBase : subCommands) {
            if (subCommandBase.getName().equalsIgnoreCase(string)) {
                return subCommandBase;
            }
            if (subCommandBase.getAliases() != null) {
                for (String alias : subCommandBase.getAliases()) {
                    if (alias.equalsIgnoreCase(string)) {
                        return subCommandBase;
                    }
                }
            }
        }
        return null;
    }
}
