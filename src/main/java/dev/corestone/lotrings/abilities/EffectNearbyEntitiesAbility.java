package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.Utilities.RingKeys;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class EffectNearbyEntitiesAbility extends AbilitySuper {

    private double range;

    private int potionPower;
    private int potionDurationTicks;
    private ArrayList<PotionEffectType> potionEffectTypes = new ArrayList<>();
    private CooldownManager cooldownManager;
    private Sound sound;

    public EffectNearbyEntitiesAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try{
            this.range = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "range").doubleValue();
            this.potionDurationTicks = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "duration-ticks").intValue();
            this.potionPower = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "power").intValue();
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            for(String str : (ArrayList<String>)plugin.getAbilityDataManager().getAbilityData(abilityName, "effects")){
                potionEffectTypes.add(PotionEffectType.getByName(str.toUpperCase()));
            }
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound"));
        }catch (Exception e){
            sendLoadError();
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event){
        if(!event.getAction().isRightClick())return;
        if(!abilityCanBeUsed(event.getPlayer().getUniqueId()))return;
        if(cooldownManager.checkAndStartCooldown())return;
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), sound, RingKeys.noiseLevel, 1);
        for(LivingEntity entity : event.getPlayer().getLocation().getNearbyLivingEntities(range)){
            if(!entity.getUniqueId().equals(event.getPlayer().getUniqueId())){
                for(PotionEffectType potionEffectType : potionEffectTypes){
                    entity.addPotionEffect(new PotionEffect(potionEffectType, potionDurationTicks, potionPower));
                }
            }
        }
    }
}
