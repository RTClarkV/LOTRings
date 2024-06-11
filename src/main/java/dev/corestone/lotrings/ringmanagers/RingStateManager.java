package dev.corestone.lotrings.ringmanagers;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.StringReader;
import java.util.Objects;


public class RingStateManager implements Listener {
    private LOTRings plugin;
    private Ring ring;

    public RingStateManager(LOTRings plugin, Ring ring){
        this.plugin = plugin;
        this.ring = ring;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        checkIfHeld(Objects.requireNonNull(Bukkit.getPlayer(ring.getOwner())));
    }
    public void delete(){
        HandlerList.unregisterAll(this);
        ring.switchRingState(RingState.LOST);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(event.getPlayer().getUniqueId() != ring.getOwner())return;
        delete();
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getUniqueId() != ring.getOwner())return;
        checkItemAndDelete(event.getItemDrop().getItemStack());
    }


    @EventHandler
    public void inventoryClose(InventoryCloseEvent event){
        if(event.getPlayer().getUniqueId() != ring.getOwner())return;
        checkInvAndDelete(event.getPlayer().getInventory());
        checkIfHeld((Player) event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event){
        if(event.getPlayer().getUniqueId() != ring.getOwner())return;
        delete();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(event.getPlayer().getUniqueId() != ring.getOwner())return;
        delete();
    }

    //for inventory state stuff
    @EventHandler
    public void onSwitchSLot(PlayerItemHeldEvent event){
        if(event.getPlayer().getUniqueId() != ring.getOwner())return;
        checkIfHeld(event.getPlayer());
//        ItemStack newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
//        ItemStack oldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
//        if(newItem != null && plugin.verifyRingItem(newItem) && Objects.equals(plugin.getRingItemID(newItem), ring.getUUID()) && ring.getRingState() == RingState.INVENTORY){
//            if(plugin.isPlayerHoldingRing(event.getPlayer()))return;
//            ring.switchRingState(RingState.HELD);
//        }
//        if(oldItem != null && plugin.verifyRingItem(oldItem) && Objects.equals(plugin.getRingItemID(oldItem), ring.getUUID()) && ring.getRingState() == RingState.HELD){
//            ring.switchRingState(RingState.INVENTORY);
//        }
    }
    public void checkInvAndDelete(Inventory inventory){
        boolean hasRing = false;
        for(ItemStack itemStack : inventory){
            if(itemStack == null) continue;
            if(!plugin.verifyRingItem(itemStack))continue;
            if(plugin.getRingItemID(itemStack).equals(ring.getUUID())){
                hasRing = true;
            }
        }
        if(!hasRing){
            delete();
        }
    }
    public void checkItemAndDelete(ItemStack itemStack){
        if(!plugin.verifyRingItem(itemStack))return;
        if(Objects.equals(plugin.getRingItemID(itemStack), ring.getUUID())){
            delete();
        }
    }
    public void checkIfHeld(Player player){
        if(ring.getRingState() == RingState.LOST)return;
        if(checkItem(player.getInventory().getItemInOffHand())){
            switch (ring.getRingState()){
                case HELD:
                    return;
                case INVENTORY:
                    ring.switchRingState(RingState.HELD);
                    return;
            }
        }
        if(checkItem(player.getInventory().getItemInMainHand())){
            switch (ring.getRingState()){
                case HELD:
                    return;
                case INVENTORY:
                    ring.switchRingState(RingState.HELD);
                    return;
            }
        }
        if(!checkItem(player.getInventory().getItemInMainHand()) && !checkItem(player.getInventory().getItemInMainHand())){
            switch (ring.getRingState()){
                case HELD:
                    ring.switchRingState(RingState.INVENTORY);
                case INVENTORY:
                    return;
            }
        }
    }
    public boolean checkOffHand(Player player){
        if(ring.getRingState() == RingState.LOST)return true;
        if(!plugin.verifyRingItem(player.getInventory().getItemInOffHand())
                || !plugin.getRingItemID(player.getInventory().getItemInOffHand()).equals(ring.getUUID())
                && ring.getRingState() == RingState.HELD){
            ring.switchRingState(RingState.INVENTORY);
            return false;
        }
        if(!plugin.verifyRingItem(player.getInventory().getItemInOffHand()))return false;
        if(player.getInventory().getItemInOffHand().getType() == Material.AIR)return false;
        if(plugin.getRingItemID(player.getInventory().getItemInOffHand()).equals(ring.getUUID())){
            if(ring.getRingState() == RingState.INVENTORY && plugin.isPlayerHoldingRing(player)){
                ring.switchRingState(RingState.HELD);
                return true;
            }
            if(ring.getRingState() == RingState.HELD && plugin.isPlayerHoldingRing(player))return true;
            return false;
        }
        return false;
    }
    public void checkMainHand(Player player){
        if(ring.getRingState() == RingState.LOST)return;
        if(!plugin.verifyRingItem(player.getInventory().getItemInMainHand())
                || !plugin.getRingItemID(player.getInventory().getItemInMainHand()).equals(ring.getUUID()) && ring.getRingState() == RingState.HELD){
            ring.switchRingState(RingState.INVENTORY);
            return;
        }
        if(!plugin.verifyRingItem(player.getInventory().getItemInMainHand()))return;
        if(player.getInventory().getItemInMainHand().getType() == Material.AIR)return;
        if(!plugin.isPlayerHoldingRing(player) && ring.getRingState() == RingState.INVENTORY && plugin.getRingItemID(player.getInventory().getItemInMainHand()).equals(ring.getUUID())){
            ring.switchRingState(RingState.HELD);
        }
    }

    public boolean checkItem(ItemStack itemStack){
        return plugin.verifyRingItem(itemStack) && plugin.getRingItemID(itemStack).equals(ring.getUUID());
    }


}
