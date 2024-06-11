package dev.corestone.lotrings.ringmanagers;

import dev.corestone.lotrings.LOTRings;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager{
    private LOTRings plugin;
    private double cooldown;
    private double cooldownLeft = 0;
    private BukkitScheduler scheduler;
    private boolean isTaskRunning = false;

    public CooldownManager(LOTRings plugin, double cooldown){
        this.plugin = plugin;
        this.cooldown = cooldown;
        this.scheduler = plugin.getServer().getScheduler();
    }
    public void startCooldown(){
        if(cooldownLeft <= 0)return;
        this.cooldownLeft = cooldown;
        run();
    }
    public void setCooldownLeft(double cooldownLeft){
        this.cooldownLeft = cooldownLeft;
    }
    public boolean isOnCooldown(){
        return cooldown < 0;
    }
    public double getCooldownLeftInt(){
        return (int)cooldownLeft;
    }
    public double getCooldown(){
        return cooldown;
    }
    public void changeCooldown(double cooldown){
        this.cooldown = cooldown;
    }
    public void stopTask(){
        isTaskRunning = false;
    }

    public void run() {
        if(isTaskRunning)return;
        isTaskRunning = true;
        scheduler.runTaskTimer(plugin, (task) ->{
            if(!isTaskRunning) task.cancel();
            if(cooldownLeft > 0){
                cooldownLeft -= 0.25;
            }
            //Bukkit.broadcastMessage("cooldown left: " + cooldownLeft);
            if(cooldownLeft <= 0 ){
                //Bukkit.broadcastMessage("Exact Time: " + ((System.currentTimeMillis() - exTime)/1000));
                stopTask();
                task.cancel();
            }
        }, 5l, 5L);

    }
}
