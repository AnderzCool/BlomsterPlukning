package dk.anderz.blomsterplukning.utils;

import dk.anderz.blomsterplukning.BlomsterPlukning;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Cooldown {

    public static HashMap<String, Long> LastRobbed = new HashMap<>();

    public static void setCooldown(String loc, Integer seconds){
        LastRobbed.put(loc, System.currentTimeMillis()+(seconds*1000));
    }
    public static boolean checkCooldown(Player p, String id, Integer seconds){
        if (p.hasMetadata(id)){
            Long meta = p.getMetadata(id).get(0).asLong();
            Long now = System.currentTimeMillis();
            if (!(now - meta <= seconds*1000)){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static Long getCooldown(String loc){
        if (LastRobbed.containsKey(loc)){
            Long meta = LastRobbed.get(loc);
            Long now = System.currentTimeMillis();
            return meta-now;
        }
        return 0L;
    }

    public static void wait(long ticks, Runnable callback) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(BlomsterPlukning.instance, callback, ticks);
    }
}