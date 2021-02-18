package me.liuli.mlgrush.core;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.Vector3;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.liuli.mlgrush.MLGRush;
import me.liuli.mlgrush.utils.OtherUtil;

import java.io.File;
import java.util.ArrayList;

public class Config {
    public static String prefix,title;
    public static int beds;
    public static Vector3 red,blue;
    public static String[] scoreboard;

    public static Item kbStick,pickaxe,block;

    public static void load(){
        if(!new File(MLGRush.plugin.getDataFolder().getPath()+"/config.yml").exists()){
            OtherUtil.readJar("config.yml", MLGRush.jarDir, MLGRush.plugin.getDataFolder().getPath()+"/config.yml");
        }
        JSONObject configJSON=JSONObject.parseObject(OtherUtil.y2j(new File(MLGRush.plugin.getDataFolder().getPath()+"/config.yml")));
        prefix=configJSON.getString("prefix");

        JSONObject arena=configJSON.getJSONObject("arena");
        beds=arena.getInteger("beds");
        red=new Vector3(arena.getJSONObject("red").getFloat("x"),arena.getJSONObject("red").getFloat("y"),arena.getJSONObject("red").getFloat("z"));
        blue=new Vector3(arena.getJSONObject("blue").getFloat("x"),arena.getJSONObject("blue").getFloat("y"),arena.getJSONObject("blue").getFloat("z"));

        title=configJSON.getString("title");
        JSONArray scoreboardJArr=configJSON.getJSONArray("scoreboard");
        ArrayList<String> scoreboardArr=new ArrayList<>();
        for(Object obj:scoreboardJArr){
            if(obj instanceof String){
                scoreboardArr.add((String) obj);
            }
        }
        scoreboard=scoreboardArr.toArray(new String[0]);

        kbStick=new Item(Item.BLAZE_ROD,0,1);
        kbStick.addEnchantment(Enchantment.get(Enchantment.ID_KNOCKBACK).setLevel(configJSON.getInteger("kblevel")));
        pickaxe=new Item(Item.DIAMOND_PICKAXE,0,1);
        block=new Item(Item.SANDSTONE,0,64);
    }
}
