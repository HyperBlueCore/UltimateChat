/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package org.ultimatechat2;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.ultimatechat2.Main;
import org.ultimatechat2.UpdateCheker;

public class onJoin
implements Listener {
    public UpdateCheker checker;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        this.checker = new UpdateCheker(Main.plugin);
        if ((p.hasPermission("ultimatechat.update") || p.isOp()) && this.checker.isConnected() && this.checker.hasUpdate()) {
            p.sendMessage((Object)ChatColor.GRAY + "=========================");
            p.sendMessage((Object)ChatColor.RED + "UltimateChat is outdated!");
            p.sendMessage((Object)ChatColor.GREEN + "Newest version: " + this.checker.getLatestVersion());
            p.sendMessage((Object)ChatColor.RED + "Your version: " + Main.plugin.getDescription().getVersion());
            p.sendMessage((Object)ChatColor.GRAY + "=========================");
        }
    }
}

