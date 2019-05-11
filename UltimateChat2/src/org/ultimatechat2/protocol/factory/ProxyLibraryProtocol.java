package org.ultimatechat2.protocol.factory;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.ultimatechat2.core.UltimateChatCore;
import org.ultimatechat2.protocol.LibraryProtocol;

import java.util.UUID;

class ProxyLibraryProtocol implements LibraryProtocol {

    private final UltimateChatCore core;

    ProxyLibraryProtocol(UltimateChatCore core) {
        if (core == null)
            throw new NullPointerException("Core cannot be null");

        this.core = core;
    }

    private Class<?> getPlaceHolderAPIClass() throws Throwable {
        return Class.forName("me.clip.placeholderapi.PlaceholderAPI");
    }

    @Override
    public String setPlaceHolder(Player player, String message, int type) {
        try {
            switch (type) {
                case LibraryProtocol.NORMAL_HOLDER: {
                    return (String) getPlaceHolderAPIClass()
                            .getMethod("setPlaceholders", Player.class, String.class)
                            .invoke(null, player, message);
                }
                case LibraryProtocol.BRACKET_HOLDER: {
                    return (String) getPlaceHolderAPIClass()
                            .getMethod("setBracketPlaceholders", Player.class, String.class)
                            .invoke(null, player, message);
                }
            }
        } catch (Throwable throwable) {
            throw new RuntimeException("Proxy cannot be handled", throwable);
        }

        throw new IllegalArgumentException("type: " + type);
    }

    private Class<?> getMassivePlayerClass() throws Throwable {
        return Class.forName("com.massivecraft.factions.entity.MPlayer");
    }

    private Class<?> getMassiveFactionClass() throws Throwable {
        return Class.forName("com.massivecraft.factions.entity.Faction");
    }

    private Object getMassivePlayer(Player player) throws Throwable {
        return getMassivePlayerClass()
                .getMethod("get", String.class)
                .invoke(null, player.getUniqueId().toString());
    }

    private Object getMassiveFaction(Player player) throws Throwable {
        return getMassivePlayerClass()
                .getMethod("getFaction")
                .invoke(getMassivePlayer(player));
    }

    private Class<?> getLegacyPlayerCollClass() throws Throwable {
        return Class.forName("net.redstoneore.legacyfactions.entity.FPlayerColl");
    }

    private Class<?> getLegacyPlayerClass() throws Throwable {
        return Class.forName("net.redstoneore.legacyfactions.entity.FPlayer");
    }

    private Object getLegacyPlayer(Player player) throws Throwable {
        return getLegacyPlayerCollClass()
                .getMethod("get", Object.class)
                .invoke(null, player.getUniqueId());
    }

    private Class<?> getLegacyFactionClass() throws Throwable {
        return Class.forName("net.redstoneore.legacyfactions.entity.Faction");
    }

    private Object getLegacyFaction(Player player) throws Throwable {
        return getLegacyPlayerClass()
                .getMethod("getFaction")
                .invoke(getLegacyPlayer(player));
    }

    @Override
    public String getFactionName(Player player, int type) {
        try {
            switch (type) {
                case LibraryProtocol.MASSIVE_CORE : {
                    return (String) getMassiveFactionClass()
                            .getMethod("getName")
                            .invoke(getMassiveFaction(player));
                }
                case LibraryProtocol.LEGACY_FACTIONS : {
                    return (String) getLegacyFactionClass()
                            .getMethod("getTag")
                            .invoke(getLegacyFaction(player));
                }
            }
        } catch (Throwable throwable) {
            throw new RuntimeException("Proxy cannot be handled", throwable);
        }

        throw new IllegalArgumentException("type: " + type);
    }

    private Class<?> getLuckPermsClass() throws Throwable {
        return Class.forName("me.lucko.luckperms.LuckPerms");
    }

    private Class<?> getLuckPermsAPIClass() throws Throwable {
        return Class.forName("me.lucko.luckperms.api.LuckPermsApi");
    }

    private Object getLuckPermsAPI() throws Throwable {
        return getLuckPermsClass()
                .getMethod("getApi")
                .invoke(null);
    }

    private Class<?> getContextsClass() throws Throwable {
        return Class.forName("me.lucko.luckperms.api.Contexts");
    }

    private Object getContexts(Player player) throws Throwable {
        return getLuckPermsAPIClass()
                .getMethod("getContextsForPlayer", Object.class)
                .invoke(getLuckPermsAPI(), player);
    }

    private Class<?> getUserClass() throws Throwable {
        return Class.forName("me.lucko.luckperms.api.User");
    }

    private Object getUser(Player player) throws Throwable {
        return getLuckPermsAPIClass()
                .getMethod("getUser", UUID.class)
                .invoke(getLuckPermsAPI(), player.getUniqueId());
    }

    private Class<?> getUserDataClass() throws Throwable {
        return Class.forName("me.lucko.luckperms.api.caching.UserData");
    }

    private Object getUserData(Player player) throws Throwable {
        return getUserClass()
                .getMethod("getCachedData")
                .invoke(getUser(player));
    }

    private Class<?> getMetaDataClass() throws Throwable {
        return Class.forName("me.lucko.luckperms.api.caching.MetaData");
    }

    private Object getMetaData(Player player) throws Throwable {
        return getUserDataClass()
                .getMethod("getMetaData", getContextsClass())
                .invoke(getUserData(player), getContexts(player));
    }

    @Override
    public String getPlayerPrefix(Player player, int type) {
        try {
            switch (type) {
                case LibraryProtocol.VAULT : {
                    Chat chat;
                    return (chat = core.getChat()) != null
                            ? chat.getPlayerPrefix(player)
                            : null;
                }
                case LibraryProtocol.LUCK_PERMS : {
                    return (String) getMetaDataClass()
                            .getMethod("getPrefix")
                            .invoke(getMetaData(player));
                }
            }
        } catch (Throwable throwable) {
            throw new RuntimeException("Proxy cannot be handled", throwable);
        }

        throw new IllegalArgumentException("type: " + type);
    }

    @Override
    public String getPlayerSuffix(Player player, int type) {
        try {
            switch (type) {
                case LibraryProtocol.VAULT : {
                    Chat chat;
                    return (chat = core.getChat()) != null
                            ? chat.getPlayerSuffix(player)
                            : null;
                }
                case LibraryProtocol.LUCK_PERMS : {
                    return (String) getMetaDataClass()
                            .getMethod("getSuffix")
                            .invoke(getMetaData(player));
                }

            }
        } catch (Throwable throwable) {
            throw new RuntimeException("Proxy cannot be handled", throwable);
        }

        throw new IllegalArgumentException("type: " + type);
    }
}
