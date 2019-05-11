package org.ultimatechat2.configuration;

import org.bukkit.configuration.Configuration;

public interface ConfigurationContainer {

    void reloadConfiguration();

    Configuration getConfiguration();

    void saveConfiguration();

}
