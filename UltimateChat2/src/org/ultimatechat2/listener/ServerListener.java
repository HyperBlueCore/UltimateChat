package org.ultimatechat2.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.ultimatechat2.core.UltimateChatCore;
import org.ultimatechat2.protocol.LibraryProtocol;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListener implements Listener {

    private final UltimateChatCore core;
    private final Logger logger;

    public ServerListener(UltimateChatCore core, Logger logger) {
        if (core == null)
            throw new NullPointerException("Core cannot be null");
        if (logger == null)
            throw new NullPointerException("Logger cannot be null");

        this.core = core;
        this.logger = logger;
    }

    @EventHandler
    public void onPlugin(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals(LibraryProtocol.VAULT_NAME)) {
            core.getPluginService().updateServices();
            logger.log(Level.INFO, "Services Updated");
        }
    }

}
