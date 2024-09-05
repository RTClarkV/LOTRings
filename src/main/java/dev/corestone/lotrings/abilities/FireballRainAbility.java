package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;


public class FireballRainAbility extends AbilitySuper implements Listener {
    private CooldownManager cooldownManager;
    private double damage;
    private int explosionRadius;
    private boolean fire;
    private String fireballType;
    private double speedMultiplier;
    private int burst;
    private int burstInterval;
    private BukkitScheduler scheduler;
    private boolean immune = false;
    private ArrayList<UUID> entityIDs = new ArrayList<>();

    public FireballRainAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.scheduler = plugin.getServer().getScheduler();
        //double bob = (double)plugin.getAbilityDataManager().getAbilityData(abilityName, "cooldown-seconds");
        try{
            this.cooldownManager = new CooldownManager(plugin,this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            this.explosionRadius =  plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "explosion-radius").intValue();
            this.damage = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "damage").doubleValue();
            this.fire = Boolean.parseBoolean(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "fire"));
            this.fireballType = plugin.getAbilityDataManager().getAbilityStringData(abilityName, "fireball-type").toUpperCase();
            this.speedMultiplier = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "speed-multiplier").doubleValue();
            this.burst = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "burst").intValue();
            this.burstInterval = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "burst-interval-ticks").intValue();
        }catch (Exception e){
            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.abilityLoadError(abilityName, abilityType.toString())));
        }
    }
    @EventHandler
    public void onUse(PlayerInteractEvent event){
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
        if(!event.getAction().isRightClick())return;
        if(cooldownManager.isOnCooldown()) return;

        for(int j = 0; j < burst; j++){
            scheduler.runTaskLater(plugin, ()->{
                shootFireball(event.getPlayer());
            }, (long) j *burstInterval);
        }

        cooldownManager.startCooldown();
    }
    public void shootFireball(Player player) {
        Location spawnLocation = player.getLocation().add(0, 10, 0); // Adjust the location 10 blocks above the player
        Vector direction = player.getLocation().getDirection().multiply(speedMultiplier);
        direction.setY(direction.getY() - 1); // Add a downward component to the direction

        if (fireballType.equals("LARGE")) {
            LargeFireball fireball = player.getWorld().spawn(spawnLocation, LargeFireball.class);
            fireball.setDirection(direction);
            entityIDs.add(fireball.getUniqueId());
            player.getWorld().playSound(spawnLocation, Sound.ENTITY_GHAST_SHOOT, 10, 0);
        }

        if (fireballType.equals("NORMAL")) {
            Fireball fireball = player.getWorld().spawn(spawnLocation, Fireball.class);
            fireball.setDirection(direction);
            entityIDs.add(fireball.getUniqueId());
            player.getWorld().playSound(spawnLocation, Sound.ENTITY_GHAST_SHOOT, 10, 1);
        }

        if (fireballType.equals("SMALL")) {
            SmallFireball fireball = player.getWorld().spawn(spawnLocation, SmallFireball.class);
            fireball.setDirection(direction);
            entityIDs.add(fireball.getUniqueId());
            player.getWorld().playSound(spawnLocation, Sound.ENTITY_GHAST_SHOOT, 10, 2);
        }
    }

    @EventHandler
    public void onImpact(ProjectileHitEvent event){
        if(!entityIDs.contains(event.getEntity().getUniqueId()))return;
        entityIDs.remove(event.getEntity().getUniqueId());
        immune = true;
        if(explosionRadius > 0) event.getEntity().getWorld().createExplosion(event.getEntity(), explosionRadius, fire);
        immune = false;
        if(event.getHitEntity() == null)return;
        if(event.getHitEntity() instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) event.getHitEntity();
            if (livingEntity instanceof Player && ring.getOwner().equals(livingEntity)) return;
            livingEntity.damage(damage, event.getEntity());
        }

    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!immune) return;
        Player player = (Player) event.getEntity();
        if (player.getUniqueId().equals(ring.getOwner())) event.setCancelled(true);
    }
}
