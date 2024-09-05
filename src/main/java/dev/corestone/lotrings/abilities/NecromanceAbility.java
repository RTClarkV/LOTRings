package dev.corestone.lotrings.abilities;

import dev.corestone.lotrings.LOTRings;
import dev.corestone.lotrings.Ring;
import dev.corestone.lotrings.RingState;
import dev.corestone.lotrings.abilities.abilityutil.CooldownManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NecromanceAbility extends AbilitySuper {

    private CooldownManager cooldownManager;
    private int summons;
    private String summonType;
    private double summonHP;
    private int summonLength;
    private ArrayList<LivingEntity> necroSummons = new ArrayList<>();

    public NecromanceAbility(LOTRings plugin, Ring ring, String abilityName) {
        super(plugin, ring, abilityName);
        try {
            this.summonType = plugin.getAbilityDataManager().getAbilityStringData(abilityName, "summon-type").toUpperCase();
            this.summonLength = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "summon-length").intValue();
            this.summonHP = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "summon-hp").doubleValue();
            this.summons = plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "summon-count").intValue();
            this.cooldownManager = new CooldownManager(plugin, this, plugin.getAbilityDataManager().getAbilityFloatData(abilityName, "cooldown-seconds").doubleValue());
        } catch (Exception e) {
            sendLoadError();
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (!abilityCanBeUsed(event.getPlayer().getUniqueId())) return;
        if (cooldownManager.checkAndStartCooldown()) return;
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        player.getWorld().playSound(player, Sound.ENTITY_WARDEN_EMERGE, 1 ,1);
        for (int i = 0; i < summons; i++) {
            LivingEntity summon = (LivingEntity) playerLocation.getWorld().spawnEntity(playerLocation, EntityType.valueOf(summonType));
            summon.setMaxHealth(summonHP);
            summon.setHealth(summonHP);
            necroSummons.add(summon);

            if(summon instanceof Monster){
                ((Monster) summon).setTarget(null);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    summon.remove();
                    clearNecroEntities();
                    player.getWorld().playSound(player, Sound.ENTITY_WARDEN_DIG,1,1);
                }
            }.runTaskLater(plugin, summonLength);
        }
        cooldownManager.startCooldown();
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity){
            Player player = ((Player) event.getDamager());
            LivingEntity damagedPlayer = (LivingEntity) event.getEntity();

            if (damagedPlayer instanceof Player) {
                if (damagedPlayer.getUniqueId().equals(ring.getOwner())) return;
            }
            for(LivingEntity e : necroSummons){
                if(e instanceof Monster){
                    ((Monster) e).setTarget(damagedPlayer);
                }
            }
        }
    }

    @EventHandler
    public void onAgro(EntityTargetLivingEntityEvent event){
        if(necroSummons.contains(event.getEntity())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onModDeath(EntityDeathEvent event) {
        if (necroSummons.contains(event.getEntity())){
            event.setDroppedExp(0);
            event.getDrops().clear();
        }
    }

    public void clearNecroEntities() {
        necroSummons.clear();
    }
}