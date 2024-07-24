package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RainFireballsAbility extends AbilitySuper {

    private double radius;
    private float power;
    private CooldownManager cooldownManager;
    private Sound sound;

    public RainFireballsAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.radius = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "radius");
            this.power = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "power");
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @Override
    public void switchState(RingState ringState) {
        if (ringState == RingState.LOST) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (!abilityCanBeUsed(event.getPlayer().getUniqueId())) return;
        if (cooldownManager.checkAndStartCooldown()) return;

        Player player = event.getPlayer();
        World world = player.getWorld();
        Location center = player.getLocation();

        world.playSound(center, sound, 10, 1);

        new BukkitRunnable() {
            final Random random = new Random();

            @Override
            public void run() {
                for (int i = 0; i < power; i++) {
                    Location randomLocation = getRandomLocation(center, radius);
                    if (randomLocation != null) {
                        spawnFireball(randomLocation);
                    }
                }
            }

            private Location getRandomLocation(Location center, double radius) {
                double x = center.getX() + (random.nextDouble() * 2 - 1) * radius;
                double z = center.getZ() + (random.nextDouble() * 2 - 1) * radius;
                return center.clone().add(x, 0, z);
            }

            private void spawnFireball(Location location) {
                Fireball fireball = (Fireball) location.getWorld().spawnEntity(location, EntityType.FIREBALL);
                fireball.setDirection(new org.bukkit.util.Vector(0, -1, 0));
                fireball.setIsIncendiary(false);
                fireball.setYield(0);
            }
        }.runTask(plugin);
    }
}
