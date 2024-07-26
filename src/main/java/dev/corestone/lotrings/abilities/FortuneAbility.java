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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneAbility extends AbilitySuper {

    private float fortuneLevel;

    public FortuneAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.fortuneLevel = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "fortune-level");
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @Override
    public void switchState(RingState ringState) {
        super.switchState(ringState);
        if (ringState != RingState.HELD) {
            HandlerList.unregisterAll(this);
        } else if (ringState == RingState.HELD) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!ring.isHeld()) return;
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        if (!abilityCanBeUsed(player.getUniqueId())) return;

        ItemStack tool = new ItemStack(Material.DIAMOND_PICKAXE);
        tool.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, (int) fortuneLevel);

        event.setDropItems(false);
    }
}
