package dk.anderz.blomsterplukning.listeners;

import dk.anderz.blomsterplukning.BlomsterPlukning;
import dk.anderz.blomsterplukning.guis.FlowerMenu;
import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class NpcClick implements Listener {
    @EventHandler
    public void NPCRightClickEvent(NpcInteractEvent e) {
        if (e.getClickType() == InteractionType.RIGHT_CLICK) {
            String id = e.getEntry().getId();
            if (Objects.equals(id, BlomsterPlukning.configYML.getString("NPC.BlomsterMenu")) || Objects.equals(id, BlomsterPlukning.configYML.getString("NPC.BlomsterByenMenu"))) {
                FlowerMenu.flowerMenu(e.getPlayer());
            }
        }
    }
}
