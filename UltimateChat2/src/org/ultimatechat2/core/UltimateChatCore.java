package org.ultimatechat2.core;

import net.milkbowl.vault.chat.Chat;
import org.ultimatechat2.configuration.ConfigurationContainer;
import org.ultimatechat2.service.Service;
import org.ultimatechat2.updater.UpdateChecker;

public interface UltimateChatCore {

    ConfigurationContainer getConfigurationContainer();

    Chat getChat();

    Service getPluginService();

    UpdateChecker getUpdateChecker();

}
