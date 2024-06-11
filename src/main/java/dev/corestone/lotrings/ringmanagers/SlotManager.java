package dev.corestone.lotrings.ringmanagers;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.Ability;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.UUID;

public class SlotManager implements Listener {
    private LOTRings plugin;
    private Ring manager;
    private ArrayList<UUID> abilities = new ArrayList<>();
    private int slot = 0;
    private int maxSlots;

    public SlotManager(LOTRings plugin, Ring manager,ArrayList<Ability> abilities){
        this.manager = manager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        for(Ability ability : abilities){
            if(ability.getAbilityType().isToggled()){
                this.abilities.add(ability.getAbilityID());
            }
        }
        this.maxSlots = this.abilities.size()-1;
    }
    public void nextSlot(){
        slot += 1;
        if(slot > maxSlots){
            slot = 0;
        }
    }
    public UUID getActiveAbility(){
        return abilities.get(slot);
    }
    @EventHandler
    public void onShiftLeftClick(PlayerInteractEvent event){
        if(!event.getPlayer().getUniqueId().equals(manager.getOwner()))return;
        if(!event.getAction().isLeftClick())return;
        if(manager.getRingState() != RingState.HELD)return;
        if(!event.getPlayer().isSneaking())return;
        nextSlot();
    }
    public void delete(){
        HandlerList.unregisterAll(this);
    }
}
