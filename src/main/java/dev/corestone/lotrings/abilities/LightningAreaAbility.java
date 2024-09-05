package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.Utilities.LocationUtils;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningAreaAbility extends AbilitySuper {

    private int boltCount;
    private int strikeRadius;
    private long strikeDurationTicks;
    private Sound sound;
    private CooldownManager cooldownManager;
    private boolean active = false;

    public LightningAreaAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.boltCount = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "bolt-count").intValue();
            this.strikeRadius = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "strike-radius").intValue();
            this.strikeDurationTicks = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "strike-duration-ticks").longValue();
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
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
        player.playSound(player.getLocation(), sound, 2.0F, 1.0F);
        // Schedule the lightning strikes
        active = true;
        new BukkitRunnable() {
            private int boltsLeft = boltCount;

            @Override
            public void run() {
                if (boltsLeft > 0) {
                    Location randomLocation = LocationUtils.getRandomLocationInRadius(player.getLocation(), strikeRadius, false, false);
                    if (randomLocation != null) {
                        player.getWorld().strikeLightning(randomLocation);
                    }
                    boltsLeft--;
                } else {
                    active = false;
                    this.cancel(); // Stop the runnable
                }
            }
        }.runTaskTimer(plugin, 0, strikeDurationTicks / boltCount); // Spread the strikes across the duration
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (active && event.getEntity().getUniqueId() == ring.getOwner() && event.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) event.setCancelled(true);
    }
}
