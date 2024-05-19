package dev.corestone.lotrings.data.dataessentials;

import dev.corestone.lotrings.LOTRings;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DataManager {
    private File file;
    private YamlConfiguration config;
    private LOTRings plugin;
    DataFile manager;
    private String fileName;
    private InputStreamReader internalConfigFileStream;
    private YamlConfiguration internalYamlConfig;

    public DataManager(LOTRings pluign, DataFile manager, String fileName){
        this.plugin = pluign;
        this.manager = manager;
        this.fileName = fileName;

        this.internalConfigFileStream = new InputStreamReader(plugin.getResource(fileName), StandardCharsets.UTF_8);
        this.internalYamlConfig = YamlConfiguration.loadConfiguration(internalConfigFileStream);

        load();
    }
    private void load(){
        file = new File(plugin.getDataFolder(), fileName);

        if(!file.exists()){
            plugin.saveResource(fileName, false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        }catch (Exception e){
            e.printStackTrace();
        }
//        if(file.exists()){
//            update();
//        }
    }
    public YamlConfiguration getInternalConfig(){
        return internalYamlConfig;
    }
    public void save(){
        try {
            config.save(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void set(String path, Object object){
        config.set(path, object);
        save();
    }
    public void remove(String path){
        config.set(path, null);
        save();
    }
    public YamlConfiguration getConfig(){
        return config;
    }
    public String getFileName(){
        return fileName;
    }
}
