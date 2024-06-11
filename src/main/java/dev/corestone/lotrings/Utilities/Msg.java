package dev.corestone.lotrings.Utilities;

public class Msg {
    public static String prefix = "&8&l[&6&lLOT&4&lRings&8&l] ";

    //command stuff
    public static String noArgs = prefix + "&cThis command is missing arguments. use /lotrhelp for help.";
    public static String help = prefix + "&c commands: \n"+
            "/lotrgetring <ringname>";
    public static String invalidRingName = prefix + "&cThat ring does not exist, please try again.";

    //loading failures
    public static String failedAttribute = "&cAn attribute failed to load. Please check if the ring attributes are correct!";
    public static String failedMaterial(String badMat, String ringName){
        return "&cA ring item failed to load with the ring &4&l" + ringName + "&c. The item " + badMat + " does not appear to be a valid material! \n " +
                "&cplease visit&b https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html &cfor help.";
    }
    public static String failedRingLoad = prefix + "&cA ring failed to load. Please contact the developer for help.";
    public static String usefulFailedRingLoad(String ringName){
        return prefix + "&cThe ring &b" + ringName + " &cfailed to load. Please make sure the config has the correct syntax.";
    }

    //config key stuff
    public static String abilityDataConfigKey = "%ability-data%";

    public static String abilityLoadError(String abilityName, String abilityType){
        return prefix+"&cThe ability &f"+abilityName+" &c does not have all of the data for Ability Type &f" + abilityType + "&c. \n"+
                "&cPlease check abilities.yml for missing information regarding &f" + abilityName +"&c.";
    }

}
