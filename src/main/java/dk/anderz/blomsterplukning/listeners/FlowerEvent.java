package dk.anderz.blomsterplukning.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.anderz.blomsterplukning.BlomsterPlukning;
import dk.anderz.blomsterplukning.configuration.Messages;
import dk.anderz.blomsterplukning.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FlowerEvent implements Listener {

    private final HashMap<UUID, Integer> pickedFlowers = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null || !isPlayerInFlowerRegion(player))
            return;

        String prefix = ColorUtils.getColored(BlomsterPlukning.configYML.getString("Blomster.chat-prefix"));
        String toolName = BlomsterPlukning.configYML.getString("Blomster.toolName");
        int flowerChance = BlomsterPlukning.configYML.getInt("Blomster.chance");
        if (event.getItem() == null || !event.getItem().hasItemMeta() || !ColorUtils.getColored(toolName).equals(event.getItem().getItemMeta().getDisplayName())) {
            return;
        }


        Block clickedBlock = event.getClickedBlock();
        UUID playerUUID = player.getUniqueId();

        Location flowerLocation = clickedBlock.getLocation();
        Material clickedMaterial = clickedBlock.getType();

        if (clickedMaterial != Material.RED_ROSE && clickedMaterial != Material.YELLOW_FLOWER)
            return;

        if (pickedFlowers.containsKey(playerUUID)) {
            player.sendMessage(prefix + "\n §7Du kan kun plukke én blomst ad gangen.");
            return;
        }

        if (Cooldown.getCooldown(flowerLocation.toString()) > 0) {
            int cooldownInSeconds = Math.toIntExact(Cooldown.getCooldown(flowerLocation.toString()) / 1000);
            player.sendMessage(prefix + "\n §7Du skal vente §f" + TimeFormatter.secToMin(cooldownInSeconds) + "§7, før du kan plukke denne blomst.");
            return;
        }

        Cooldown.setCooldown(flowerLocation.toString(), 600);
        int checkDistanceId = new BukkitRunnable() {
            final Location loc = player.getLocation();
            int secondsLeft = ((10) * 20) / 5;;

            @Override
            public void run() {
                if (!player.isOnline() || player.getLocation().distance(loc) > 0.5) {
                    Messages.send(player, "blomster.event.fail");
                    Cooldown.setCooldown(flowerLocation.toString(), 10);
                    cancel();
                    pickedFlowers.remove(playerUUID);
                    return;
                }

                if (secondsLeft <= 0) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(1001);
                    if (randomNumber <= flowerChance) {
                        short[] flowerDataValues = {0, 1, 2, 3, 4, 5, 6, 7, 8};
                        int randomIndex = random.nextInt(flowerDataValues.length);
                        short randomFlowerDataValue = flowerDataValues[randomIndex];
                        ItemStack flower = new ItemStack(Material.RED_ROSE, 1, randomFlowerDataValue);
                        player.getInventory().addItem(flower);

                        Bukkit.getScheduler().runTask(BlomsterPlukning.instance, () -> {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "logs add item-RED_ROSE:" + randomFlowerDataValue + " 1");
                        });
                        System.out.println("Added flower (RED_ROSE) with id: " + randomFlowerDataValue + " to logs. (/logs add item-RED_ROSE:" + randomFlowerDataValue + " 1)");

                        Bukkit.broadcastMessage(Messages.get("blomster.event.broadcast", "\\{player\\}", player.getName())[0]);
                    } else {
                        int money = random.nextInt(5000) + 1500;
                        Messages.send(player, "blomster.event.no-flower", "\\{money\\}", String.valueOf(money));
                        Econ.addMoney(player, (double) money);
                    }
                    cancel();
                    pickedFlowers.remove(playerUUID);
                    return;
                }
                secondsLeft--;
                ActionBar.sendActionbar(player, Messages.get("blomster.event.actionbar", "\\{time\\}", String.valueOf(Math.floor(100.0 - (secondsLeft * 100.0 / 39))).replace(".0", ""))[0]);

            }
        }.runTaskTimer(BlomsterPlukning.instance, 5L, 5L).getTaskId();
        pickedFlowers.put(playerUUID, checkDistanceId);
    }


    private boolean isPlayerInFlowerRegion(Player player) {
        Set<ProtectedRegion> regions = BlomsterPlukning.getRegionAtLocation(player.getLocation());

        if (regions == null || regions.isEmpty()) {
            return false;
        }

        List<String> flowerRegions = BlomsterPlukning.configYML.getStringList("Blomster.regions");

        if (flowerRegions == null || flowerRegions.isEmpty()) {
            return false;
        }

        for (ProtectedRegion region : regions) {
            String regionId = region.getId();
            if (flowerRegions.contains(regionId)) {
                return true;
            }
        }

        return false;
    }
}
