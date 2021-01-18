package me.liuli.mlgrush;

import cn.nukkit.plugin.PluginBase;
import me.liuli.mlgrush.core.Config;
import me.liuli.mlgrush.listeners.BlockListener;
import me.liuli.mlgrush.listeners.PlayerListener;
import me.liuli.mlgrush.manager.ArenaManager;
import me.liuli.mlgrush.manager.CommandManager;
import me.liuli.mlgrush.utils.OtherUtils;

import java.io.File;

public class MLGRush extends PluginBase {
    public static MLGRush plugin;
    public static String jarDir=MLGRush.class.getProtectionDomain().getCodeSource().getLocation().getFile();

    @Override
    public void onLoad(){
        getServer().getProperties().set("spawn-protection",0);
    }

    @Override
    public void onEnable() {
        plugin = this;

        if (!new File(getDataFolder().getPath()+"/fastjson.jar").exists()) {
            OtherUtils.readJar("fastjson.jar",jarDir,getDataFolder().getPath()+"/fastjson.jar");
        }
        OtherUtils.injectClass(new File(getDataFolder().getPath()+"/fastjson.jar"));
        getDataFolder().mkdirs();
        Config.load();
        ArenaManager.check();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getCommandMap().register("mlgrush",new CommandManager());
    }
}
