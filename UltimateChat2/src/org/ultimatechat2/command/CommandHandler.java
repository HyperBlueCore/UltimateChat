package org.ultimatechat2.command;

import org.bukkit.command.CommandExecutor;

import java.util.HashSet;
import java.util.Set;

public abstract class CommandHandler implements CommandExecutor {

    private final String commandName;
    private final String[] aliases;
    private final Set<CommandNode> nodes;

    public CommandHandler(String commandName, String[] aliases) {
        if (commandName == null)
            throw new NullPointerException("Command Name cannot be null");

        this.commandName = commandName;
        this.aliases = aliases != null
                ? aliases.clone()
                : new String[0];
        this.nodes = new HashSet<>();
    }

    public static String[] getArguments(String[] src, int startPoint) {
        if (src.length > 0 && startPoint > 0) {
            int newLength;
            String[] next = new String[(newLength = src.length - 1)];
            System.arraycopy(src, 1, next, 0, newLength);
            return getArguments(next, startPoint - 1);
        }
        return src.length > 0 && startPoint == 0 ? src : new String[0];
    }

    protected String getCommandName() {
        return commandName;
    }

    protected boolean hasAlias(String label) {
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(label))
                return true;
        }
        return false;
    }

    public Set<CommandNode> getNodes() {
        return new HashSet<>(nodes);
    }

    protected void setNodes(Set<CommandNode> nodes) {
        this.nodes.clear();
        if (nodes != null)
            this.nodes.addAll(nodes);
    }

}
