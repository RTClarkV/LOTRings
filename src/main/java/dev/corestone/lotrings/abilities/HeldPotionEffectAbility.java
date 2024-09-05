package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class HeldPotionEffectAbility extends AbilitySuper {

    private PotionEffectType potionEffectType;
    private int power;
    private BukkitScheduler scheduler;

    public HeldPotionEffectAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        scheduler = plugin.getServer().getScheduler();
        try{
            this.potionEffectType = PotionEffectType.getByName(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "potion-effect"));
            power = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "power").intValue();
        }catch (Exception e){
            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.abilityLoadError(abilityName, abilityType.toString())));
        }
        scheduler.runTaskTimer(plugin, (task)->{
            if(ring.getState() == RingState.LOST){
                task.cancel();
                return;
            }
            if(ring.isHeld()) Bukkit.getPlayer(ring.getOwner()).addPotionEffect(new PotionEffect(potionEffectType, 100000, power));
        }, 0L, 20L);

    }

    @Override
    public void switchState(RingState ringState){
        if(ringState == RingState.HELD) Bukkit.getPlayer(ring.getOwner()).addPotionEffect(new PotionEffect(potionEffectType, 100000, power));
        if(ringState != RingState.HELD) Bukkit.getPlayer(ring.getOwner()).removePotionEffect(potionEffectType);
    }
}
