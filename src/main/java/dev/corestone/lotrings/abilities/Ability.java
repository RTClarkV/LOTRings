package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.RingState;

import java.util.UUID;

public interface Ability {
    void switchState(RingState ringState);
    String getName();
    boolean isOnCooldown();
    void setCooldown();
    int getCooldownTimeLeft();
    UUID getAbilityID();

    AbilityType getAbilityType();

}
