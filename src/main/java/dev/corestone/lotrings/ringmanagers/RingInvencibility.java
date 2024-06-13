package dev.corestone.lotrings.ringmanagers;

import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
import dev.corestone.lotrings.LOTRings;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class RingInvencibility implements Listener {
    private LOTRings plugin;

    public RingInvencibility(LOTRings plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event){
        for(ItemStack itemStack : event.getInventory()){
            if(itemStack != null && plugin.verifyRingItem(itemStack)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void despawn(ItemDespawnEvent event){
        if(!plugin.verifyRingItem(event.getEntity().getItemStack()))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void Anvil(PrepareAnvilEvent event){
        for(ItemStack item : event.getInventory()){
            if(item !=null && plugin.verifyRingItem(item)){
                event.setResult(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void itemHurt(EntityDamageEvent event){
        if(!event.getEntity().getType().equals(EntityType.DROPPED_ITEM))return;
        Item item = (Item) event.getEntity();
        if(!plugin.verifyRingItem(item.getItemStack()))return;
        event.setCancelled(true);
    }
}
