package me.liuli.mlgrush;

import cn.nukkit.plugin.PluginBase;
import me.liuli.mlgrush.core.Config;
import me.liuli.mlgrush.listeners.BlockListener;
import me.liuli.mlgrush.listeners.PlayerListener;
import me.liuli.mlgrush.manager.ArenaManager;
import me.liuli.mlgrush.command.MLGRushCommand;
import me.liuli.mlgrush.utils.OtherUtil;

import java.io.File;
import java.io.IOException;

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

        if(!this.getServer().getPluginManager().getPlugins().containsKey("FastJSONLib")){
            //download plugin
            try {
                String pluginPath=this.getServer().getPluginPath();
                OtherUtil.downloadFile("https://github.com/liulihaocai/FJL/releases/download/1.0/FastJSONLib-1.0.jar",
                        pluginPath,"FastJSONLib-1.0.jar");
                //then load it
                this.getServer().getPluginManager()
                        .loadPlugin(new File(pluginPath,"FastJSONLib-1.0.jar").getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.getDataFolder().mkdirs();
        Config.load();
        ArenaManager.check();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getCommandMap().register("mlgrush",new MLGRushCommand());
    }
}
