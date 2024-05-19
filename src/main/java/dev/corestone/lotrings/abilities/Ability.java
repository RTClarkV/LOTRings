package dev.corestone.lotrings.abilities;

import java.util.UUID;

public interface Ability {
    void switchState();
    String getName();
    boolean isOnCooldown();
    void setCooldown();
    int getCooldownTimeLeft();
    UUID getAbilityID();

    AbilityType getAbilityType();

}
