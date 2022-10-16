package usa.alafleur.spigot_plugin;

/**
 * Interface representing a source of truth for the server. There should only be one per server.
 */
public interface ServerSourceOfTruth {
    /**
     * Time in minutes since the server started.
     *
     * @return int - minutes
     */
    int getMinutesSinceStartup();

    /**
     * Total number of players online.
     *
     * @return int - total number of players online
     */
    int getTotalPlayersOnline();

    /**
     * Informs the source of truth that a player logged on.
     */
    void playerLoggedOn();

    /**
     * Returns total number of milliseconds since a player last logged on.
     *
     * @return long - milliseconds
     */
    long getMillisSinceLastLogin();
}
