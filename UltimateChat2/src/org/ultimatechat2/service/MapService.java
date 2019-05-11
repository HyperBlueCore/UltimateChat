package org.ultimatechat2.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class MapService implements Service {

    private final Map<String, ServiceEntry> services;

    public MapService(Collection<ServiceEntry> collection) {
        if (collection == null)
            throw new NullPointerException("Preloaded list cannot be null");
        services = new HashMap<>();
        for (ServiceEntry entry : collection) {
            if (entry != null)
                services.put(entry.getServiceName(), entry);
        }
    }

    @Override
    public boolean isEnabledService(String serviceName) {
        ServiceEntry entry;
        return (entry = services.get(serviceName)) != null && entry.isEnabled();
    }

    @Override
    public abstract void updateServices();

}
