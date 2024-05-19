package dev.corestone.lotrings.data;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.abilities.AbilityType;
import dev.corestone.lotrings.data.dataessentials.DataFile;
import dev.corestone.lotrings.data.dataessentials.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class AbilityDataManager implements DataFile {
    private LOTRings plugin;
    private DataManager data;
    private ArrayList<String> abilities = new ArrayList<>();
    private HashMap<String, AbilityType> abilityTypes = new HashMap<>();

    private HashMap<String, ArrayList<HashMap<String, ?>>> abilityData = new HashMap<>();

    public AbilityDataManager(LOTRings plugin, String fileName){
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, fileName);
        update(data.getInternalConfig());
        load();
    }
    public void load(){
        for(String string : data.getConfig().getConfigurationSection("abilities").getKeys(false)){
            ArrayList<HashMap<String, ?>> dataMap = new ArrayList<>();
            abilities.add(string);
            String abilityPath = "abilities."+string;
            dataMap.add(addAbilityCash("ability-type", AbilityType.valueOf(getConfig().getString(abilityPath+".ability-type").toUpperCase())));
            for(String restOfDataKey : getConfig().getConfigurationSection(abilityPath).getKeys(false)){
                if(!restOfDataKey.equals("ability-type")) dataMap.add(addAbilityCash(restOfDataKey, getConfig().get(abilityPath+"."+restOfDataKey)));
            }
            abilityData.put(string, dataMap);
        }
        plugin.getServer().getConsoleSender().sendMessage(abilityData.toString());
    }
    public <T> HashMap<String, ?> addAbilityCash(String index, T thing){
        HashMap<String, T> innerMap = new HashMap<>();
        innerMap.put(index, thing);
        return innerMap;
    }


    public ArrayList<String> getAbilities(){
        return abilities;
    }
    public AbilityType getAbilityType(String ability){
        return abilityTypes.get(ability);
    }
    public ArrayList<HashMap<String, ?>> getAbilityData(String abilityName){
        return abilityData.get(abilityName);
    }
    @Override
    public YamlConfiguration getConfig() {
        return data.getConfig();
    }

    @Override
    public void set(String path, Object object) {
        data.set(path, object);
    }

    @Override
    public void remove(String path) {
        data.remove(path);
    }

    @Override
    public void update(YamlConfiguration internalYamlConfig) {
        set("version", internalYamlConfig.get("version"));
    }
}
