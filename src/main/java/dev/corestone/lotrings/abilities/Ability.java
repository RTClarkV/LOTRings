package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.abilityutil.AbilityType;

import java.util.UUID;

public interface Ability {
    void switchState(RingState ringState);
    String getName();
    String getDisplayName();
    UUID getID();

    AbilityType getAbilityType();
    void boot();

}
