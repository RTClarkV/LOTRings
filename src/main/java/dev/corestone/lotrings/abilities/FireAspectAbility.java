package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class FireAspectAbility extends AbilitySuper implements Listener {

    private int durationTicks;
    public FireAspectAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        try{
            this.durationTicks = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "duration-ticks").intValue();
        }catch (Exception e){
            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.abilityLoadError(abilityName, abilityType.toString())));
        }
    }
    @Override
    public void switchState(RingState ringState){
        if(ringState == RingState.LOST) HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)return;
        if(event.getDamager().getType() != EntityType.PLAYER)return;
        if(!abilityCanBeUsed(event.getDamager().getUniqueId()))return;
        event.getEntity().setFireTicks(durationTicks);
    }

}
