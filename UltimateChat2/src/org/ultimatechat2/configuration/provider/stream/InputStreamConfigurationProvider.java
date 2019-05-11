package org.ultimatechat2.configuration.provider.stream;

import org.bukkit.configuration.Configuration;
import org.ultimatechat2.configuration.provider.ConfigurationProvider;

import java.io.Reader;

public interface InputStreamConfigurationProvider extends ConfigurationProvider {

    Configuration createConfiguration(Reader reader);

}
