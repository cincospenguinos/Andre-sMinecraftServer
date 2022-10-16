package usa.alafleur.spigot_plugin;

import org.bukkit.Server;

/**
 * Interface between the Bukkit server and anything that needs to know anything about
 * the server. Does not handle commands; only provides information.
 */
public class ServerMaintenanceInformation {
    private final long _startupTime;
    private Server _server;

    public ServerMaintenanceInformation() {
        _startupTime = System.currentTimeMillis();
    }

    /**
     * Number of minutes since this object was instantiated.
     *
     * @return int - minutes
     */
    public int getMinutesSinceStartup() {
        long now = System.currentTimeMillis();
        return Math.toIntExact((now - _startupTime) / (60L * 1000L));
    }

    public int getTotalPlayersOnline() {
        return _server.getOnlinePlayers().size();
    }

    public void setServer(Server server) {
        _server = server;
    }
}
