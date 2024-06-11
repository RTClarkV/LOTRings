package dev.corestone.lotrings;


import dev.corestone.lotrings.Utilities.RingKeys;
import dev.corestone.lotrings.abilities.Ability;
import dev.corestone.lotrings.commands.GetRingCommand;
import dev.corestone.lotrings.data.AbilityDataManager;
import dev.corestone.lotrings.data.RingDataManager;
import dev.corestone.lotrings.ringmanagers.AbilityGenerator;
import dev.corestone.lotrings.ringmanagers.NewRingTracker;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class LOTRings extends JavaPlugin {
    private AbilityDataManager abilityDataManager;
    private RingDataManager ringDataManager;
    private AbilityGenerator abilityGenerator;
    private GetRingCommand getRingCommand;
    private NewRingTracker newRingTracker;
    private ArrayList<Ring> ringList = new ArrayList<>();
    private BukkitScheduler scheduler;

    @Override
    public void onEnable() {
        this.scheduler = getServer().getScheduler();
        // Plugin startup logic
        RingKeys.ringNameKey = new NamespacedKey(this, "LOTRing-ring-name");
        RingKeys.ringIDKey = new NamespacedKey(this, "LOTRings-ringID");
        this.abilityDataManager = new AbilityDataManager(this, "abilities.yml");
        this.ringDataManager = new RingDataManager(this, "rings.yml");
        this.abilityGenerator = new AbilityGenerator(this);
        this.newRingTracker = new NewRingTracker(this);
        //commands
        this.getRingCommand = new GetRingCommand(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void reload(){
        for(Ring ring : ringList){
            ring.switchRingState(RingState.LOST);
        }
        this.abilityDataManager = null;
        this.ringDataManager = null;

        this.abilityDataManager = new AbilityDataManager(this, "abilities.yml");
        this.ringDataManager = new RingDataManager(this, "rings.yml");
        //delete all the ring objects.
        //reset the configs.
        //check online players for rings.
    }
    public ArrayList<Ring> getRings(){
        return ringList;
    }
    public ArrayList<UUID> getRingIDs(){
        ArrayList<UUID> ringIDs = new ArrayList<>();
        for(Ring ring : ringList){
            ringIDs.add(ring.getUUID());
        }
        return ringIDs;
    }


    public AbilityDataManager getAbilityDataManager(){
        return abilityDataManager;
    }
    public RingDataManager getRingDataManager(){return ringDataManager;}
    public ArrayList<Ability> getAbilities(Ring ring){
        return abilityGenerator.getAbilities(ring);
    }
    public boolean verifyRingItem(ItemStack itemStack){
        if(itemStack.getItemMeta() == null)return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(RingKeys.ringIDKey))return false;
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(RingKeys.ringNameKey))return false;
        if(!ringDataManager.getRingNames().contains(itemStack.getItemMeta().getPersistentDataContainer().get(RingKeys.ringNameKey, PersistentDataType.STRING)))return false;
        return true;
    }
    public boolean checkIfNewRing(ItemStack itemStack){
        if(!verifyRingItem(itemStack))return false;
        if(getRingIDs().isEmpty())return true;
//        if(getRingIDs() == null)return true;
        return !getRingIDs().contains(UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(RingKeys.ringIDKey, PersistentDataType.STRING))));
    }
    public boolean checkInvForRing(Inventory inventory){
        for(ItemStack itemStack : inventory){
            if(itemStack != null){
                if(verifyRingItem(itemStack)){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isPlayerHoldingRing(Player player){
        for(Ring ring : ringList){
            if(ring.getRingState() == RingState.HELD && ring.getOwner() == player.getUniqueId()){
                return true;
            }
        }
        return false;
    }
//    public boolean isPlayerHoldingThisRing(Player player, Ring ring){
//        for(Ring r : ringList){
//            if(ring.getRingState() == RingState.HELD && r.getOwner() == player.getUniqueId()){
//                return true;
//            }
//        }
//        return false;
//    }
    public UUID getRingItemID(ItemStack itemStack){
        if(!verifyRingItem(itemStack))return null;
        return UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(RingKeys.ringIDKey, PersistentDataType.STRING)));
    }

    public void addNewRing(ItemStack item, UUID owner){
        if(!verifyRingItem(item))return;
        //if(!checkIfNewRing(item)) return;
        if(getRingIDs().contains(getRingItemID(item))) {
            for(Ring ring : ringList){
                if(ring.getUUID().equals(getRingItemID(item)) && ring.getRingState().equals(RingState.LOST))
                    ring.load(item ,owner);
            }
            return;
        }
        //Bukkit.broadcastMessage("making new ring!");
        Ring ring = new Ring(this, item, owner);
        ringList.add(ring);
    }
    public void deleteRing(UUID uuid){
//        Bukkit.broadcastMessage("deleting: " + ringList.toString());
//        Ring ringToDelete = null;
//        for(Ring ring : ringList){
//            if(ring.getUUID().equals(uuid)) ringToDelete = ring;
//        }
//        ringList.remove(ringToDelete);
//        Bukkit.broadcastMessage("After: " + ringList.toString());
    }
}
