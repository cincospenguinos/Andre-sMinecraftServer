package usa.alafleur.spigot_plugin;

/**
 * Determines whether or not to stop the server
 */
public class ServerTerminationDetermination {
    public static final int MINUTES_TO_LOG_ON = 10;
    public static final long MILLIS_LOGIN_TIME_LIMIT = 30000L;

    private final ServerSourceOfTruth _source;

    public ServerTerminationDetermination(ServerSourceOfTruth sourceOfTruth) {
        _source = sourceOfTruth;
    }

    /**
     * Returns true if it is time to stop the server.
     *
     * @return true if it is time to stop the server
     */
    public boolean timeToStopServer() {
        if (_source.getMinutesSinceStartup() < MINUTES_TO_LOG_ON) {
            return false;
        }

        if (_source.getTotalPlayersOnline() > 0) {
            return false;
        }

        return _source.getMillisSinceLastLogin() >= MILLIS_LOGIN_TIME_LIMIT;
    }
}
