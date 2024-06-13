package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionToggledEffect extends AbilitySuper {

    private PotionEffectType potionEffectType;
    private CooldownManager cooldownManager;
    private int power;
    private int durationTicks;
    public PotionToggledEffect(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        //plugin.getServer().getPluginManager().registerEvents(this, plugin);
        try{
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            this.power = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "power").intValue();
            this.durationTicks = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "duration-ticks").intValue();
            this.potionEffectType = PotionEffectType.getByName(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "potion-effect").toUpperCase());
        }catch (Exception e){
            sendLoadError();
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if(!event.getAction().isRightClick())return;
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
        if(cooldownManager.isOnCooldown())return;
        event.getPlayer().addPotionEffect(new PotionEffect(potionEffectType, durationTicks, power));
        cooldownManager.startCooldown();
    }
}
