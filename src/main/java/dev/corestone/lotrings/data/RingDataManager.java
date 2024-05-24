package dev.corestone.lotrings.data;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import dev.corestone.lotrings.Utilities.NiceTools;
import dev.corestone.lotrings.data.dataessentials.DataFile;
import dev.corestone.lotrings.data.dataessentials.DataManager;
import org.bukkit.attribute.Attribute;
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
    private HashMap<String, HashMap<Attribute, Integer>> ringAttributes = new HashMap<>();

    public RingDataManager(LOTRings plugin, String fileName){
        this.fileName = fileName;
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, fileName);
        update(data.getInternalConfig());
        load();
        updateLore();
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
                }
                if(ringDetail.equals("attributes")){
                    for(String attributeSTR : getConfig().getConfigurationSection(path).getKeys(false)){
                        try{
                            ringAttributes.put(ringName, NiceTools.castHashMap(Attribute.valueOf(attributeSTR), getConfig().getInt(path + "." + attributeSTR)));
                        }catch (Exception exception){
                            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix+Msg.failedAttribute));
                        }
                    }
                }
            }
        }
    }
    public void updateLore(){
        for(String ringName : getConfig().getConfigurationSection("rings").getKeys(false)) {
            String path = "rings."+ringName+".lore";
            List<String> loreEdit = getConfig().getStringList(path);
            for(int j = 0; j < loreEdit.size(); j++){
                if(loreEdit.get(j).equals(Msg.abilityDataConfigKey)){
                    loreEdit.remove(j);
                    for(String abilityName : ringAbilities.get(ringName)){
                        int jCount = 0;
                        for(String loreElement : (ArrayList<String>)plugin.getAbilityDataManager().getAbilityData(abilityName, "description")){
                            loreEdit.add(j + jCount, loreElement);
                            jCount+=1;
                        }
                    }
                }
            }
            ringLore.put(ringName, (ArrayList<String>) loreEdit);
        }
    }
    public ArrayList<Attribute> getAttributes(String ringName){
        return (ArrayList<Attribute>) ringAttributes.get(ringName).keySet();
    }
    public ArrayList<String> getRingLore(String ringName){
        return ringLore.get(ringName);
    }
    public List<String> getAbilities(String ringName){
        return ringAbilities.get(ringName);
    }
    public HashMap<Attribute, Integer> getAttributeHash(String ringName){
        return ringAttributes.get(ringName);
    }
    public String getRingDisplayName(String ringName){
        return ringDisplayNames.get(ringName);
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
