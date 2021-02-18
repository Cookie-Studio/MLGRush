package me.liuli.mlgrush.manager;

import cn.nukkit.Player;
import me.liuli.mlgrush.core.Arena;
import me.liuli.mlgrush.core.Config;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    public static Map<String, Arena> playerArena=new HashMap<>();
    public static Player queuedPlayer;

    public static void addPlayer(Player player){
        Arena arena=getArena(player);
        if(arena!=null||isQueuePlayer(player)){
            player.sendMessage(Config.prefix+"You are in a queue now.");
            return;
        }
        if(queuedPlayer==null){
            queuedPlayer=player;
        }else{
            ArenaManager.createArena(queuedPlayer,player);
            queuedPlayer=null;
        }
        player.sendMessage(Config.prefix+"Queued!Type /mlgrush leave to leave queue");
    }

    public static void removePlayer(Player player){
        if(isQueuePlayer(player)){
            queuedPlayer=null;
        }else {
            Arena arena = getArena(player);
            if (arena == null) {
                player.sendMessage(Config.prefix + "You not in queue!");
                return;
            }
            arena.remove(true);
        }
        player.sendMessage(Config.prefix+"You left queue!");
    }
    public static boolean isQueuePlayer(Player player){
        if(queuedPlayer==null){
            return false;
        }else{
            return queuedPlayer.equals(player);
        }
    }

    public static Arena getArena(Player player){
        return playerArena.get(player.getName());
    }
}
