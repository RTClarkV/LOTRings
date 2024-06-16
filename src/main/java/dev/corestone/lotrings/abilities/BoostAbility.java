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
import org.bukkit.event.player.PlayerInteractEvent;
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
        }catch (Exception e){
            sendLoadError();
        }
    }
    @Override
    public void switchState(RingState ringState){
        if(ringState == RingState.LOST){
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event){
        if(!event.getAction().isRightClick())return;
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
        if(cooldownManager.checkAndStartCooldown())return;
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(power));
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), sound, 10, 1);
    }
}
