package br.alkazuz.clans.services;

import br.alkazuz.clans.objects.Clan;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public enum ESortRanking {
    KDR("KDR",
            "Ordenar por KDR",
            new ItemStack(Material.DIAMOND_SWORD),
            Clan::getKDR,
            clan -> String.format("§aKDR: §f%.2f", clan.getKDR())),

    KILLS("Kills",
            "Ordenar por Kills",
            new ItemStack(397, 1, (short) 3),
            clan ->(double) clan.getKills(),
            clan -> String.format("§aKills: §f%s", NumberFormat.getInstance().format(clan.getKills()))),

    POINTS("Gladiadores",
            "Ordenar por gladiadores ganho",
            new ItemStack(Material.DIAMOND_HELMET),
            clan -> (double) clan.getPoints(),
            clan -> String.format("§aGladiadores ganhos: §f%d", clan.getPoints())),

    BALANCE("Saldo",
            "Ordenar por Saldo",
            new ItemStack(Material.YELLOW_FLOWER),
            Clan::getBalance,
            clan -> String.format("§aSaldo depositado: §f%.2f", clan.getBalance()));
    private String name;
    private String description;
    private ItemStack icon;
    private final Function<Clan, Double> valueGetter;
    private final Function<Clan, String> render;

    private ESortRanking(String name, String description, ItemStack icon, Function<Clan, Double> valueGetter, Function<Clan, String> render) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.valueGetter = valueGetter;
        this.render = render;
    }

    public Function<Clan, Double> getValueGetter() {
        return valueGetter;
    }

    public Function<Clan, String> getRender() {
        return render;
    }

    public double getValue(Clan clan) {
        return valueGetter.apply(clan);
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

    public static ESortRanking getByName(final String name) {
        for (final ESortRanking sort : values()) {
            if (sort.getName().equalsIgnoreCase(name)) {
                return sort;
            }
        }
        return null;
    }

    public Comparator<? super Clan> getComparator() {
        return Comparator.comparingDouble(this::getValue);
    }
}
