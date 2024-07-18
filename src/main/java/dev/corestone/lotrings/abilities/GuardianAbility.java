package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.AbilitySuper;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class GuardianAbility extends AbilitySuper {

    private double damage;
    private CooldownManager cooldownManager;
    private Sound sound;

    public GuardianAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.damage = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "damage");
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
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        RayTraceResult result = world.rayTraceEntities(eyeLocation, direction, 30);
        if (result != null && result.getHitEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) result.getHitEntity();
            target.damage(damage, player);
        }

        world.playSound(player.getLocation(), sound, 10, 1);
    }
}
