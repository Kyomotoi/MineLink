package moe.kyomotoi.minelink.commands;

import moe.kyomotoi.minelink.utils.ConfigDealer;
import moe.kyomotoi.minelink.MineLink;
import moe.kyomotoi.minelink.client.Http;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Player extends Command {

    public Player(String cmdName, String permission) {
        super(cmdName, permission);
    }

    private void PlayerHelper(CommandSender sender) {
        String cmd = ConfigDealer.getString("commands.player");

        sender.sendMessage("§7--===· §6§lMineLink Player Menu §7·===--");
        sender.sendMessage("> §e/"+cmd+" {context} §f(Send message to outside)");
        sender.sendMessage("> §e/"+cmd+" disable §f(Refuse message from outside)");
        sender.sendMessage("> §e/"+cmd+" enabled §f(Accept message from outside)");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean isEnabled = MineLink.isEnabledSend;
        if (!isEnabled) {
            sender.sendMessage("§cHTTP(send message) service has been disable.");
        }

        String playerName = sender.getName();

        if (args.length == 0) {
            PlayerHelper(sender);

        } else if (args[0].equalsIgnoreCase("disable")) {
            MineLink.disabledPlayer.add(playerName);
            sender.sendMessage("§aYou will not receive message now.");

        } else if (args[0].equalsIgnoreCase("enabled")) {
            try {
                MineLink.disabledPlayer.remove(playerName);
                sender.sendMessage("§aYou will receive message now.");
            } catch (Exception err) {
                sender.sendMessage("§cWARNING! Failed to disable, maybe you does not disable.");
            }

        } else {
            String msg = args[0];

            try {
                Http.sendMessage(playerName, msg);
                sender.sendMessage("§aSend message success!");
            } catch (Exception err) {
                sender.sendMessage("§cFailed to send, maybe HTTP(send message) service has been disable.");
            }
        }
    }
}
