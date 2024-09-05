package dev.corestone.lotrings.abilities.abilityutil;

public enum AbilityType {
    DISABLED(true,true),
    HELD_POTION_EFFECT(false, true),
    POTION_TOGGLE_EFFECT(true, true),
    FIREBALL(true, true),
    FIRE_ASPECT(false, true),
    MAX_HEALTH(false, true),
    PUSH_BACK(true, true),
    EFFECT_NEARBY_ENTITIES(true, true),
    BOOST(true, true),
    LEVITATION(false, true),
    INVISIBILITY(true, true),
    TUNNELING(true, true),
    FORTUNE(false, true),
    LIGHTNING(true, true),
    RIPTIDE(true, true),
    BONEMEAL(true, true),
    RAINSTORM(true, true),
    NECROMANCY(true, true),
    FIREBALL_RAIN(true, true),
    SLAM(true, true),
    HEAL_OTHERS(true, true),
    RESIST_EFFECTS(false, true),
    RESIST_DAMAGE(false, true),
    IGNITE_PLAYERS(true, true),
    LIGHTNING_RAIN(true, true);


    private boolean isToggled;
    private boolean onlyActiveWhenHeld;
    AbilityType(boolean isToggled, boolean onlyActiveWhenHeld){
        this.isToggled = isToggled;
        this.onlyActiveWhenHeld = onlyActiveWhenHeld;
    }
    public boolean isToggled(){
        return isToggled;
    }
    public boolean isOnlyActiveWhenHeld(){
        return onlyActiveWhenHeld;
    }

}
