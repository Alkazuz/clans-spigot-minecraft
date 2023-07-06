package br.alkazuz.clans.services;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import br.alkazuz.clans.objects.Clan;

import java.util.function.Consumer;
import java.util.function.Predicate;

public enum ESettings {
    FRIENDLY_FIRE("Fogo Amigo",
            "Permite que membros do clan se ataquem",
            new ItemStack(Material.FIREBALL),
            clan -> clan.isFriendlyFire(),
            clan -> clan.setFriendlyFire(!clan.isFriendlyFire()));

    private String name;
    private String description;
    private ItemStack icon;
    private final Predicate<Clan> isActiveChecker;
    private final Consumer<Clan> toggler;

    private ESettings(String name, String description, ItemStack icon, Predicate<Clan> isActiveChecker, Consumer<Clan> toggler) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.isActiveChecker = isActiveChecker;
        this.toggler = toggler;
    }

    public boolean isClanActive(Clan clan) {
        return isActiveChecker.test(clan);
    }

    public void toggleClan(Clan clan) {
        toggler.accept(clan);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public static ESettings getByName(final String name) {
        for (final ESettings settings : values()) {
            if (settings.getName().equalsIgnoreCase(name)) {
                return settings;
            }
        }
        return null;
    }

}
