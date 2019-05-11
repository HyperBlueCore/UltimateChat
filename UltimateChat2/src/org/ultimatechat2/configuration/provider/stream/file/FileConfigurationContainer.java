package org.ultimatechat2.configuration.provider.stream.file;

import org.bukkit.configuration.Configuration;
import org.ultimatechat2.configuration.ConfigurationContainer;
import org.ultimatechat2.configuration.provider.ConfigurationProvider;
import org.ultimatechat2.configuration.provider.stream.InputStreamConfigurationProvider;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

public class FileConfigurationContainer implements ConfigurationContainer {

    public interface DefaultConfiguration {

        Reader getReader();

    }

    private final File file;
    private final ConfigurationProvider provider;
    private final DefaultConfiguration defaultConfiguration;

    private Configuration configuration;

    public FileConfigurationContainer(File file, ConfigurationProvider provider, DefaultConfiguration defaultConfiguration) {
        if (file == null)
            throw new NullPointerException("File cannot be null");
        if (provider == null)
            throw new NullPointerException("Provider cannot be null");
        if (!(provider instanceof InputStreamConfigurationProvider))
            throw new IllegalArgumentException("Provider is not supported");

        this.file = file;
        this.provider = provider;
        this.defaultConfiguration = defaultConfiguration;
    }

    public void reloadConfiguration() {
        if (provider instanceof FileConfigurationProvider)
            configuration = ((FileConfigurationProvider) provider).createConfiguration(file);

        if (configuration == null || defaultConfiguration == null)
            return;

        Reader def;
        if ((def = defaultConfiguration.getReader()) != null)
            configuration.setDefaults(((InputStreamConfigurationProvider) provider).createConfiguration(def));

    }

    @Override
    public Configuration getConfiguration() {
        if (configuration == null)
            reloadConfiguration();
        return configuration;
    }

    @Override
    public void saveConfiguration() {
        if (provider instanceof FileConfigurationProvider) {
            try {
                ((FileConfigurationProvider) provider).saveConfiguration(configuration, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
