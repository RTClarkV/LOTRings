package dev.corestone.lotrings;


import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import dev.corestone.lotrings.Utilities.RingKeys;
import dev.corestone.lotrings.abilities.Ability;
import dev.corestone.lotrings.ringmanagers.RingStateManager;
import dev.corestone.lotrings.ringmanagers.SlotManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Ring {
    private LOTRings plugin;
    private ArrayList<Ability> abilities;
    private ArrayList<Ability> slotAbilities;
    private UUID owner;
    private UUID ringID;
    private ItemStack item = null;
    private RingState ringState = RingState.INVENTORY;

    private String ringName;
    private SlotManager slotManager;
    private RingStateManager ringStateManager;
    private BukkitScheduler scheduler;


    public Ring(LOTRings plugin, ItemStack item, UUID owner){
        this.plugin = plugin;
        this.owner = owner;
        load(item, owner);
        //this.scheduler = plugin.getServer().getScheduler();
//        scheduler.runTaskTimer(plugin, ()->{
//            Bukkit.broadcastMessage(ringName + ": " + ringState);
//        }, 0L, 20L);

    }
    public void load(ItemStack item, UUID owner){
        Bukkit.broadcastMessage("reloading");
        this.ringName = item.getItemMeta().getPersistentDataContainer().get(RingKeys.ringNameKey, PersistentDataType.STRING);
        this.ringID = UUID.fromString(item.getItemMeta().getPersistentDataContainer().get(RingKeys.ringIDKey, PersistentDataType.STRING));
        for(ItemStack itemStack : Bukkit.getPlayer(owner).getInventory()){
            if(itemStack != null){
                if(!itemStack.getType().isAir()
                        && itemStack.getItemMeta() != null
                        && itemStack.getItemMeta().getPersistentDataContainer().has(RingKeys.ringIDKey)
                        && UUID.fromString(Objects.requireNonNull(itemStack.getItemMeta().getPersistentDataContainer().get(RingKeys.ringIDKey, PersistentDataType.STRING))).equals(ringID)
                        && itemStack.getType().equals(item.getType())){
                    this.item = itemStack;
                }
            }
        }
        if(item.getType().isAir()){
            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.failedRingLoad));
            return;
        }
        this.abilities = plugin.getAbilities(this);
        this.slotManager = new SlotManager(plugin, this, abilities);
        updateRing();
        switchRingState(RingState.INVENTORY);
        this.ringStateManager = new RingStateManager(plugin, this);
    }
    public void updateRing(){
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Colorize.format(plugin.getRingDataManager().getRingDisplayName(ringName)));
        itemMeta.setLore(plugin.getRingDataManager().getRingLore(ringName));
        item.setItemMeta(itemMeta);
    }

    public void switchRingState(RingState ringState){
        this.ringState = ringState;
        Bukkit.broadcastMessage("Item " + ringName + " state: " + ringState);
        for(Ability ability : abilities){
            ability.switchState(ringState);
        }
        switch (ringState){
            case HELD:

            case INVENTORY:

            case LOST:
                slotManager.delete();

                //scheduler.cancelTasks(plugin);
        }
    }
    public UUID getOwner(){
        return owner;
    }
    public void setOwner(UUID uuid){
        this.owner = uuid;
    }
    public UUID getUUID(){
        return ringID;
    }
    public RingState getRingState(){
        return ringState;
    }
    public String getRingName(){
        return ringName;
    }
    public UUID getActiveAbilityUUID(){
        return slotManager.getActiveAbility();
    }
    public ItemStack getItem(){
        return item;
    }


}
