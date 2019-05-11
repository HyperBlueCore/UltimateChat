package org.ultimatechat2.core;

import net.milkbowl.vault.chat.Chat;
import org.ultimatechat2.configuration.ConfigurationContainer;
import org.ultimatechat2.service.Service;
import org.ultimatechat2.updater.UpdateChecker;

class CoreImpl implements UltimateChatCore {

    private final UltimateChatPlugin original;

    CoreImpl(UltimateChatPlugin original) {
        if (original == null)
            throw new NullPointerException("Original cannot be null");
        this.original = original;
    }

    @Override
    public ConfigurationContainer getConfigurationContainer() {
        return original.getConfigurationContainer();
    }

    @Override
    public Chat getChat() {
        return original.getChat();
    }

    @Override
    public Service getPluginService() {
        return original.getPluginService();
    }

    @Override
    public UpdateChecker getUpdateChecker() {
        return original.getUpdateChecker();
    }

}
