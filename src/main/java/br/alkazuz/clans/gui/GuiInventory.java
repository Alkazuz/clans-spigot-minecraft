package br.alkazuz.clans.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GuiInventory {
    public static void openNoClan(Player player) {

        Inventory inventory = Bukkit.createInventory(null, "Menu de Clans", 3);
    }
}
