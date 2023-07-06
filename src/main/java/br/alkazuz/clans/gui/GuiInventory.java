package br.alkazuz.clans.gui;

import br.alkazuz.clans.Clans;
import br.alkazuz.clans.config.Settings;
import br.alkazuz.clans.manager.ClanManager;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.objects.Clan;
import br.alkazuz.clans.objects.ClanPlayer;
import br.alkazuz.clans.services.ESettings;
import br.alkazuz.clans.services.ESortRanking;
import br.alkazuz.utils.GuiHolder;
import br.alkazuz.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GuiInventory {
    private static final int MAX_PER_PAGE = 24;
    private static final int[] RANKING_SLOTS = new int[] {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public static void openClanRank(Player player, ESortRanking eSortRanking, int page) {
        Inventory inventory;
        GuiHolder holder;

        if (player.getOpenInventory() != null
                && player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder
                && ((GuiHolder) player.getOpenInventory().getTopInventory().getHolder()).getId() == 3335) {
            inventory = player.getOpenInventory().getTopInventory();
            holder = (GuiHolder) inventory.getHolder();
        } else {
            Map<String, Object> properties = new HashMap<>();
            properties.put("page", page);
            holder = new GuiHolder(3335, properties);
            inventory = Clans.getInstance().getServer().createInventory(holder, 9 * 6,
                    "Ranking de Clans - " + (page + 1));
        }
        int i = 1;

        for (ESortRanking sortRanking : ESortRanking.values()) {
            inventory.setItem(i, new ItemBuilder(eSortRanking == sortRanking ? new ItemStack(Material.BEDROCK) : sortRanking.getIcon())
                    .setName("§a" + sortRanking.getName())
                    .addLore("", "§7Clique para alterar"
                    ).toItemStack());
            i += 2;
        }

        List<Clan> allClans = new ArrayList<>(ClanManager.getClans().values());
        allClans.sort(eSortRanking.getComparator());

        int totalPages = (allClans.size() + MAX_PER_PAGE - 1) / MAX_PER_PAGE;

        int startIndex = page * MAX_PER_PAGE;
        int endIndex = Math.min(startIndex + MAX_PER_PAGE, allClans.size());
        if (endIndex >= allClans.size()) {
            endIndex = allClans.size();
        }

        List<Clan> currentPageItems = allClans.subList(startIndex, endIndex);

        int slot = 0;
        for (Clan clan : currentPageItems) {
            inventory.setItem(RANKING_SLOTS[slot],
                    new ItemBuilder(Material.PAINTING)
                            .setName(clan.displayNameWithTag())
                            .addLore("", "§aDono: §f" + clan.getOwner(),
                                    eSortRanking.getRender().apply(clan)
                            ).toItemStack()
                    );
        }
        if (page > 0) {
            inventory.setItem(47, new ItemBuilder(Material.ARROW)
                    .setName("§aPágina anterior")
                    .addLore("", "§7Clique para ir para a página anterior"
                    ).toItemStack());
        }

        if (page < totalPages - 1) {
            inventory.setItem(51, new ItemBuilder(Material.ARROW)
                    .setName("§aPróxima página")
                    .addLore("", "§7Clique para ir para a próxima página"
                    ).toItemStack());
        }
        if (!(player.getOpenInventory() != null
                && player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder
                && ((GuiHolder) player.getOpenInventory().getTopInventory().getHolder()).getId() == 3335)) {
            player.openInventory(inventory);
        } else {
            player.updateInventory();
        }
    }

    public static void openClanSettings(Player player, ClanPlayer clanPlayer) {
        Clan clan = clanPlayer.getClan();
        GuiHolder holder = new GuiHolder(3334);
        Inventory inventory = Clans.getInstance().getServer().createInventory(holder, 9 *  4,
                "Configurações do Clan - " + clanPlayer.getClan().getTag());
        int i = 10;
        for (ESettings settings : ESettings.values()) {
            inventory.setItem(i, new ItemBuilder(settings.getIcon())
                    .setName("§a" + settings.getName())
                    .addLore("", "§7" + settings.getDescription()
                    ).toItemStack());
            inventory.setItem(i + 9, new ItemBuilder(Material.INK_SACK, 1, (byte) (settings.isClanActive(clan) ? 10 : 8))
                    .setName("§a" + (settings.isClanActive(clan) ? "Ativado" : "Desativado"))
                    .addLore("", "§7Clique para alterar"
                    ).toItemStack());
            i += 2;
        }

        inventory.setItem(35, new ItemBuilder(Material.ARROW)
                .setName("§aVoltar")
                .addLore("", "§7Clique para voltar"
                ).toItemStack());

        player.openInventory(inventory);
    }
    public static void openClan(Player player, ClanPlayer clanPlayer) {
        Clan clan = clanPlayer.getClan();
        GuiHolder holder = new GuiHolder(3333);
        Inventory inventory = Clans.getInstance().getServer().createInventory(holder, 9 *  3,
                "Menu do Clan - " + clanPlayer.getClan().getTag());
        inventory.setItem(10, new ItemBuilder(Material.EMPTY_MAP)
                .setName(clanPlayer.getClan().displayNameWithTag())
                .addLore("",
                        "§aTag: §f" + clanPlayer.getClan().getTag(),
                        "§aDono: §f" + clanPlayer.getClan().getOwner(),
                        "§aMembros (Online): §f" + String.format("%d/%d", clan.getOnlineMembers().size(), ClanPlayerManager.getClanPlayersFromClan(clan).size()),
                        "§aKDR: §f" + String.format("%.2f", clan.getKDR()),
                        "",
                        "§aPontos: §f" + NumberFormat.getInstance().format(clan.getPoints()),
                        "§aSaldo: §f" + NumberFormat.getInstance().format(clan.getBalance()),
                        "§aFundado em: §f" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(clan.getCreatedAt()),
                        "",
                        "§4Alertas: §c" + clan.getWarnings()
                ).toItemStack());
        inventory.setItem(11, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                .setName(clanPlayer.getDisplayName())
                .addLore("", "§aSeu cargo: §7" + clanPlayer.getRole().getName(),
                        "§aKDR: §f" + String.format("%.2f", clanPlayer.getKDR())
                ).toItemStack());

        inventory.setItem(15, new ItemBuilder(Material.SIGN)
                .setName("§aAjuda")
                .addLore("", "§7Ver a lista de comandos", "§7Clique para ver a lista de comandos"
                ).toItemStack());
        inventory.setItem(16, new ItemBuilder(Material.BOOK)
                .setName("§aClans")
                .addLore("", "§7Ver todos os clans", "§7Clique para ver todos os clans"
                ).toItemStack());
        if (clanPlayer.getRole().canToggleFriendlyFire()) {
            inventory.setItem(14, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                    .setName("§aConfigurações")
                    .addLore("", "§7Clique para alterar as configurações do clan"
                    ).toItemStack());
        }

        player.openInventory(inventory);
    }

    public static void openNoClan(Player player) {
        GuiHolder holder = new GuiHolder(1532);
        Inventory inventory = Clans.getInstance().getServer().createInventory(holder, 9 *  3, "Menu de Clans");
        inventory.setItem(10, new ItemBuilder(Material.ANVIL)
                .setName("§aCriar Clan")
                .addLore("", "§7Criar um novo clan", "§7Preço para criar um clan §e"
                        + NumberFormat.getInstance().format(Settings.CLAN_PRICE)
                ).toItemStack());

        inventory.setItem(15, new ItemBuilder(Material.SIGN)
                .setName("§aAjuda")
                .addLore("", "§7Ver a lista de comandos", "§7Clique para ver a lista de comandos"
                ).toItemStack());
        inventory.setItem(16, new ItemBuilder(Material.BOOK)
                .setName("§aClans")
                .addLore("", "§7Ver todos os clans", "§7Clique para ver todos os clans"
                ).toItemStack());

        player.openInventory(inventory);
    }
}
