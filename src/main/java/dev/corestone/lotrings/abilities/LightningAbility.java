package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

public class LightningAbility extends AbilitySuper {

    private double range;
    private CooldownManager cooldownManager;
    private Sound sound;

    public LightningAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.range = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "range").doubleValue();
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
        RayTraceResult rayTraceResult = player.rayTraceBlocks(range);
        RayTraceResult rayTraceResult2 = player.rayTraceEntities((int) range);
        if (rayTraceResult2 != null) {
            LivingEntity targetEntity = (LivingEntity) rayTraceResult2.getHitEntity();
            targetEntity.getWorld().strikeLightningEffect(targetEntity.getLocation());
            targetEntity.damage(6);
            player.getWorld().playSound(player.getLocation(), sound, 10, 1);

            return;
        }

        if (rayTraceResult != null) {
            Block targetBlock = rayTraceResult.getHitBlock();
            targetBlock.getWorld().strikeLightning(targetBlock.getLocation());
            player.getWorld().playSound(player.getLocation(), sound, 10, 1);
        }
    }
}
