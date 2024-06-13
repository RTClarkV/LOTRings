package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class LevitationAbility extends AbilitySuper implements Listener {


    public LevitationAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
