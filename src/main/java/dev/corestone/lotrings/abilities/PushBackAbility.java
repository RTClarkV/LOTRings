package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PushBackAbility extends AbilitySuper {

    private double range;
    private double powerXZ;
    private Sound sound;
    private double powerY;
    private CooldownManager cooldownManager;

    public PushBackAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try{
            this.range = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "range").doubleValue();
            this.powerXZ = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "horizontal-power").doubleValue();
            this.powerY = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "vertical-power").doubleValue();
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @EventHandler
    public void onuUse(PlayerInteractEvent event){
        if(!event.getAction().isRightClick())return;
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
        if(cooldownManager.isOnCooldown())return;
        cooldownManager.startCooldown();
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), sound, 10, 1);
        for(LivingEntity entity : event.getPlayer().getLocation().getNearbyLivingEntities(range)){
            if(entity.getUniqueId()!=ring.getOwner()){
                Vector vector = entity.getLocation().toVector().subtract(event.getPlayer().getLocation().toVector());
                double mag = vector.clone().length();
                double thetaX = Math.acos(vector.getX()/mag);
                double thetaZ = Math.acos(vector.getZ()/mag);
                vector.setX(Math.cos(thetaX)*powerXZ);
                vector.setZ(Math.cos(thetaZ)*powerXZ);
                vector.setY(powerY);
                entity.setVelocity(vector);
            }
        }
    }
}
