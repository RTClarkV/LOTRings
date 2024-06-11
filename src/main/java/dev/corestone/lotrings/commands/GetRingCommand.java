package dev.corestone.lotrings.commands;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import dev.corestone.lotrings.Utilities.RingKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetRingCommand implements CommandExecutor, TabCompleter {

    private LOTRings plugin;

    public GetRingCommand(LOTRings plugin){
        this.plugin = plugin;
        plugin.getCommand("lotrgetring").setExecutor(this);
        plugin.getCommand("lotrgetring").setTabCompleter(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player))return true;
        Player player = (Player) sender;
        ArrayList<String> argsList = new ArrayList<>();
        for(int j = 0; args.length > j; j++){
            argsList.add(args[j]);
        }
        if(argsList.isEmpty()){
            player.sendMessage(Colorize.format(Msg.noArgs));
            return true;
        }
        String ringName = args[0];
        if(!plugin.getRingDataManager().getRingNames().contains(ringName)){
            player.sendMessage(Colorize.format(Msg.invalidRingName));
            return true;
        }

        ItemStack itemStack = new ItemStack(plugin.getRingDataManager().getRingMaterial(ringName));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(RingKeys.ringIDKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        itemMeta.getPersistentDataContainer().set(RingKeys.ringNameKey, PersistentDataType.STRING, ringName);
        itemStack.setItemMeta(itemMeta);
        player.getInventory().addItem(itemStack);
        plugin.addNewRing(itemStack, player.getUniqueId());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return plugin.getRingDataManager().getRingNames();
    }
}
