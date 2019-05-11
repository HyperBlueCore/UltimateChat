package org.ultimatechat2.command;

import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public abstract class CommandNode {

    private final String nodeName;
    private final Set<CommandNode> nodes;

    public CommandNode(String nodeName) {
        if (nodeName == null)
            throw new NullPointerException("Node Name cannot be null");

        this.nodeName = nodeName;
        this.nodes = new HashSet<>();
    }

    public String getNodeName() {
        return nodeName;
    }

    protected Set<CommandNode> getNodes() {
        return nodes;
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

}
