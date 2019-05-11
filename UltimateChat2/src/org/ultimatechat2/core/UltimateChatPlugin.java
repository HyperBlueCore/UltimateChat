package org.ultimatechat2.core;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.ultimatechat2.command.ultimatechat.UltimateChatCommand;
import org.ultimatechat2.configuration.ConfigurationContainer;
import org.ultimatechat2.configuration.provider.stream.file.FileConfigurationContainer;
import org.ultimatechat2.configuration.provider.stream.file.YamlConfigurationProvider;
import org.ultimatechat2.listener.PlayerListener;
import org.ultimatechat2.protocol.LibraryProtocol;
import org.ultimatechat2.protocol.factory.LibraryProtocolFactory;
import org.ultimatechat2.service.MapService;
import org.ultimatechat2.service.Service;
import org.ultimatechat2.service.ServiceEntry;
import org.ultimatechat2.updater.UpdateChecker;
import org.ultimatechat2.updater.UpdateResult;
import org.ultimatechat2.util.ConfigurationValue;
import org.ultimatechat2.util.Metrics;

public class UltimateChatPlugin extends JavaPlugin {

    private static final String FILE_CONFIGURATION_NAME = "config.yml";

    private static class ServiceData implements ServiceEntry {

        final String pluginName;
        Plugin resource;

        ServiceData(String pluginName) {
            this.pluginName = pluginName;
        }

        void localizePlugin() {
            resource = Bukkit.getPluginManager().getPlugin(pluginName);
        }

        @Override
        public String getServiceName() {
            return pluginName;
        }

        @Override
        public boolean isEnabled() {
            if (resource == null)
                localizePlugin();
            return resource != null && resource.isEnabled();
        }

    }

    private static class ConfigurationServiceData extends ServiceData {

        final ConfigurationContainer configurationContainer;
        final ConfigurationValue configurationValue;

        ConfigurationServiceData(String pluginName, ConfigurationContainer configurationContainer, ConfigurationValue configurationValue) {
            super(pluginName);
            this.configurationContainer = configurationContainer;
            this.configurationValue = configurationValue;
        }

        @Override
        public boolean isEnabled() {
            return configurationContainer.getConfiguration().getBoolean(configurationValue.getPath())
                    && super.isEnabled();
        }

    }

    private static class ServiceImpl extends MapService {

        final Collection<ServiceEntry> serviceList;

        ServiceImpl(Collection<ServiceEntry> serviceList) {
            super(serviceList);
            this.serviceList = serviceList;
        }

        @Override
        public void updateServices() {
            for (ServiceEntry entry : serviceList) {
                if (entry instanceof ServiceData)
                    ((ServiceData) entry).localizePlugin();
            }
        }

    }

    private static class DefaultConfigurationImpl implements FileConfigurationContainer.DefaultConfiguration {

        final Plugin plugin;

        DefaultConfigurationImpl(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public Reader getReader() {
            InputStream inputStream;
            return (inputStream = plugin.getResource(FILE_CONFIGURATION_NAME)) != null ? new InputStreamReader(inputStream) : null;
        }

    }

    private UltimateChatCore core;
    private ConfigurationContainer configurationContainer;
    private Chat chat;
    private Service pluginService;
    private UpdateChecker updateChecker;

    @Override
    public void onLoad() {
        LibraryProtocolFactory.initialize((core = new CoreImpl(this)));
    }

    private boolean checkLibrary() {
        Plugin vaultPlugin;
        if ((vaultPlugin = Bukkit.getPluginManager().getPlugin(LibraryProtocol.VAULT_NAME)) != null) {
            if (Bukkit.getPluginManager().isPluginEnabled(vaultPlugin)) {
                RegisteredServiceProvider<Chat> chatProvider;
                if ((chatProvider = getServer().getServicesManager().getRegistration(Chat.class)) != null)
                    chat = chatProvider.getProvider();
                else
                    getLogger().log(Level.SEVERE, "Vault Chat Provider is null, therefore, plugin won't work correctly");
                return true;
            }
        }

        return false;
    }

    private void initializeService() {
        final List<ServiceEntry> preList = new ArrayList<>();

        Collection<ServiceEntry> preloaded = new AbstractCollection<ServiceEntry>() {
            @Override
            public Iterator<ServiceEntry> iterator() {
                class IteratorImpl implements Iterator<ServiceEntry> {

                    private final Iterator<ServiceEntry> it = preList.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public ServiceEntry next() {
                        return it.next();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Remove is not supported.");
                    }

                }
                return new IteratorImpl();
            }

            @Override
            public int size() {
                return preList.size();
            }
        };

        preList.add(new ServiceData(LibraryProtocol.VAULT_NAME));
        preList.add(new ConfigurationServiceData(LibraryProtocol.PLACE_HOLDER_API_NAME,
                configurationContainer,
                ConfigurationValue.PLACE_HOLDER_API));
        preList.add(new ServiceData(LibraryProtocol.FACTIONS_NAME));
        preList.add(new ServiceData(LibraryProtocol.LEGACY_FACTIONS_NAME));
        preList.add(new ServiceData(LibraryProtocol.LUCK_PERMS_NAME));
        preList.add(new ServiceData(LibraryProtocol.MASSIVE_CORE_NAME));

        pluginService = new ServiceImpl(preloaded);
    }

    @Override
    public void onEnable() {
        File configurationFile;
        configurationContainer = new FileConfigurationContainer((configurationFile = new File(getDataFolder(), "config.yml")),
                YamlConfigurationProvider.getInstance(),
                new DefaultConfigurationImpl(this));
        if (!(configurationFile.exists())) {
            configurationContainer.reloadConfiguration();
            configurationContainer.saveConfiguration();
        }

        Metrics.startSubmitting(this);

        if (checkLibrary()) {
            initializeService();

            updateChecker = new UpdateChecker(getLogger(), getDescription().getVersion());

            UpdateResult updateResult;
            if ((updateResult = updateChecker.getResult()) != UpdateResult.DISCONNECTED) {
                if (updateResult == UpdateResult.OUTDATED) {
                    getServer().getConsoleSender().sendMessage("------------------------");
                    getServer().getConsoleSender().sendMessage("UltimateChat is outdated!");
                    getServer().getConsoleSender().sendMessage("Newest version: " + updateChecker.getLatestVersion());
                    getServer().getConsoleSender().sendMessage("Your version: " + updateChecker.getActualVersion());
                    getServer().getConsoleSender().sendMessage("Please Update Here: https://www.spigotmc.org/resources/43489");
                    getServer().getConsoleSender().sendMessage("------------------------");
                } else {
                    getServer().getConsoleSender().sendMessage("------------------------");
                    getServer().getConsoleSender().sendMessage("UltimateChat is up to date!");
                    getServer().getConsoleSender().sendMessage("------------------------");
                }
            }

            Bukkit.getPluginManager().registerEvents(new PlayerListener(core), this);

            PluginCommand pluginCommand;
            if ((pluginCommand = getCommand("ultimatechat")) == null)
                throw new RuntimeException("ultimatechat Command is not registered in plugin.yml");

            pluginCommand.setExecutor(new UltimateChatCommand(configurationContainer));
        } else {
            getLogger().info("Disabling plugin! Vault is disabled or not found!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    ConfigurationContainer getConfigurationContainer() {
        return configurationContainer;
    }

    Chat getChat() {
        return chat;
    }

    Service getPluginService() {
        return pluginService;
    }

    UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

}

