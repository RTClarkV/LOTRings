package dev.corestone.lotrings;


import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.abilities.Ability;
import dev.corestone.lotrings.abilities.AbilityType;
import dev.corestone.lotrings.ringmanagers.SlotManager;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class Ring {
    private LOTRings plugin;
    private ArrayList<Ability> abilities;
    private SlotManager slotManager;
    private UUID owner;
    private UUID ringID;
    private ItemStack item;

    private Ability activeSlotAbility;
    private String ringName;
    private ArrayList<String> ringLore;

    public Ring(LOTRings plugin, ItemStack item, UUID owner, ArrayList<String> abilities){
        this.plugin = plugin;
        this.owner = owner;
        updateRing();
    }
    public void updateRing(){
        //setRingLore();
    }
    public void setRingLore(ArrayList<String> lore){
        for(String string : lore){
            lore.remove(lore);
            lore.add(Colorize.format(string));
        }
        item.getItemMeta().setLore(lore);
    }


}
