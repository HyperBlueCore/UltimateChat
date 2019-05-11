package org.ultimatechat2.util;

import org.bukkit.ChatColor;

public class Color {

    public static String getColor(char ch, String input) {
        return ChatColor.translateAlternateColorCodes(ch, input);
    }

}
