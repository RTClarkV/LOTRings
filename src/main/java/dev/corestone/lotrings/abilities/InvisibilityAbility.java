package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class InvisibilityAbility extends AbilitySuper{
    private CooldownManager cooldownManager;
    private int duration;
    private BukkitScheduler scheduler;
    public InvisibilityAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        this.scheduler = plugin.getServer().getScheduler();
        try{
            this.duration = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "duration-seconds").intValue();
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
        }catch (Exception e){
            sendLoadError();
        }
    }

    @Override
    public void switchState(RingState ringState){
        super.switchState(ringState);
        if(ringState != RingState.HELD) Bukkit.getPlayer(ring.getOwner()).setInvisible(false);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (!abilityCanBeUsed(event.getPlayer().getUniqueId())) return;
        if (cooldownManager.checkAndStartCooldown()) return;
        event.getPlayer().setInvisible(true);
        scheduler.runTaskLater(plugin, (task)->{
            if(ring.isLost())task.cancel();
            Bukkit.getPlayer(ring.getOwner()).setInvisible(false);
        }, (long) duration*20);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))return;
        if(!event.getDamager().getUniqueId().equals(ring.getOwner()))return;
        if(Bukkit.getPlayer(ring.getOwner()).isInvisible()){
            event.setCancelled(true);
        }
    }
}
