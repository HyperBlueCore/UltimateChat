/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package org.ultimatechat2.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateChecker {

    // Spigot API
    private static final String PLUGIN_LINK  = "https://api.spigotmc.org/legacy/update.php?resource=43489";
    // Plugin Key for requests
    private static final String PLUGIN_KEY   = "key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=43489";
    // Format where bytes will be parsed
    private static final String CHARSET_NAME = "UTF-8";

    private final Logger logger;
    private final String actualVersion;
    private String latestVersion;

    /**
     * Construct a new instance
     * @param logger where it'll be sent messages
     * @param actualVersion the plugin version
     */
    public UpdateChecker(Logger logger, String actualVersion) {
        this.logger = logger;
        this.actualVersion = actualVersion;
    }

    /**
     * Get the plugin version installed.
     *
     * @return the plugin version
     */
    public String getActualVersion() {
        return actualVersion;
    }

    /**
     * Return the latest version found out in the official spigot plugin site.
     * <p>
     * If this returns null then something went wrong.
     *
     * @return the latest version of plugin
     */
    public String getLatestVersion() {
        if (latestVersion == null) {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(PLUGIN_LINK).openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.getOutputStream().write(PLUGIN_KEY.getBytes(CHARSET_NAME));
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String version;
                if ((version = reader.readLine()).length() <= 7)
                    latestVersion = version;
                reader.close();
                con.disconnect();
            }
            catch (Throwable ignored) {
                logger.log(Level.SEVERE, "-----------------------------");
                logger.log(Level.SEVERE, "Failed to check for a update!");
                logger.log(Level.SEVERE, "-----------------------------");
            }
        }
        return latestVersion;
    }

    /**
     * Check to know if it is updated or not.
     * <p>
     * Return true if actual version of plugin and returned version from website
     * are the same one, otherwise false if it couldn't connected to website or
     * are not the same one.
     *
     * @return {@code true} if it is updated, otherwise {@code false}
     */
    public UpdateResult getResult() {
        if (getLatestVersion() == null)
            return UpdateResult.DISCONNECTED;
        return latestVersion.equals(actualVersion) ? UpdateResult.UPDATED : UpdateResult.OUTDATED;
    }

}

