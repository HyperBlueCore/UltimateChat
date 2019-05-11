package org.ultimatechat2.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ultimatechat2.core.UltimateChatCore;
import org.ultimatechat2.protocol.LibraryProtocol;
import org.ultimatechat2.protocol.factory.LibraryProtocolFactory;
import org.ultimatechat2.service.Service;
import org.ultimatechat2.updater.UpdateResult;
import org.ultimatechat2.util.ConfigurationValue;

import static org.bukkit.Bukkit.getServer;

public class PlayerListener implements Listener {

    private final UltimateChatCore core;

    public PlayerListener(UltimateChatCore core) {
        if (core == null)
            throw new NullPointerException("Core cannot be null");

        this.core = core;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        String version;
        if (player.hasPermission("ultimatechat.update")
                && (version = core.getUpdateChecker().getLatestVersion()) != null) {

            UpdateResult updateResult;
            if ((updateResult = core.getUpdateChecker().getResult()) == UpdateResult.DISCONNECTED
                    || updateResult == UpdateResult.UPDATED)
                return;

            player.sendMessage(ChatColor.GRAY  + "=========================");
            player.sendMessage(ChatColor.RED   + "UltimateChat is outdated!");
            player.sendMessage(ChatColor.GREEN + "Newest version: " + version);
            player.sendMessage(ChatColor.RED   + "Your version: " + core.getUpdateChecker().getActualVersion());
            player.sendMessage(ChatColor.GRAY  + "=========================");
        }
    }

    private String parseFormat(String format, Player player, String message) {
        format = format
                .replace("{player}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{msg}", message)
                .replace("%", "%%");

        Service pluginService = core.getPluginService();
        LibraryProtocol libraryProtocol = LibraryProtocolFactory.getProxyProtocol();

        /* if (getConfig().getBoolean("PlaceholderAPI") && (pm = getServer().getPluginManager()).getPlugin("PlaceholderAPI").isEnabled()) {
            PlaceholderAPI.getExpansions();
            return PlaceholderAPI.setPlaceholders((Player) name, (String) msg);
        }

         if (getConfig().getBoolean("PlaceholderAPI") && (pm = getServer().getPluginManager()).getPlugin("PlaceholderAPI").isEnabled()) {
            PlaceholderAPI.getExpansions();
            return PlaceholderAPI.setBracketPlaceholders((Player)name, (String)msg);
        } */

        if (pluginService.isEnabledService(LibraryProtocol.LUCK_PERMS_NAME))
            format = format
                    .replace("{prefix}", libraryProtocol.getPlayerPrefix(player, LibraryProtocol.LUCK_PERMS))
                    .replace("{suffix}", libraryProtocol.getPlayerSuffix(player, LibraryProtocol.LUCK_PERMS));
        else if (!(Bukkit.getPluginManager().isPluginEnabled("UltraPermissions"))) {
            String str;
            if ((str = libraryProtocol.getPlayerPrefix(player, LibraryProtocol.VAULT)) != null)
                format = format.replace("{prefix}", str);
            if ((str = libraryProtocol.getPlayerSuffix(player, LibraryProtocol.VAULT)) != null)
                format = format.replace("{suffix}", str);
        }

        if (pluginService.isEnabledService(LibraryProtocol.PLACE_HOLDER_API_NAME))
            format = libraryProtocol.setPlaceHolder(player, format, LibraryProtocol.NORMAL_HOLDER);

        if (pluginService.isEnabledService(LibraryProtocol.FACTIONS_NAME) && pluginService.isEnabledService(LibraryProtocol.MASSIVE_CORE_NAME))
            format = format.replace("{faction}", libraryProtocol.getFactionName(player, LibraryProtocol.MASSIVE_CORE));

        if (pluginService.isEnabledService(LibraryProtocol.LEGACY_FACTIONS_NAME))
            format = format.replace("{legacyfaction}", libraryProtocol.getFactionName(player, LibraryProtocol.LEGACY_FACTIONS));

        return format;
    }

    private String parseBlock(String message) {
        Configuration configuration;
        if ((configuration = core.getConfigurationContainer().getConfiguration()).getBoolean(ConfigurationValue.BLOCK_WORDS.getPath())) {
            for (String blockedWord : configuration.getStringList(ConfigurationValue.BLOCKED_WORDS.getPath())) {
                StringBuilder blockCharacter = new StringBuilder();
                for (int i = 0 ; i < blockedWord.length() ; i++)
                    blockCharacter.append('*');
                message = message.replaceAll(blockedWord, blockCharacter.toString());
            }
        }
        return message;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();

        if (getServer().getPluginManager().isPluginEnabled(LibraryProtocol.VAULT_NAME)) {
            String message = player.hasPermission("ultimateChat.colours")
                    ? ChatColor.translateAlternateColorCodes('&', e.getMessage())
                    : e.getMessage().replace(String.valueOf(ChatColor.COLOR_CHAR), "&");
            e.setFormat(ChatColor.translateAlternateColorCodes('&',
                    parseBlock(parseFormat(core.getConfigurationContainer().getConfiguration().getString(ConfigurationValue.CHAT_FORMAT.getPath()),
                            player,
                            message))));
        }
    }

}
