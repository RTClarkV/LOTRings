package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

public class BonemealAbility extends AbilitySuper {

    private Sound sound;

    public BonemealAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.sound = Sound.valueOf(plugin.getAbilityDataManager().getAbilityStringData(abilityName, "sound").toUpperCase());
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @Override
    public void switchState(RingState ringState) {
        if (ringState == RingState.LOST) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().isRightClick()) return;
        if (!abilityCanBeUsed(player.getUniqueId())) return;

        Block targetBlock = event.getClickedBlock();

        if (targetBlock == null) return;


        boolean success = targetBlock.applyBoneMeal(event.getBlockFace());
        if (success) {
            player.getWorld().playSound(targetBlock.getLocation(), sound, 1.0F, 1.0F);
        }
    }
}
