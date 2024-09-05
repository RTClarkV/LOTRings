package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SlamAbility extends AbilitySuper {

    private double launchHeight;
    private double explosionPower;
    private double pushbackRange;
    private double pushbackHorizontalStrength;
    private double pushbackVerticalStrength;
    private int launchDurationTicks;
    private CooldownManager cooldownManager;
    private Sound launchSound;
    private Sound slamSound;
    private boolean fire;
    private boolean launchWhereLooking;
    private boolean isUsingAbility = false;

    public SlamAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.launchHeight = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "launch-height").doubleValue();
            this.launchDurationTicks = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "launch-duration-ticks").intValue();
            this.explosionPower = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "explosion-power").doubleValue();
            this.pushbackRange = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "pushback-range").doubleValue();
            this.pushbackHorizontalStrength = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "pushback-horizontal-strength").doubleValue();
            this.pushbackVerticalStrength = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "pushback-vertical-strength").doubleValue();
            this.launchSound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "launch-sound").toUpperCase());
            this.slamSound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "slam-sound").toUpperCase());
            this.fire = Boolean.parseBoolean(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "fire"));
            this.launchWhereLooking = Boolean.parseBoolean(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "launch-where-looking"));
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (!abilityCanBeUsed(event.getPlayer().getUniqueId())) return;
        if (cooldownManager.checkAndStartCooldown()) return;

        Player player = event.getPlayer();
        isUsingAbility = true;
        player.setVelocity(new Vector(0, launchHeight, 0));
        player.getWorld().playSound(player.getLocation(), launchSound, 10, 1);

        // Wait for the configurable duration before launching the player downwards
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!launchWhereLooking) {
                    player.setVelocity(new Vector(0, -launchHeight, 0)); // Launch back down
                } else {
                    Vector direction = player.getEyeLocation().getDirection();
                    direction.multiply(launchHeight * 2);
                    player.setVelocity(direction);
                }
                // Check repeatedly if the player has hit the ground
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnGround()) {
                            Location slamLocation = player.getLocation();
                            player.getWorld().createExplosion(slamLocation, (float) explosionPower, fire, false);
                            player.getWorld().playSound(slamLocation, slamSound, 10, 1);
                            for (Entity entity : slamLocation.getNearbyEntities(pushbackRange, pushbackRange, pushbackRange)) {
                                if (entity instanceof LivingEntity && entity != player) {
                                    LivingEntity target = (LivingEntity) entity;
                                    Vector vector = target.getLocation().toVector().subtract(slamLocation.toVector()).normalize();
                                    vector.multiply(pushbackHorizontalStrength).setY(pushbackVerticalStrength);
                                    target.setVelocity(vector);
                                }
                            }

                            // End of ability usage, stop fall damage protection
                            isUsingAbility = false;
                            this.cancel(); // Stop the runnable
                        }
                    }
                }.runTaskTimer(plugin, 0, 1); // Check every tick (1 tick = 50ms)
            }
        }.runTaskLater(plugin, launchDurationTicks); // Delay before launching downwards
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getUniqueId().equals(ring.getOwner()) && isUsingAbility) {
                event.setCancelled(true);
        }
    }
}
