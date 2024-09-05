package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;



public class HealOthersAbility extends AbilitySuper {

    private CooldownManager cooldownManager;
    private double healAmount;
    private Sound healSound;
    private int range;

    public HealOthersAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.healAmount = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "heal-amount").doubleValue();
            this.healSound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
            this.range = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "range").intValue();
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
        RayTraceResult rayTraceResult = event.getPlayer().rayTraceEntities(range);
        if (rayTraceResult == null) return;
        if (rayTraceResult.getHitEntity() == null) return;
        if (!(rayTraceResult.getHitEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) rayTraceResult.getHitEntity();
        if (entity.getMaxHealth() < entity.getHealth() + healAmount) {
            healAmount = entity.getMaxHealth() - entity.getHealth();
        }
        entity.setHealth(entity.getHealth() + healAmount);
        event.getPlayer().getWorld().playSound(event.getPlayer(), healSound, 10, 1);
    }
}
