package org.ultimatechat2;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.util.IdUtil;
import java.util.List;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.milkbowl.vault.chat.Chat;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.ultimatechat2.Metrics;
import org.ultimatechat2.UpdateCheker;
import org.ultimatechat2.onJoin;

public class Main
        extends JavaPlugin
        implements Listener {
    public static Main plugin;
    private static Chat chat;
    private String msg;
    private String prefix = "";
    private String suffix = "";
    private String playername;
    private Faction faction;
    private Player name;
    private UpdateCheker checker;

    public Chat getChat() {
        return chat;
    }

    private String getColor(String msg) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)msg);
    }

    public void onEnable() {
        Metrics metrics = new Metrics(this);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        PluginManager pm = getServer().getPluginManager();
        Bukkit.getServer().getPluginManager().registerEvents((Listener)new onJoin(), (Plugin)this);
        plugin = this;
        checker = new UpdateCheker(this);
        if (checker.isConnected()) {
            if (checker.hasUpdate()) {
                getServer().getConsoleSender().sendMessage("------------------------");
                getServer().getConsoleSender().sendMessage("UltimateChat is outdated!");
                getServer().getConsoleSender().sendMessage("Newest version: " + checker.getLatestVersion());
                getServer().getConsoleSender().sendMessage("Your version: " + plugin.getDescription().getVersion());
                getServer().getConsoleSender().sendMessage("Please Update Here: https://www.spigotmc.org/resources/43489");
                getServer().getConsoleSender().sendMessage("------------------------");
            } else {
                getServer().getConsoleSender().sendMessage("------------------------");
                getServer().getConsoleSender().sendMessage("UltimateChat is up to date!");
                getServer().getConsoleSender().sendMessage("------------------------");
            }
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            setupChat();
            registerConfig();
        }
    }

    private void registerConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (p.hasPermission("ultimatechat.admin") || p.isOp()) {
                if (cmd.getName().equalsIgnoreCase("ultimatechat")) {
                    if (args.length <= 2) {
                        p.sendMessage("Incorrect Arguments");
                    }
                    if(args.length == 1){
                        if (args[0].equalsIgnoreCase("reload")) {
                            reloadConfig();
                            saveConfig();
                            getConfig();
                            p.sendMessage(getColor("&6[UltimateChat] Config reloaded!"));
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("help")) {
                            p.sendMessage(getColor("&6UltimateChat2 Commands List"));
                            p.sendMessage(getColor("&6/UltimateChat Reload - Reloads config."));
                            p.sendMessage(getColor("&6/UltimateChat Help - Shows this list."));
                            p.sendMessage(getColor("&6/UltimateChat - Shows this list."));
                            return true;
                        }
                    }
                    if (args.length == 0){
                        p.sendMessage(getColor("&6UltimateChat2 Commands List"));
                        p.sendMessage(getColor("&6/UltimateChat Reload - Reloads config."));
                        p.sendMessage(getColor("&6/UltimateChat Help - Shows this list."));
                        p.sendMessage(getColor("&6/UltimateChat - Shows this list."));
                        return true;
                    }
                }
            } else {
                p.sendMessage((Object)ChatColor.DARK_RED + "You do not have permission to perform this command!");
            }
            return false;
        }
        return false;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p;
        name = p = e.getPlayer();
        playername = p.getName();
        msg = e.getMessage();
        for (int i = 0; i < e.getMessage().length(); ++i) {
            if (e.getMessage().charAt(i) != '&' || p.hasPermission("ultimateChat.colours")) continue;
            msg = e.getMessage().replaceAll("&", "");
        }
        if (getServer().getPluginManager().isPluginEnabled("Factions") && getServer().getPluginManager().isPluginEnabled("MassiveCore")) {
            String uuidString = String.valueOf(p.getUniqueId());
            UUID uuid = UUID.fromString(uuidString);
            ConsoleCommandSender sender = Bukkit.getConsoleSender();
            MPlayer mplayer = MPlayer.get((Object)IdUtil.getId((Object)playername));
            faction = mplayer.getFaction();
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            if (chat.getPlayerPrefix(p) != null && !Bukkit.getPluginManager().isPluginEnabled("UltraPermissions")) {
                prefix = chat.getPlayerPrefix(p);
                suffix = chat.getPlayerSuffix(p);
            }
            if (getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
                LuckPermsApi api = LuckPerms.getApi();
                Contexts contexts = api.getContextsForPlayer((Object)p);
                UserData cachedData = api.getUser(p.getName()).getCachedData();
                MetaData metaData = cachedData.getMetaData(contexts);
                String prefix = metaData.getPrefix();
                String suffix = metaData.getSuffix();
                if (suffix != null) {
                    suffix = suffix;
                    if (prefix != null) {
                        suffix = suffix;
                    }
                }
            }
            String msgConfig = getConfig().getString("chatFormat");
            e.setFormat(getColor(getVar(msgConfig)));
        }
    }

    private String getVar(String msg) {
        PluginManager pm;
        msg = msg.replace("{player}", playername);
        msg = msg.replace("{displayname}", name.getDisplayName());
        msg = msg.replace("{msg}", this.msg);
        msg = msg.replace("%", "%%");
        if (getServer().getPluginManager().isPluginEnabled("Factions") && getServer().getPluginManager().isPluginEnabled("MassiveCore")) {
            msg = msg.replace("{faction}", faction.getName());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("LegacyFactions")) {
            FPlayer fplayer = FPlayerColl.get((Object)name);
            net.redstoneore.legacyfactions.entity.Faction faction = fplayer.getFaction();
            String tag = faction.getTag();
            msg = msg.replace("{legacyfaction}", tag);
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            msg = msg.replace("{prefix}", prefix);
            msg = msg.replace("{suffix}", suffix);
        }
        if (getConfig().getBoolean("PlaceholderAPI") && (pm = getServer().getPluginManager()).getPlugin("PlaceholderAPI").isEnabled()) {
            PlaceholderAPI.getExpansions();
            return PlaceholderAPI.setPlaceholders((Player)name, (String)msg);
        }
        if (getConfig().getBoolean("PlaceholderAPI") && (pm = getServer().getPluginManager()).getPlugin("PlaceholderAPI").isEnabled()) {
            PlaceholderAPI.getExpansions();
            return PlaceholderAPI.setBracketPlaceholders((Player)name, (String)msg);
        }
        if (getConfig().getBoolean("BlockWords")) {
            List stringList = getConfig().getStringList("BlockedWords");
            for (Object s : stringList) {
                if (!msg.contains(String.valueOf(s))) continue;
                msg.replaceAll((String.valueOf(s)), "*****");
            }
        }
        return msg;
    }

    private boolean setupChat() {
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider rsp = getServer().getServicesManager().getRegistration(Chat.class);
            chat = (Chat)rsp.getProvider();
            return chat != null;
        }
        return false;
    }
}

