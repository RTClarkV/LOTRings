package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Location;
import org.bukkit.Particle;
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

public class GuardianAbility extends AbilitySuper{
    private double damage;
    private CooldownManager cooldownManager;
    private Sound sound;
    private Particle particle;
    private double dense;

    public GuardianAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.damage = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "damage");
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
            this.particle = Particle.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "particle").toUpperCase());
            this.dense = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "density");
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
        if (player.getTargetEntity(30) instanceof LivingEntity){
            RayTraceResult target = player.rayTraceBlocks(30);
            RayTraceResult targetEntity = player.rayTraceEntities(30);
            Location loc1 = player.getEyeLocation();
            Location loc2;
            Entity hitEntity;
            if (targetEntity.getHitEntity() != null) {
                hitEntity = targetEntity.getHitEntity();
                loc2 = targetEntity.getHitEntity().getLocation();
            } else if(target.getHitBlock() != null) {
                loc2 = target.getHitBlock().getLocation();
            } else {
                return;
            }
            
            createParticleLine(loc1, loc2, particle, dense);
            world.playSound(player.getLocation(), sound, 10, 1);
        }

    }

    public void createParticleLine(Location start, Location end, Particle particle, double density) {
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double distance = start.distance(end);
        double step = 1.0 / density;
        int steps = (int) (distance * density);

        for (int i = 0; i <= steps; i++) {
            Location currentLocation = start.clone().add(direction.clone().multiply(i * step));
            start.getWorld().spawnParticle(particle, currentLocation, 0, 0, 0, 0, 0);
        }
    }
}
