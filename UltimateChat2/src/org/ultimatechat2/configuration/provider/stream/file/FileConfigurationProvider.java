package org.ultimatechat2.configuration.provider.stream.file;

import org.bukkit.configuration.Configuration;
import org.ultimatechat2.configuration.provider.stream.InputStreamConfigurationProvider;

import java.io.File;
import java.io.IOException;

public interface FileConfigurationProvider extends InputStreamConfigurationProvider {

    Configuration createConfiguration(File file);

    void saveConfiguration(Configuration configuration, File file) throws IOException;

}
