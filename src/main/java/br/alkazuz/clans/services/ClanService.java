package br.alkazuz.clans.services;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.config.Settings;
import br.alkazuz.clans.manager.ClanManager;
import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.objects.ClanRoles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

public class ClanService {
    public static String verifyRequirements(Player player, ClanPlayer clanPlayer) {
        if (clanPlayer.getClan() != null) {
            return "Você já está em um clan.";
        }
        if (player.hasPermission("clan.create.bypass")) {
            return null;
        }
        if (!Clans.getInstance().getEconomy().has(player.getName(), Settings.CLAN_PRICE)) {
            return "Você não tem dinheiro suficiente para criar um clan.";
        }
        return null;
    }

    public static void openClanCreationProcess(Player player, ClanPlayer clanPlayer) {
        player.sendMessage("§aDigite o nome do clan. Para cancelar digite §ccancelar§a.");
        Clan clan = new Clan("", "");
        processClanName(player, clanPlayer, clan);
    }

    private static void processClanName(Player player, ClanPlayer clanPlayer, Clan clan) {
        Clans.getInstance().getEventWaiter()
                .waitForEvent(PlayerChatEvent.class, EventPriority.LOW,
                        event -> player.getName().equals(event.getPlayer().getName()),
                        event -> {
                            event.setCancelled(true);
                            if (event.getMessage().equalsIgnoreCase("cancelar")) {
                                player.sendMessage("§cVocê cancelou a criação do clan.");
                                return;
                            }
                            String clanName = event.getMessage();
                            String errorMessage = validateClanName(clanName);
                            if (errorMessage != null) {
                                player.sendMessage("§c" + errorMessage);
                                processClanName(player, clanPlayer, clan);
                                return;
                            }
                            if (ClanManager.clanExists(clanName)) {
                                player.sendMessage("§cJá existe um clan com esse nome.");
                                processClanName(player, clanPlayer, clan);
                                return;
                            }
                            clan.setName(clanName);
                            processClanTag(player, clanPlayer, clan);
                        }, 15 * 20, () -> {
                            player.sendMessage("§cVocê demorou muito para digitar o nome do clan.");
                        });
    }

    private static void processClanTag(Player player, ClanPlayer clanPlayer, Clan clan) {
        player.sendMessage("§aDigite a tag do clan. Para cancelar digite §ccancelar§a.");
        Clans.getInstance().getEventWaiter()
                .waitForEvent(PlayerChatEvent.class, EventPriority.LOW,
                        event -> player.getName().equals(event.getPlayer().getName()),
                        event -> {
                            event.setCancelled(true);
                            if (event.getMessage().equalsIgnoreCase("cancelar")) {
                                player.sendMessage("§cVocê cancelou a criação do clan.");
                                return;
                            }
                            String clanTag = event.getMessage();
                            String errorMessage = validateClanTag(clanTag);
                            if (errorMessage != null) {
                                player.sendMessage("§c" + errorMessage);
                                processClanTag(player, clanPlayer, clan);
                                return;
                            }
                            if (ClanManager.clanTagExists(clanTag)) {
                                processClanTag(player, clanPlayer, clan);
                                player.sendMessage("§cJá existe um clan com essa tag.");
                                return;
                            }
                            clan.setTag(clanTag);
                            errorMessage = verifyRequirements(player, clanPlayer);
                            if (errorMessage != null) {
                                player.sendMessage("§c" + errorMessage);
                                return;
                            }
                            Clans.getInstance().getEconomy().withdrawPlayer(player.getName(), Settings.CLAN_PRICE);
                            try {
                                clan.setOwner(player.getName());
                                ClanManager.createClan(clan);
                                clanPlayer.setClan(clan);
                                clanPlayer.setRole(ClanRoles.LEADER);
                                clanPlayer.save();
                                player.getWorld().playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                                player.sendMessage("§aClan criado com sucesso.");
                                Bukkit.broadcastMessage("§aO jogador §f" + player.getDisplayName() + " §acriou o clan §f" + clan.getName() + "§a.");
                            } catch (IllegalArgumentException e) {
                                player.sendMessage("§c" + e.getMessage());
                            } catch (Exception e) {
                                player.sendMessage("§cOcorreu um erro ao criar o clan.");
                                e.printStackTrace();
                            }

                        }, 15 * 20, () -> {
                            player.sendMessage("§cVocê demorou muito para digitar a tag do clan.");
                        });
    }

    private static String validateClanTag(String clanTag) {
        if (clanTag.length() != 3) {
            return "A tag do clan deve ter 3 caracteres.";
        }
        if (!clanTag.matches("[a-zA-Z0-9]+")) {
            return "A tag do clan deve conter apenas letras e números.";
        }
        return null;
    }

    private static String validateClanName(String clanName) {
        if (clanName.length() < 3) {
            return "O nome do clan deve ter no mínimo 3 caracteres.";
        }
        if (clanName.length() > 16) {
            return "O nome do clan deve ter no máximo 16 caracteres.";
        }
        if (!clanName.matches("[a-zA-Z0-9 ]+")) {
            return "O nome do clan deve conter apenas letras, números e espaços.";
        }
        return null;
    }

}
