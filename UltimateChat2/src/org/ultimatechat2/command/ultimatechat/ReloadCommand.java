package org.ultimatechat2.command.ultimatechat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.ultimatechat2.command.CommandNode;
import org.ultimatechat2.configuration.ConfigurationContainer;

public class ReloadCommand extends CommandNode {

    private static final String NODE_NAME = "reload";

    private final ConfigurationContainer configurationContainer;

    ReloadCommand(ConfigurationContainer configurationContainer) {
        super(NODE_NAME);
        if (configurationContainer == null)
            throw new NullPointerException("Configuration Container cannot be null");

        this.configurationContainer = configurationContainer;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        configurationContainer.reloadConfiguration();
        sender.sendMessage(ChatColor.GOLD + "[UltimateChat] " + ChatColor.GREEN + "Config reloaded!");
    }

}
