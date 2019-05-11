package org.ultimatechat2.configuration.provider.stream.file;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

public class YamlConfigurationProvider implements FileConfigurationProvider {

    private static FileConfigurationProvider INSTANCE;

    private YamlConfigurationProvider() { }

    public static FileConfigurationProvider getInstance() {
        if (INSTANCE == null)
            INSTANCE = new YamlConfigurationProvider();
        return INSTANCE;
    }

    @Override
    public Configuration createConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void saveConfiguration(Configuration configuration, File file) throws IOException {
        if (!(configuration instanceof FileConfiguration))
            throw new IllegalArgumentException("Configuration cannot be saved because is not a FileConfiguration");

        ((FileConfiguration) configuration).save(file);
    }

    @Override
    public Configuration createConfiguration(Reader reader) {
        return YamlConfiguration.loadConfiguration(reader);
    }

}
