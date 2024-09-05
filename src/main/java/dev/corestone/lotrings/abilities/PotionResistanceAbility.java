package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class PotionResistanceAbility extends AbilitySuper {

    private ArrayList<PotionEffectType> immuneEffects = new ArrayList<>();
    private BukkitScheduler scheduler;

    public PotionResistanceAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        scheduler = plugin.getServer().getScheduler();
        try {
            for (String effectName : (ArrayList<String>) plugin.getAbilityDataManager().getAbilityData(abilityName, "immune-effects")) {
                immuneEffects.add(PotionEffectType.getByName(effectName.toUpperCase()));
            }
        } catch (Exception e) {
            sendLoadError();
        }
        scheduler.runTaskTimer(plugin, (task) -> {
            if (ring.getState() == RingState.LOST) {
                task.cancel();
                return;
            }
            if (ring.isHeld()) {
                Bukkit.getPlayer(ring.getOwner()).getActivePotionEffects().forEach(effect -> {
                    if (immuneEffects.contains(effect.getType())) {
                        Bukkit.getPlayer(ring.getOwner()).removePotionEffect(effect.getType());
                    }
                });
            }
        }, 0L, 20L);
    }

    @Override
    public void switchState(RingState ringState) {
        if (ringState == RingState.HELD) {
            if (Bukkit.getPlayer(ring.getOwner()).getActivePotionEffects() == null) return;
            Bukkit.getPlayer(ring.getOwner()).getActivePotionEffects().forEach(effect -> {
                if (immuneEffects.contains(effect.getType())) {
                    Bukkit.getPlayer(ring.getOwner()).removePotionEffect(effect.getType());
                }
            });
        }
    }
}
