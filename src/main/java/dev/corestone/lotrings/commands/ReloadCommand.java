package dev.corestone.lotrings.commands;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Utilities.Colorize;
import dev.corestone.lotrings.Utilities.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private LOTRings plugin;

    public ReloadCommand(LOTRings plugin){
        this.plugin = plugin;
        plugin.getCommand("lotrreload").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        double timeMS = System.currentTimeMillis();
        commandSender.sendMessage(Colorize.format(Msg.prefix + "&cReloading rings..."));
        plugin.reload();
        commandSender.sendMessage(Colorize.format(Msg.prefix + "&cReload complete, time " + (System.currentTimeMillis()-timeMS+"ms.")));
        return true;
    }
}
