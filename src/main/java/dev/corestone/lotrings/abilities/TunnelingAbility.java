package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;



public class TunnelingAbility extends AbilitySuper {
    private CooldownManager cooldownManager;

    public TunnelingAbility(LOTRings plugin, Ring ring, String abilityName){
        super(plugin, ring, abilityName);

        try {
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @EventHandler
    public void onuUse(PlayerInteractEvent event){
        // Do stuff here
    }
}
