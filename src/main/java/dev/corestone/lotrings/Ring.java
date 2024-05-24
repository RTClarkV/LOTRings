package dev.corestone.lotrings;


import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.RingKeys;
import dev.corestone.lotrings.abilities.Ability;
import dev.corestone.lotrings.abilities.AbilityType;
import dev.corestone.lotrings.ringmanagers.RingStateManager;
import dev.corestone.lotrings.ringmanagers.SlotManager;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class Ring {
    private LOTRings plugin;
    private ArrayList<Ability> abilities;
    private ArrayList<Ability> slotAbilities;
    private UUID owner;
    private UUID ringID;
    private ItemStack item;
    private RingState ringState;

    private Ability activeSlotAbility;
    private String ringName;
    private SlotManager slotManager;


    public Ring(LOTRings plugin, ItemStack item, UUID owner){

        this.plugin = plugin;
        this.owner = owner;
        this.item = item;
        this.abilities = abilities;
        for(Ability ability : abilities){
            if(ability.getAbilityType().isToggled()){
                slotAbilities.add(ability);
            }
        }
        this.slotManager = new SlotManager(plugin, this, abilities);
        this.ringName = item.getItemMeta().getPersistentDataContainer().get(RingKeys.ringNameKey, PersistentDataType.STRING);
        this.ringID = UUID.fromString(item.getItemMeta().getPersistentDataContainer().get(RingKeys.ringIDKey, PersistentDataType.STRING));
        updateRing();
    }
    public void updateRing(){
        ItemMeta itemMeta = item.getItemMeta();
        for(String str : plugin.getRingDataManager().getRingLore(ringName)){
            itemMeta.getLore().add(Colorize.format(str));
        }
        itemMeta.setDisplayName(Colorize.format(plugin.getRingDataManager().getRingDisplayName(ringName)));
        item.setItemMeta(itemMeta);
    }

    public void switchRingState(RingState ringState){
        this.ringState = ringState;
        switch (ringState){
            case HELD:

            case INVENTORY:

            case LOST:
        }
    }
    public UUID getOwner(){
        return owner;
    }
    public RingState getRingState(){
        return ringState;
    }
    public String getRingName(){
        return ringName;
    }


}
