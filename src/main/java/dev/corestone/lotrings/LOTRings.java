package dev.corestone.lotrings;


import dev.corestone.lotrings.data.AbilityDataManager;
import dev.corestone.lotrings.data.RingDataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LOTRings extends JavaPlugin {
    private AbilityDataManager abilityDataManager;
    private RingDataManager ringDataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.abilityDataManager = new AbilityDataManager(this, "abilities.yml");
        this.ringDataManager = new RingDataManager(this, "rings.yml");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void reload(){
        //delete all the ring objects.
        //reset the configs.
        //re-add the rings for update.
    }

    public AbilityDataManager getAbilityDataManager(){
        return abilityDataManager;
    }
}
