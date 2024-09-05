package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ResistDamageAbility extends AbilitySuper {

    private final Map<DamageCause, Double> damageResistance = new HashMap<>();

    public ResistDamageAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);

        // Load resistance values from the config
        ConfigurationSection configSection = plugin.getAbilityDataManager().getAbilityData(abilityName, "damage-resistance");
        if (configSection == null) {
            sendLoadError();
            return;
        }

        for (String key : configSection.getKeys(false)) {
            try {
                DamageCause cause = DamageCause.valueOf(key.toUpperCase());
                double resistance = configSection.getDouble(key);
                damageResistance.put(cause, resistance);
            } catch (IllegalArgumentException e) {
                sendLoadError();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!player.getUniqueId().equals(ring.getOwner())) return;
        if (!ring.isHeld()) return;
        DamageCause cause = event.getCause();
        if (damageResistance.containsKey(cause)) {
            player.setFallDistance(0.0f);
            double resistance = damageResistance.get(cause);
            double newDamage = event.getDamage() * (1 - resistance);
            event.setDamage(newDamage);
        }
    }
}
