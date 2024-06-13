package dev.corestone.lotrings.ringmanagers;

import dev.corestone.lotrings.LOTRings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;


public class NewRingTracker implements Listener {
    private LOTRings plugin;
    private BukkitScheduler scheduler;
    public NewRingTracker(LOTRings plugin){
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPickUp(PlayerAttemptPickupItemEvent event){
        scheduler.runTaskLater(plugin, ()->{
            scanPlayerForRing(event.getPlayer());
        }, 1L);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        plugin.removeDisconnectedList(event.getPlayer().getUniqueId());
        scanPlayerForRing(event.getPlayer());
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        scanPlayerForRing((Player) event.getPlayer());
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        scanPlayerForRing(event.getPlayer());
    }
    public void scanPlayerForRing(Player player){
        if(plugin.getDisconnectedList().contains(player.getUniqueId()))return;
        for(ItemStack item : player.getInventory()){
            if(item != null && plugin.verifyRingItem(item)){
                plugin.addNewRing(item, player.getUniqueId());
                //if(plugin.getRingIDs().contains())
            }
        }
    }
    public void scanPlayerItemForRing(ItemStack item, Player player){
        if(item.getItemMeta() == null)return;
        if(!plugin.verifyRingItem(item))return;
        if(plugin.checkIfNewRing(item)){
            plugin.addNewRing(item, player.getUniqueId());
        }
    }
}
