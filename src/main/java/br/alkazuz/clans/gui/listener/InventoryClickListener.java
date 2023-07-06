package br.alkazuz.clans.gui.listener;

import br.alkazuz.clans.gui.GuiInventory;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.services.ClanService;
import br.alkazuz.clans.services.ESettings;
import br.alkazuz.clans.services.ESortRanking;
import br.alkazuz.utils.GuiHolder;
import br.alkazuz.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements org.bukkit.event.Listener {


    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if (event.getInventory() != null &&
                event.getInventory().getHolder() != null &&
                    event.getInventory().getHolder() instanceof GuiHolder) {
            GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
            Player player = (Player) event.getPlayer();
            ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());
            if (holder.getId() == 3334 && clanPlayer.getClan() != null) {
                clanPlayer.getClan().save();
            }
        }
    }

    @EventHandler
    public void onClickInventoryClanRank(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GuiHolder) {
            GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
            if (holder.getId() == 3335) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                int page = (int) holder.getProperty("page");
                if (event.getCurrentItem() != null) {
                    if (event.getSlot() <= 8) {
                        ESortRanking sort = ESortRanking.getByName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                        if (sort != null) {
                            GuiInventory.openClanRank(player, sort, page);
                        }
                    } else if (event.getSlot() == 47) {
                        GuiInventory.openClanRank(player, ESortRanking.BALANCE, page - 1);
                    } else if (event.getSlot() == 51) {
                        GuiInventory.openClanRank(player, ESortRanking.BALANCE, page + 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClickInventoryClan(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GuiHolder) {
            GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
            if (holder.getId() == 3333) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());
                if (event.getSlot() == 16) {
                    GuiInventory.openClanRank((Player) event.getWhoClicked(), ESortRanking.BALANCE, 0);
                } else if (event.getSlot() == 14 && event.getCurrentItem() != null) {
                    Clan clan = clanPlayer.getClan();
                    if (clan == null) {
                        player.closeInventory();
                        return;
                    }
                    GuiInventory.openClanSettings(player, clanPlayer);
                }
            }
        }
    }

    @EventHandler
    public void onClickInventoryClanSettings(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GuiHolder) {
            GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
            if (holder.getId() == 3334) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());
                if (event.getSlot() == 35) {
                    GuiInventory.openClan(player, clanPlayer);
                } else if (event.getCurrentItem() != null) {
                    Clan clan = clanPlayer.getClan();
                    if (clan == null) {
                        player.closeInventory();
                        return;
                    }
                    ItemStack icon = event.getInventory().getItem(event.getSlot() - 9);
                    ESettings settings = ESettings.getByName(ChatColor.stripColor(icon.getItemMeta().getDisplayName()));
                    if (settings == null) {
                        return;
                    }
                    settings.toggleClan(clan);

                    if (settings.isClanActive(clan)) {
                        player.sendMessage("§aVocê ativou o " + settings.getName() + " do clan!");
                        clan.broadcast("§aO " + settings.getName() + " do clan foi ativado!");
                    } else {
                        player.sendMessage("§cVocê desativou o " + settings.getName() + " do clan!");
                        clan.broadcast("§cO " + settings.getName() + " do clan foi desativado!");
                    }
                    int i = 10;
                    for (ESettings settings_ : ESettings.values()) {
                        event.getInventory().setItem(i, new ItemBuilder(settings_.getIcon())
                                .setName("§a" + settings_.getName())
                                .addLore("", "§7" + settings_.getDescription()
                                ).toItemStack());
                        event.getInventory().setItem(i + 9, new ItemBuilder(Material.INK_SACK, 1, (byte) (settings_.isClanActive(clan) ? 10 : 8))
                                .setName("§a" + (settings_.isClanActive(clan) ? "Ativado" : "Desativado"))
                                .addLore("", "§7Clique para alterar"
                                ).toItemStack());
                        i += 2;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClickInventoryNoClan(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GuiHolder) {
            GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
            if (holder.getId() == 1532) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                ClanPlayer clanPlayer = ClanPlayerManager.getClanPlayer(player.getName());
                if (event.getSlot() == 10) {
                   player.closeInventory();
                   String errorMessage = ClanService.verifyRequirements(player, clanPlayer);
                     if (errorMessage != null) {
                         player.sendMessage(errorMessage);
                         return;
                     }
                    ClanService.openClanCreationProcess(player, clanPlayer);
                } else if (event.getSlot() == 15) {
                    // GuiInventory.openHelp((Player) event.getWhoClicked());
                } else if (event.getSlot() == 16) {
                    GuiInventory.openClanRank((Player) event.getWhoClicked(), ESortRanking.BALANCE, 0);
                }
            }
        }
    }

}
