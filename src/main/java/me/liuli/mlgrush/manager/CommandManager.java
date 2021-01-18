package me.liuli.mlgrush.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import me.liuli.mlgrush.core.Config;

public class CommandManager extends Command {
    private String help="MLGRush Plugin By Liuli!";
    public CommandManager() {
        super("mlgrush", "MLGRush Plugin By Liuli!");
    }
    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(!sender.isPlayer()){
            sender.sendMessage(help);
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(help);
            return false;
        }
        Player player=Server.getInstance().getPlayer(sender.getName());
        switch (args[0]){
            case "join":{
                PlayerManager.addPlayer(player);
                break;
            }
            case "leave":{
                PlayerManager.removePlayer(player);
                break;
            }
        }
        return false;
    }
}

