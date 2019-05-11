package org.ultimatechat2.util;

public enum ConfigurationValue {

    CHAT_FORMAT("chatFormat"),
    PLACE_HOLDER_API("PlaceholderAPI"),
    BLOCK_WORDS("BlockWords"),
    BLOCKED_WORDS("BlockedWords");

    final String path;

    ConfigurationValue(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
