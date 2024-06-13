package dev.corestone.lotrings.ringmanagers;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.abilities.Ability;
import dev.corestone.lotrings.abilities.AbilitySuper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotManager implements Listener {
    private LOTRings plugin;
    private Ring manager;
    private List<Ability> abilities = new ArrayList<>();
    private int slot = 0;
    private int maxSlots;

    public SlotManager(LOTRings plugin, Ring manager, ArrayList<Ability> abilities){
        this.manager = manager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        Collections.reverse(abilities); //yea yea I have to reverse it again because of stupid lore list stuff

        for(Ability ability : abilities){
            if(ability.getAbilityType().isToggled()){
                this.abilities.add(ability);
            }
        }
        this.maxSlots = this.abilities.size()-1;
    }
    public void nextSlot(){
        slot += 1;
        if(slot > maxSlots){
            slot = 0;
        }
        Bukkit.getPlayer(manager.getOwner()).sendActionBar(Colorize.format(abilities.get(slot).getDisplayName()));
    }
    public Ability getActiveAbility(){
        return abilities.get(slot);
    }
    @EventHandler
    public void onShiftLeftClick(PlayerInteractEvent event){
        if(!event.getPlayer().getUniqueId().equals(manager.getOwner()))return;
        if(!event.getAction().isLeftClick())return;
        if(manager.getState() != RingState.HELD)return;
        if(!event.getPlayer().isSneaking())return;
        nextSlot();
    }
    @EventHandler
    public void onRun(PlayerJumpEvent event){
        //Bukkit.broadcastMessage("jump");
    }
    public void delete(){
        HandlerList.unregisterAll(this);
    }
}
