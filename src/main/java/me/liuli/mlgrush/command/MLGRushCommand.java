package me.liuli.mlgrush.command;

import cn.cookiestudio.easy4form.window.BFormWindowSimple;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import me.liuli.mlgrush.core.Config;
import me.liuli.mlgrush.manager.PlayerManager;

public class MLGRushCommand extends Command {
    private String help="MLGRush Plugin By Liuli!";
    public MLGRushCommand() {
        super("mlgrush", "MLGRush Plugin By Liuli!");
    }
    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender){
            sender.sendMessage(help);
            return true;
        }
        BFormWindowSimple bFormWindowSimple = new BFormWindowSimple("MLGRush","");
        Player player = (Player)sender;
        bFormWindowSimple.addButton(new ElementButton("开始MLGRush匹配"));
        bFormWindowSimple.addButton(new ElementButton("退出匹配"));
        bFormWindowSimple.setResponseAction((e) -> {
            FormResponseSimple responseSimple = (FormResponseSimple) e.getResponse();
            switch (responseSimple.getClickedButtonId()){
                case 0:
                    PlayerManager.addPlayer(player);
                    break;
                case 1:
                    PlayerManager.removePlayer(player);
                    break;
            }
        });
        bFormWindowSimple.sendToPlayer(player);
        return false;
    }
}

