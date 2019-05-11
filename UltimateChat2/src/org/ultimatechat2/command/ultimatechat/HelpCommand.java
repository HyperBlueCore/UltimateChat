package org.ultimatechat2.command.ultimatechat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.ultimatechat2.command.CommandNode;

public class HelpCommand extends CommandNode {

    private static final String NODE_NAME = "help";

    HelpCommand() {
        super(NODE_NAME);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        sender.sendMessage(ChatColor.GRAY + " ----- " + ChatColor.GOLD + "UltimateChat2 Commands List" + ChatColor.GRAY + " ----- ");
        sender.sendMessage(ChatColor.GOLD + " /" + label + ChatColor.GRAY + " - " + ChatColor.GREEN + "Shows this list.");
        sender.sendMessage(ChatColor.GOLD + " /" + label + " help" + ChatColor.GRAY + " - Shows this list.");
        sender.sendMessage(ChatColor.GOLD + " /" + label + " reload" + ChatColor.GRAY + "  - Reloads config.");
    }
}
