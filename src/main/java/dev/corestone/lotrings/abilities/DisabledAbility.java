package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.abilityutil.AbilityType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public class DisabledAbility implements Ability, Listener {

    private Ring ring;
    private LOTRings plugin;
    private String abilityName;
    private String displayName;
    private AbilityType abilityType = AbilityType.DISABLED;
    private UUID abilityID;
    public DisabledAbility(LOTRings plugin, Ring ring, String abilityName){
        this.plugin = plugin;
        this.ring = ring;
        this.abilityID = UUID.randomUUID();
        this.displayName = plugin.getAbilityDataManager().getAbilityData(abilityName, "display-name");
        this.abilityName = abilityName;
    }
    @Override
    public void switchState(RingState ringState) {
        switch (ringState){
            case HELD:
                break;
            case INVENTORY:
                break;
            case LOST:
                HandlerList.unregisterAll(this);
                break;
        };
    }

    @Override
    public String getName() {
        return abilityName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public UUID getID() {
        return abilityID;
    }

    @Override
    public AbilityType getAbilityType() {
        return abilityType;
    }

    @Override
    public void boot() {

    }
}
