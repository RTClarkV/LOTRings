package dev.corestone.lotrings.abilities;

public enum AbilityType {
    DISABLED(true,true),
    HELD_POTION_EFFECT(false, true),
    FIREBALL(true, true),
    PUSH_BACK(true, true),
    LEVITATION(false, true);
    private boolean isToggled;
    private boolean onlyActiveWhenHeld;
    private AbilityType(boolean isToggled, boolean onlyActiveWhenHeld){
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
