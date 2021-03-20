package me.liuli.mlgrush.core;

import cn.cookiestudio.lobbysystem.PluginMain;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;
import me.liuli.mlgrush.manager.ArenaManager;
import me.liuli.mlgrush.manager.PlayerManager;

import java.util.ArrayList;

public class Arena {
    public Level level;
    public Player red,blue;
    public int redBed=0,blueBed=0;
    public ArrayList<Vector3> blockpos;
    public Scoreboard redScoreboard,blueScoreboard;

    public Arena(Level level,Player red,Player blue){
        this.level=level;
        this.red=red;
        this.blue=blue;
        this.blockpos=new ArrayList<>();
    }

    public void tpRedToSpawn(){
        red.teleport(Position.fromObject(Config.red,level),PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
    public void tpBlueToSpawn(){
        blue.teleport(Position.fromObject(Config.blue,level),PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
    public void giveItem(){
        givePlayerItem(red);
        givePlayerItem(blue);
    }
    public void updateArena(){
        if(redBed>=Config.beds){
            red.sendMessage(Config.prefix+"You lose!");
            blue.sendMessage(Config.prefix+"You win!");
            remove(true);
        }
        if(blueBed>=Config.beds){
            red.sendMessage(Config.prefix+"You win!");
            blue.sendMessage(Config.prefix+"You lose!");
            remove(true);
        }
        for(Vector3 vec3:blockpos){
            level.setBlock(vec3, Block.get(0));
        }
        blockpos.clear();
        updateRedSb();
        updateBlueSb();
    }
    public void remove(boolean tp){
        if(tp){
            red.teleport(PluginMain.getInstance().getLobby().getLobbyConfig().getLobbyPosition());
            blue.teleport(PluginMain.getInstance().getLobby().getLobbyConfig().getLobbyPosition());
            red.getInventory().clearAll();
            blue.getInventory().clearAll();
            redScoreboard.hideFor(red);
            blueScoreboard.hideFor(blue);
        }
        PlayerManager.playerArena.remove(blue.getName());
        PlayerManager.playerArena.remove(red.getName());
        ArenaManager.removeArena(this);
    }

    public static void givePlayerItem(Player player){
        player.getInventory().clearAll();
        player.getInventory().addItem(Config.kbStick,Config.block,Config.pickaxe);
    }
    public void updateRedSb(){
        if(redScoreboard!=null) redScoreboard.hideFor(red);
        redScoreboard = ScoreboardAPI.createScoreboard();
        ScoreboardDisplay rsbd = redScoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", Config.title);
        int count=1;
        for(String str:Config.scoreboard){
            String name=str.replaceAll("%against%",blue.getName()).replaceAll("%blue%",blueBed+"").replaceAll("%red%",redBed+"").replaceAll("%max%",Config.beds+"");
            rsbd.addLine(name,count);
            count++;
        }
        redScoreboard.showFor(red);
    }
    public void updateBlueSb(){
        if(blueScoreboard!=null) blueScoreboard.hideFor(blue);
        blueScoreboard = ScoreboardAPI.createScoreboard();
        ScoreboardDisplay bsbd = blueScoreboard.addDisplay(DisplaySlot.SIDEBAR, "dumy", Config.title);
        int count=1;
        for(String str:Config.scoreboard){
            String name=str.replaceAll("%against%",red.getName()).replaceAll("%blue%",blueBed+"").replaceAll("%red%",redBed+"").replaceAll("%max%",Config.beds+"");
            bsbd.addLine(name,count);
            count++;
        }
        blueScoreboard.showFor(blue);
    }
}
