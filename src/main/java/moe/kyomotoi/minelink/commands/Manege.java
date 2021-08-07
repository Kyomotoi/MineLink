package moe.kyomotoi.minelink.commands;

import moe.kyomotoi.minelink.utils.ConfigDealer;
import moe.kyomotoi.minelink.MineLink;
import moe.kyomotoi.minelink.client.Http;
import moe.kyomotoi.minelink.utils.ErrorDealer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Manege extends Command {

    public Manege(String cmdName, String permission) {
        super(cmdName, permission);
    }

    private void ManegeHelper(CommandSender sender) {
        String cmd = ConfigDealer.getString("commands.manege");
        String playerCmd = ConfigDealer.getString("commands.player");

        sender.sendMessage("§7--===· §6§lMineLink Admin Menu §7·===--");
        sender.sendMessage("> §e/"+cmd+" test §f(Testing)");
        sender.sendMessage("> §e/"+cmd+" accept disable §f(Disable received)");
        sender.sendMessage("> §e/"+cmd+" accpet enabled §f(Enabled received)");
        sender.sendMessage("> §e/"+cmd+" send disable §f(Disable player send message)");
        sender.sendMessage("> §e/"+cmd+" send enabled §f(Enabled player send message)");
        sender.sendMessage("> §eFor player: /"+playerCmd+" {message} §f(Sending message to outside)");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            ManegeHelper(sender);

        } else if (args[0].equalsIgnoreCase("test")) {
            try {
                Http.sendMessage("Server", "This message is from server~ For testing!");
            } catch (Exception err) {
                sender.sendMessage("§cTesting failed...");
                ErrorDealer.paste(err);
            }

        } else if (args[0].equalsIgnoreCase("accept")) {
            if (args[1].equalsIgnoreCase("disable")) {
                MineLink.isEnabledAcceptMsg = false;
                sender.sendMessage("§cThe HTTP received has been disable now.");

            } else if (args[1].equalsIgnoreCase("enabled")) {
                MineLink.isEnabledAcceptMsg = true;
                sender.sendMessage("§aThe HTTP received has been enabled now.");
            } else {
                sender.sendMessage("§4Please check your input!");
            }

        } else if (args[0].equalsIgnoreCase("send")) {
            if (args[1].equalsIgnoreCase("disable")) {
                MineLink.isEnabledSend = false;
                sender.sendMessage("§cNow player can't send message to outside.");

            } else if (args[1].equalsIgnoreCase("enabled")) {
                MineLink.isEnabledSend = true;
                sender.sendMessage("§aNow player can send message to outside.");

            } else {
                sender.sendMessage("§4Please check your input!");
            }

        } else {
            sender.sendMessage("§4Please check your input!");
        }
    }
}
