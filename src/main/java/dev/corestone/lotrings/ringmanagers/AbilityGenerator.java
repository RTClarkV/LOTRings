package dev.corestone.lotrings.ringmanagers;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.*;

import java.util.ArrayList;
import java.util.List;

public class AbilityGenerator {
    private LOTRings plugin;
    public AbilityGenerator(LOTRings plugin){
        this.plugin = plugin;
    }

    public ArrayList<Ability> getAbilities(Ring ring){
        List<String> listAbil = plugin.getRingDataManager().getAbilities(ring.getRingName());
        ArrayList<Ability> abilities = new ArrayList<>();

        for(String strAb : listAbil){
            switch (plugin.getAbilityDataManager().getAbilityType(strAb)){
                case DISABLED:
                    abilities.add(new DisabledAbility(plugin, ring, strAb));
                    break;
                case FIREBALL:
                    abilities.add(new FireBallAbility(plugin, ring, strAb));
                    break;
                case HELD_POTION_EFFECT:
                    abilities.add(new HeldPotionEffectAbility(plugin, ring, strAb));
                    break;
                case FIRE_ASPECT:
                    abilities.add(new FireAspectAbility(plugin, ring, strAb));
                    break;
                case POTION_TOGGLE_EFFECT:
                    abilities.add(new PotionToggledEffect(plugin, ring, strAb));
                    break;
                case PUSH_BACK:
                    abilities.add(new PushBackAbility(plugin, ring, strAb));
                    break;
                case BOOST:
                    abilities.add(new BoostAbility(plugin, ring, strAb));
                    break;
                case MAX_HEALTH:
                    abilities.add(new MaxHealthAbility(plugin, ring, strAb));
                    break;
                case EFFECT_NEARBY_ENTITIES:
                    abilities.add(new EffectNearbyEntitiesAbility(plugin, ring, strAb));
                    break;
                case INVISIBILITY:
                    abilities.add(new InvisibilityAbility(plugin, ring, strAb));
                    break;
            }
        }
        return abilities;
    }
}
