package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import org.bukkit.event.Listener;

import java.util.UUID;

public class DisabledAbility implements Ability, Listener {

    private Ring ring;
    private LOTRings plugin;
    public DisabledAbility(LOTRings plugin, Ring ring){
        this.plugin = plugin;
        this.ring = ring;
    }
    @Override
    public void switchState() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isOnCooldown() {
        return false;
    }

    @Override
    public void setCooldown() {

    }

    @Override
    public int getCooldownTimeLeft() {
        return 0;
    }

    @Override
    public UUID getAbilityID() {
        return null;
    }

    @Override
    public AbilityType getAbilityType() {
        return null;
    }
}
