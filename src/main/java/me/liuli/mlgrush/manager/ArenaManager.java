package me.liuli.mlgrush.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import me.liuli.mlgrush.MLGRush;
import me.liuli.mlgrush.core.Arena;
import me.liuli.mlgrush.core.Config;
import me.liuli.mlgrush.utils.OtherUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ArenaManager {
    public static void check() {
        File file=new File(MLGRush.plugin.getDataFolder().getPath()+"/map/");
        if(!file.exists()){
            MLGRush.plugin.getLogger().warning("Map Folder NOT Found!Please put rush map to "+MLGRush.plugin.getDataFolder().getPath()+"/map/");
            Server.getInstance().getPluginManager().disablePlugin(MLGRush.plugin);
        }
        if(!file.isDirectory()){
            MLGRush.plugin.getLogger().warning("Map Folder INCORRECT!Please check if map folder wrong!");
            Server.getInstance().getPluginManager().disablePlugin(MLGRush.plugin);
        }
    }
    public static ArrayList<Arena> arenas=new ArrayList<>();

    public static void createArena(Player red,Player blue){
        try {
            String levelName=getRandomLevelName();
            OtherUtil.copyDir(MLGRush.plugin.getDataFolder().getPath()+"/map/","./worlds/"+levelName+"/");
            Server.getInstance().loadLevel(levelName);
            Level level=Server.getInstance().getLevelByName(levelName);
            level.stopTime();
            level.setTime(Level.TIME_DAY);
            level.setAutoSave(false);

            Arena arena=new Arena(level,red,blue);
            PlayerManager.playerArena.put(blue.getName(),arena);
            PlayerManager.playerArena.put(red.getName(),arena);

            arenas.add(arena);
            arena.tpRedToSpawn();
            arena.tpBlueToSpawn();
            arena.giveItem();

            red.sendMessage("Game Started!");
            blue.sendMessage("Game Started!");
            red.sendMessage(Config.prefix+"You Are Fighting Against "+blue.getName());
            blue.sendMessage(Config.prefix+"You Are Fighting Against "+red.getName());
            arena.updateArena();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    arena.red.getInventory().removeItem(Config.block);
                    arena.level.dropItem(arena.red,Config.block);
                    arena.blue.getInventory().removeItem(Config.block);
                    arena.level.dropItem(arena.blue,Config.block);
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void removeArena(Arena arena){
        try{
            arenas.remove(arena);
            String levelName=arena.level.getFolderName();
            Server.getInstance().unloadLevel(arena.level);
            OtherUtil.delDir("./worlds/"+levelName+"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getRandomLevelName(){
        return UUID.randomUUID().toString().substring(0,10).replaceAll("-","").toUpperCase();
    }
}
