package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public class DisabledAbility implements Ability, Listener {

    private Ring ring;
    private LOTRings plugin;
    private String abilityName;
    private AbilityType abilityType = AbilityType.DISABLED;
    private UUID abilityID;
    public DisabledAbility(LOTRings plugin, Ring ring, String abilityName){
        this.plugin = plugin;
        this.ring = ring;
        this.abilityID = UUID.randomUUID();
    }
    @Override
    public void switchState(RingState ringState) {

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
        return abilityID;
    }

    @Override
    public AbilityType getAbilityType() {
        return abilityType;
    }
}
