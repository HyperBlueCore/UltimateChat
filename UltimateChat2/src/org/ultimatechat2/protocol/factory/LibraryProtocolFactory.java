package org.ultimatechat2.protocol.factory;

import org.ultimatechat2.core.UltimateChatCore;
import org.ultimatechat2.protocol.LibraryProtocol;

public final class LibraryProtocolFactory {

    private static LibraryProtocol libraryProtocol;

    public static void initialize(UltimateChatCore core) {
        if (LibraryProtocolFactory.libraryProtocol != null)
            throw new IllegalStateException("Library Protocol was initialized");

        LibraryProtocolFactory.libraryProtocol = new ProxyLibraryProtocol(core);
    }

    public static LibraryProtocol getProxyProtocol() {
        return libraryProtocol;
    }

}
