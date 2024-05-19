package dev.corestone.lotrings.data;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.data.dataessentials.DataFile;
import dev.corestone.lotrings.data.dataessentials.DataManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RingDataManager implements DataFile {
    private LOTRings plugin;
    private String fileName;
    private DataManager data;
    private HashMap<String, ArrayList<String>> ringLore = new HashMap<>();
    private HashMap<String, String> ringDisplayNames = new HashMap<>();
    private HashMap<String, List<String>> ringAbilities = new HashMap<>();

    public RingDataManager(LOTRings plugin, String fileName){
        this.fileName = fileName;
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, fileName);
        update(data.getInternalConfig());
        load();
    }
    public void load(){
        for(String ringName : getConfig().getConfigurationSection("rings").getKeys(false)){
            for(String ringDetail : getConfig().getConfigurationSection("rings."+ringName).getKeys(false)){
                String path = "rings."+ringName+"."+ringDetail;
                if(ringDetail.equals("name")){
                    ringDisplayNames.put(ringDetail, getConfig().getString(path));
                }
                if(ringDetail.equals("abilities")){
                    ringAbilities.put(ringName, getConfig().getStringList(path));
                    plugin.getServer().getConsoleSender().sendMessage(ringAbilities.toString());
                }
                if(ringDetail.equals("lore")){
                    ArrayList<String> loreEdit = new ArrayList<>();
                    //plugin.getAbilityDataManager().get
                }
            }
        }
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
        //then needs to loop through to see if it does not have a new added item.
    }
}
