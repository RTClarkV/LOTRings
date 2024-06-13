package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.UUID;


public class FireBallAbility extends AbilitySuper implements Listener {
    private CooldownManager cooldownManager;
    private double damage;
    private int explosionRadius;
    private boolean fire;
    private String fireballType;
    private double speedMultiplier;
    private int burst;
    private int burstInterval;
    private BukkitScheduler scheduler;
    private ArrayList<UUID> entityIDs = new ArrayList<>();

    public FireBallAbility(LOTRings plugin, Ring ring, String abilityName) {
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
                if(ring.getState() != RingState.LOST) shootFireball(event.getPlayer());
            }, (long) j *burstInterval);
        }

        cooldownManager.startCooldown();
    }
    public void shootFireball(Player player){
        if(fireballType.equals("LARGE")){
            LargeFireball fireball = player.launchProjectile(LargeFireball.class, player.getLocation().getDirection());
            fireball.setDirection(player.getLocation().getDirection().multiply(speedMultiplier));
            entityIDs.add(fireball.getUniqueId());
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10, 0);
        }
        if(fireballType.equals("NORMAL")){
            Fireball fireball = player.launchProjectile(Fireball.class, player.getLocation().getDirection());
            fireball.setDirection(player.getLocation().getDirection().multiply(speedMultiplier));
            entityIDs.add(fireball.getUniqueId());
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10, 1);
        }
        if(fireballType.equals("SMALL")){
            SmallFireball fireball = player.launchProjectile(SmallFireball.class, player.getLocation().getDirection());
            fireball.setDirection(player.getLocation().getDirection().multiply(speedMultiplier));
            entityIDs.add(fireball.getUniqueId());
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10, 2);
        }

    }

    @EventHandler
    public void onImpact(ProjectileHitEvent event){
        if(!entityIDs.contains(event.getEntity().getUniqueId()))return;
        entityIDs.remove(event.getEntity().getUniqueId());
        if(explosionRadius > 0) event.getEntity().getWorld().createExplosion(event.getEntity(), explosionRadius, fire);
        if(event.getHitEntity() == null)return;
        if(event.getHitEntity() instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) event.getHitEntity();
            livingEntity.damage(damage, event.getEntity());
        }
    }

    @Override
    public void switchState(RingState ringState){
        if(ringState == RingState.LOST) HandlerList.unregisterAll(this);
    }
}
