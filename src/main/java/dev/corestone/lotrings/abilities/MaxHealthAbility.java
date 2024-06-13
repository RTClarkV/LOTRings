package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import org.bukkit.Bukkit;

public class MaxHealthAbility extends AbilitySuper{

    private double maxHealth;
    public MaxHealthAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try{
            this.maxHealth = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "max-health").doubleValue();
        }catch (Exception e){
            sendLoadError();
        }

    }
    @Override
    public void switchState(RingState ringState){
        super.switchState(ringState);
        if(ringState != RingState.HELD){
            Bukkit.getPlayer(ring.getOwner()).resetMaxHealth();
        }
        if(ringState == RingState.HELD) Bukkit.getPlayer(ring.getOwner()).setMaxHealth(maxHealth);
    }
}
