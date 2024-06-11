package dev.corestone.lotrings.ringmanagers;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.Ability;
import dev.corestone.lotrings.abilities.DisabledAbility;
import org.bukkit.Bukkit;

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
            }
        }
        return abilities;
    }
}
