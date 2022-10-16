package usa.alafleur.spigot_plugin;

import org.bukkit.Server;

/**
 * Interface between the Bukkit server and anything that needs to know anything about
 * the server. Does not handle commands; only provides information.
 */
public class ServerMaintenanceInformation implements ServerSourceOfTruth {
    private final long _startupTime;
    private Server _server;
    private long _lastLoginMillis;
    private final Object _mutex;

    public ServerMaintenanceInformation() {
        _startupTime = System.currentTimeMillis();
        _lastLoginMillis = -1;
        _mutex = new Object();
    }

    public ServerMaintenanceInformation(Server server) {
        _startupTime = System.currentTimeMillis();
        _lastLoginMillis = -1;
        _mutex = new Object();
        _server = server;
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

    /**
     * Inform this source of truth that a player logged on.
     */
    public void playerLoggedOn() {
        synchronized (_mutex) {
            _lastLoginMillis = System.currentTimeMillis();
        }
    }

    public long getMillisSinceLastLogin() {
        synchronized (_mutex) {
            if (_lastLoginMillis < 0) {
                return 0;
            }

            return System.currentTimeMillis() - _lastLoginMillis;
        }
    }

    public void setServer(Server server) {
        _server = server;
    }
}
