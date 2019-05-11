package org.ultimatechat2.command.ultimatechat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.ultimatechat2.command.CommandHandler;
import org.ultimatechat2.command.CommandNode;
import org.ultimatechat2.configuration.ConfigurationContainer;
import org.ultimatechat2.util.function.PermissionFunction;

import java.util.Set;

public class UltimateChatCommand extends CommandHandler {

    private static final String COMMAND_NAME      = "ultimatechat";
    private static final String[] COMMAND_ALIASES = new String[] {"uc"};

    private static class CommandPermission extends PermissionAdapter<CommandSender> {

        private static final String PERMISSION_NAME = "ultimatechat.admin";
        private static final String ERROR_MESSAGE   = ChatColor.DARK_RED + "You do not have permission to perform this command!";
        private static final CommandPermission INSTANCE = new CommandPermission();

        private CommandPermission() {
            super(PERMISSION_NAME, ERROR_MESSAGE);
            if (INSTANCE != null)
                throw new IllegalStateException("Command Permission already was initialized");
        }

        @Override
        public void ifPresent(CommandSender sender, CommandHandler handler, String label, String[] args) {
            if (args.length == 0) {
                for (CommandNode node : handler.getNodes()) {
                    if (node.getNodeName().equalsIgnoreCase("help")) {
                        node.execute(sender, label, args);
                        break;
                    }
                }
            } else {
                for (CommandNode node : handler.getNodes()) {
                    if (node.getNodeName().equalsIgnoreCase(args[0])) {
                        node.execute(sender, label, args);
                        return;
                    }
                }
                sender.sendMessage(ChatColor.RED + "Argument is not valid!");
            }
        }

    }

    public UltimateChatCommand(ConfigurationContainer configurationContainer) {
        super(COMMAND_NAME, COMMAND_ALIASES);
        Set<CommandNode> nodeSet = getNodes();
        nodeSet.add(new HelpCommand());
        nodeSet.add(new ReloadCommand(configurationContainer));
        setNodes(nodeSet);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(getCommandName())
                || hasAlias(label)) {
            PermissionFunction.executeFunction(CommandPermission.INSTANCE, sender, new Object[] {this, label, args});
            return true;
        }
        return false;
    }
}
