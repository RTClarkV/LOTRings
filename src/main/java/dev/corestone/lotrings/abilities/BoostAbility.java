package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.AbilitySuper;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class BoostAbility extends AbilitySuper {

    private double power;
    private CooldownManager cooldownManager;
    private Sound sound;
    public BoostAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.power = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "magnitude").doubleValue();
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
//            Bukkit.getPlayer(ring.getOwner()).setAllowFlight(true);
//            Bukkit.getPlayer(ring.getOwner()).setFlying(false);
            Bukkit.getPlayer(ring.getOwner()).setFlyingFallDamage(TriState.TRUE);
        }catch (Exception e){
            sendLoadError();
            Bukkit.getPlayer(ring.getOwner()).setAllowFlight(false);
            Bukkit.getPlayer(ring.getOwner()).setFlying(false);
        }
    }
    @Override
    public void switchState(RingState ringState){
        if(ringState == RingState.LOST){
            Bukkit.getPlayer(ring.getOwner()).setAllowFlight(false);
            Bukkit.getPlayer(ring.getOwner()).setFlying(false);
            HandlerList.unregisterAll(this);
        }
        if(ringState == RingState.INVENTORY){
            Bukkit.getPlayer(ring.getOwner()).setAllowFlight(false);
            Bukkit.getPlayer(ring.getOwner()).setFlying(false);
        }
        if(ringState == RingState.HELD){
            Bukkit.getPlayer(ring.getOwner()).setAllowFlight(true);
            Bukkit.getPlayer(ring.getOwner()).setFlying(false);
            Bukkit.getPlayer(ring.getOwner()).setFlyingFallDamage(TriState.TRUE);
        }
    }

    @EventHandler
    public void doubleJump(PlayerToggleFlightEvent event){
        if(!event.getPlayer().hasPermission("lotr.admin"))event.setCancelled(true);
        if(event.getPlayer().getUniqueId().equals(ring.getOwner()))event.setCancelled(true);
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
        if(cooldownManager.checkAndStartCooldown())return;
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(power));
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), sound, 10, 1);
    }
}
