package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.Random;

public class TornadoAbility extends AbilitySuper {
    private LOTRings plugin;
    private double magnitude;
    private double liftMag;
    private double Pmag;
    private double duration;
    private double radius;
    private Random random = new Random();
    private BukkitScheduler scheduler;
    private CooldownManager cooldownManager;
    private Sound sound;
    public TornadoAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        this.scheduler = plugin.getServer().getScheduler();
        try{
            this.plugin = plugin;
            this.magnitude = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "magnitude");
            this.liftMag = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "lift-magnitude");
            this.Pmag = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "push-pull-mag");
            this.radius = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "radius");
            this.duration = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "duration-seconds");
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds"));
        }catch (Exception e){
            sendLoadError();
        }
    }


    @EventHandler
    private void onUse(PlayerInteractEvent event){
        if(!event.getAction().isRightClick())return;
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
        if(cooldownManager.checkAndStartCooldown())return;
        twisterEffect(duration*10);
    }

    private void twisterEffect(double count){
        if (count <= 0) return;
        if (ring.isLost() || !ring.isHeld()) return;
        for(LivingEntity livingEntity : Bukkit.getPlayer(ring.getOwner()).getLocation().getNearbyLivingEntities(radius)) {
            if (livingEntity.getUniqueId().equals(ring.getOwner())) continue; //if the entity is the owner don't go crazy.
            if (livingEntity.isDead()) continue;
            //if (livingEntity.getLocation().distance(Bukkit.getPlayer(ring.getOwner()).getLocation()) > radius) continue;

            double Xrel = livingEntity.getLocation().toVector().getX() - Bukkit.getPlayer(ring.getOwner()).getLocation().toVector().getX() + getRan(-1, 1); //relative X cord.

            double Zrel = livingEntity.getLocation().toVector().getZ() - Bukkit.getPlayer(ring.getOwner()).getLocation().toVector().getZ() + getRan(-1, 1); // relative Y cord.

            double mag = Math.sqrt(Xrel * Xrel + Zrel * Zrel);

            double Y = liftMag; // up or down factor.

            double m = -((double) Math.round(Zrel) / Math.round(Xrel));

            double thetaX = Math.acos(Xrel / mag);
            double thetaZ = Math.acos(Zrel / mag);

            double Zinward = Math.cos(thetaZ) * Pmag;

            double Xinward = Math.cos(thetaX) * Pmag;

            double X = magnitude * Math.cos(thetaX);

            double Z = magnitude * Math.cos(thetaZ);

            X *= -1;

            Vector vector = livingEntity.getVelocity();
            vector.setX(Z + Xinward); // yes, I swapped the X and Z on purpose
            vector.setZ(X + Zinward);
            vector.setY(Y);
            livingEntity.setVelocity(vector);
        }

        scheduler.runTaskLater(plugin, (task)->{
            twisterEffect(count-1);
        }, 2L);
    }
    public int getRan(int min, int max){
        int a = random.nextInt(max+1-min);
        a += min;
        return a;
    }
}
