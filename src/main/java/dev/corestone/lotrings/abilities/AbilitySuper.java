package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import dev.corestone.lotrings.abilities.abilityutil.AbilityType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.logging.Handler;

public class AbilitySuper implements Ability, Listener {
    String abilityName;
    String abilityDisplayName;
    UUID uuid;
    LOTRings plugin;
    Ring ring;
    AbilityType abilityType;
    public AbilitySuper(LOTRings plugin, Ring ring, String abilityName){
        this.plugin = plugin;
        this.ring = ring;
        this.abilityName = abilityName;
        this.uuid = UUID.randomUUID();
        this.abilityDisplayName = plugin.getAbilityDataManager().getAbilityData(abilityName, "display-name");
        this.abilityType = plugin.getAbilityDataManager().getAbilityType(abilityName);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public boolean abilityCanBeUsed(UUID playerID){
        if(abilityType.isToggled() && abilityType.isOnlyActiveWhenHeld())return playerID.equals(ring.getOwner()) && ring.getActiveAbility().getID().equals(uuid) && ring.isHeld();
        if(abilityType.isOnlyActiveWhenHeld()) return playerID.equals(ring.getOwner()) && ring.isHeld();
        if(abilityType.isToggled()) return playerID.equals(ring.getOwner()) && ring.getActiveAbility().getID().equals(uuid);
        return false;
    }
    public void sendLoadError(){
        plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.abilityLoadError(abilityName, abilityType.toString())));
    }
    public Ring getRing(){
        return ring;
    }
    public LOTRings getPlugin(){
        return plugin;
    }

    @Override
    public void switchState(RingState ringState) {
        if(ringState == RingState.LOST) HandlerList.unregisterAll(this);
    }

    @Override
    public String getName() {
        return abilityName;
    }

    @Override
    public String getDisplayName() {
        return abilityDisplayName;
    }


    @Override
    public UUID getID() {
        return uuid;
    }

    @Override
    public AbilityType getAbilityType() {
        return abilityType;
    }

    @Override
    public void boot() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
