package org.ultimatechat2.protocol;

import org.bukkit.entity.Player;

public interface LibraryProtocol {

    String VAULT_NAME            = "Vault";
    String PLACE_HOLDER_API_NAME = "PlaceholderAPI";
    String FACTIONS_NAME         = "Factions";
    String LEGACY_FACTIONS_NAME  = "LegacyFactions";
    String LUCK_PERMS_NAME       = "LuckPerms";
    String MASSIVE_CORE_NAME     = "MassiveCore";

    // PlaceHolderAPI starts

    int NORMAL_HOLDER            = 0x0;
    int BRACKET_HOLDER           = 0x1;

    // PlaceHolderAPI ends

    // Faction Names starts

    int MASSIVE_CORE             = 0x0;
    int LEGACY_FACTIONS          = 0x1;

    // Faction Names ends

    // Prefix/Suffix starts

    int VAULT                    = 0x2;
    int LUCK_PERMS               = 0x3;

    // Prefix/Suffix ends

    String setPlaceHolder(Player player, String message, int type);

    String getFactionName(Player player, int type);

    String getPlayerPrefix(Player player, int type);

    String getPlayerSuffix(Player player, int type);

}
