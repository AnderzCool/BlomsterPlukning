package dk.anderz.blomsterplukning.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dk.anderz.blomsterplukning.BlomsterPlukning;
import dk.anderz.blomsterplukning.configuration.Guis;
import dk.anderz.blomsterplukning.configuration.Messages;
import dk.anderz.blomsterplukning.utils.ColorUtils;
import dk.anderz.blomsterplukning.utils.Econ;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlowerMenu {

    public static void flowerMenu(Player player) {

        int toolPrize = BlomsterPlukning.configYML.getInt("Blomster.toolPrize");
        String toolName = ColorUtils.getColored(BlomsterPlukning.configYML.getString("Blomster.toolName") );
        String prefix = Guis.getColored("blomster.menu.name")[0];

        player.playSound(player.getLocation(), Sound.CLICK, 1, 10);

        Gui gui = Gui.gui().title(Component.text(prefix))
                .rows(5)
                .disableAllInteractions()
                .create();


        gui.getFiller().fillTop(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, (short) 1)).name(Component.text("§f")).asGuiItem());
        gui.getFiller().fillBottom(ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE)).name(Component.text("§f")).asGuiItem());
        ItemStack back = new ItemStack(Material.INK_SACK, 1, (short) 1);
        gui.setItem(36, ItemBuilder.from(back).name(Component.text("§fLuk menu.")).asGuiItem(event -> {
            event.getWhoClicked().closeInventory();
            player.playSound(player.getLocation(), Sound.CLICK, 1, 10);
        }));

        gui.setItem(22, ItemBuilder.from(new ItemStack(Material.DIAMOND_HOE)).setName(toolName).setLore(Guis.getColored("blomster.menu.item.lore", "\\{pris\\}", String.valueOf(toolPrize))).asGuiItem(event -> {
            String groups = PlaceholderAPI.setPlaceholders(player, "%luckperms_groups%");
            if (groups.contains("vagt") || groups.contains("officer") || groups.contains("inspektør") || groups.contains("direktør")) {
                Messages.send(player, "blomster.shop.vagt");
                return;
            }
            if (Econ.getbalance(player.getName()) >= toolPrize) {
                Econ.removeMoney(player.getName(), toolPrize);
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_HOE, 1) {{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(toolName);
                    setItemMeta(meta);
                }});
                Messages.send(player, "blomster.shop.successful", "\\{item\\}", toolName, "\\{pris\\}", String.valueOf(toolPrize));
                player.closeInventory();
            } else {
                Messages.send(player, "blomster.shop.no-money");
                player.closeInventory();
            }
        }));
        gui.open(player);
    }
}
