package dev.corestone.lotrings.ringmanagers;

import dev.corestone.lotrings.LOTRings;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager extends BukkitRunnable {
    private LOTRings plugin;
    private double cooldown;
    private HashMap<UUID, Double> cooldowns = new HashMap<>();

    public CooldownManager(LOTRings plugin, double cooldown){
        this.plugin = plugin;
        this.cooldown = cooldown;
        this.runTaskTimer(plugin, 0L, 5L); //runs task 4 times a second.
    }
    public void setCooldown(UUID uuid){
        cooldowns.put(uuid, cooldown);
    }
    public void changeCooldown(double cooldown){
        this.cooldown = cooldown;
    }
    public double getExactTimeLeft(UUID uuid){
        return cooldowns.get(uuid);
    }
    public int getTimeLeftInt(UUID uuid){
        return cooldowns.get(uuid).intValue();
    }
    public boolean isOnCooldown(UUID uuid){
        if(!cooldowns.containsKey(uuid))return false;
        return cooldowns.get(uuid) > -1; //I used -1 instead of 0 because of the "off by one" error.
    }

    @Override
    public void run() {
        for(UUID uuid : cooldowns.keySet()){
            if(cooldowns.get(uuid) > -1){ //I used -1 instead of 0 because of the "off by one" error.
                cooldowns.replace(uuid, cooldowns.get(uuid)-0.25);
            }
        }
    }
}
