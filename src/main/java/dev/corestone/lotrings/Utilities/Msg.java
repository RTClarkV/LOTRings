package dev.corestone.lotrings.Utilities;

public class Msg {
    public static String prefix = "&8&l[&6&lLOT&4&lRings&8&l] ";

    //loading failures
    public static String failedAttribute = "&cAn attribute failed to load. Please check if the ring attributes are correct!";


    //config key stuff
    public static String abilityDataConfigKey = "%ability-data%";

    public static String abilityLoadError(String abilityName, String abilityType){
        return prefix+"&cThe ability &f"+abilityName+" &c does not have all of the data for Ability Type &f" + abilityType + "&c. \n"+
                "&cPlease check abilities.yml for missing information regarding &f" + abilityName +"&c.";
    }

}
