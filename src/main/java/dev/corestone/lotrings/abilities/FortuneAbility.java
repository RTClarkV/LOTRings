package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneAbility extends AbilitySuper implements Listener {

    private float fortuneLevel;

    public FortuneAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.fortuneLevel = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "fortune-level");
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!ring.isHeld()) return;
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        if (!abilityCanBeUsed(player.getUniqueId())) return;

        event.setCancelled(true);

        ItemStack tool = new ItemStack(Material.DIAMOND_PICKAXE);
        tool.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, (int) fortuneLevel);
        ItemStack usedTool = event.getPlayer().getInventory().getItemInMainHand();

        int durabilityDamage = 1;
        if (usedTool.containsEnchantment(Enchantment.DURABILITY)) {
            int unbreakingLevel = usedTool.getEnchantmentLevel(Enchantment.DURABILITY);
            double chance = 1.0 / (unbreakingLevel + 1);
            if (Math.random() > chance) {
                durabilityDamage = 0;
            }
        }

        event.getBlock().breakNaturally(tool);
        usedTool.setDurability((short) (usedTool.getDurability() + durabilityDamage));

        if (tool.getDurability() >= tool.getType().getMaxDurability()) {
            player.getInventory().remove(tool);
        }
    }


}