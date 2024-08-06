package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TunnelingAbility extends AbilitySuper {

    //private double range;
    //private CooldownManager cooldownManager;
    private boolean isBreaking;
    private BukkitScheduler scheduler;
    private Sound sound;
    private List<String> unbreakableBlocks;

    public TunnelingAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        this.scheduler = plugin.getServer().getScheduler();
        try {
            //this.range = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "range").doubleValue();
            //this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
            this.unbreakableBlocks = plugin.getAbilityDataManager().getAbilityStringListData(abilityName, "unbreakable-blocks");
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerBreakBlock(BlockBreakEvent event) {
        if(isBreaking)return;
        if(event.isCancelled())return;
        if (!abilityCanBeUsed(event.getPlayer().getUniqueId())) return;
        //if (cooldownManager.checkAndStartCooldown()) return;

        Player player = event.getPlayer();
        //RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), range);
        breakBlocksAround(player, event.getBlock());
        player.getWorld().playSound(player.getLocation(), sound, 10, 1);
//        if (rayTraceResult != null && rayTraceResult.getHitBlock() != null) {
//            Block targetBlock = rayTraceResult.getHitBlock();
//            breakBlocksAround(player, targetBlock);
//            player.getWorld().playSound(player.getLocation(), sound, 10, 1);
//        }
    }

    private void breakBlocksAround(Player player, Block center) {
        isBreaking = true;
        ArrayList<Block> blockList = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = center.getRelative(x, y, z);

                    // Check if the block is in the unbreakable blocks list
                    if (unbreakableBlocks.contains(block.getType().toString())) {
                        continue; // Skip unbreakable blocks
                    }

                    // Check if the block is bedrock
                    if (block.getType() == Material.BEDROCK || block.getType().equals(Material.AIR)) {
                        continue; // Skip bedrock
                    }

                    // Create and call a BlockBreakEvent to check if the block can be broken
                    //BlockBreakEvent breakEvent = new BlockBreakEvent(block, player);
                    //Bukkit.getPluginManager().callEvent(breakEvent);

                    // If the event is cancelled, skip breaking the block
//                    if (breakEvent.isCancelled()) {
//                        continue;
//                    }

                    blockList.add(block); //sending all commented code for fancy delay.

                    // Break the block if it's not air and passes the checks
//                    if (block.getType() != Material.AIR) {
//                        block.breakNaturally();
//                        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 10, block.getType().createBlockData());
//                    }
                }
            }
        }

//        for(int i = 0; i < blockList.size(); i++){
//            for(int j = 0; j < blockList.size(); j++){
//                if(blockList.get(i).getLocation().distance(player.getLocation())
//                        < blockList.get(j).getLocation().distance(player.getLocation())){
//                    Block closerBlock = blockList.get(i);
//                    Block fartherBlock = blockList.get(j);
//                    blockList.set(i, closerBlock);
//                    blockList.set(j, fartherBlock);
//                }
//            }
//        }

        stupidRecursiveFunction(blockList, player);

//        int tickSpacing = 0;
//        for(Block block : blockList){
//            breakBlockLater(block, tickSpacing*2);
//            tickSpacing += 1;
//            Bukkit.broadcastMessage(tickSpacing + " ff");
//        }
//        scheduler.runTaskLater(plugin, (task)->{
//            isBreaking = false;
//        }, tickSpacing* 2L + 1);
    }

    private void stupidRecursiveFunction(ArrayList<Block> blockList, Player player){
        if(blockList.isEmpty() || !ring.isHeld()){
            isBreaking = false;
            return;
        }
        Block closestBlock = blockList.get(0);
        Block compareBlock;
        for(Block block : blockList){
            compareBlock = block;
            if(closestBlock.getLocation().distance(player.getLocation()) > compareBlock.getLocation().distance(player.getLocation())){
                closestBlock = compareBlock;
            }
        }
        breakBlock(closestBlock, player);
        blockList.remove(closestBlock);

        scheduler.runTaskLater(plugin, (task) -> {
            stupidRecursiveFunction(blockList, player);
        }, 3);
    }

    private void breakBlock(Block block, Player player){

        // Create and call a BlockBreakEvent to check if the block can be broken
        //BlockBreakEvent breakEvent = new BlockBreakEvent(block, player);
        //Bukkit.getPluginManager().callEvent(breakEvent);
        player.breakBlock(block);
        // If the event is cancelled, skip breaking the block
        //if (breakEvent.isCancelled()) return;
        //block.breakNaturally();
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 5 ,1);
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 10, block.getType().createBlockData());
    }
}
