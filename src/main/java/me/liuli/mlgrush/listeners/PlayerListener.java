package me.liuli.mlgrush.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import me.liuli.mlgrush.core.Arena;
import me.liuli.mlgrush.manager.PlayerManager;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        Player player=((Player) event.getEntity());
        Arena arena=PlayerManager.getArena(player);
        if(arena==null)return;
        if(event.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
            if(player.getName().equals(arena.blue.getName())){
                arena.tpBlueToSpawn();
            }else {
                arena.tpRedToSpawn();
            }
            event.setCancelled();
        }else{
            event.setDamage(-1);
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event){
        Arena arena=PlayerManager.getArena(event.getPlayer());
        if(arena==null)return;
        if(event.getAction().equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)&&event.getBlock().getId()==Block.BED_BLOCK){
            event.setCancelled();
        }
        event.getPlayer().getFoodData().setLevel(20);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event){
        if(PlayerManager.isQueuePlayer(event.getPlayer())){
            PlayerManager.queuedPlayer=null;
        }
        Arena arena=PlayerManager.getArena(event.getPlayer());
        if(arena==null)return;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Position spawn=Server.getInstance().getDefaultLevel().getSafeSpawn();
                if(event.getPlayer().getName().equals(arena.blue.getName())){
                    arena.red.teleport(spawn);
                    arena.red.getInventory().clearAll();
                }else{
                    arena.blue.teleport(spawn);
                    arena.blue.getInventory().clearAll();
                }
                arena.remove(false);
            }
        },1000);
    }
}
