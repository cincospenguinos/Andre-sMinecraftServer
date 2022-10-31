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
        boolean enoughMinutesElapsed = _source.getMinutesSinceStartup() > MINUTES_TO_LOG_ON;
        if (!enoughMinutesElapsed) {
            return false;
        }

        boolean someoneHasLoggedOn = _source.getMillisSinceLastLogin() > 0;
        if (!someoneHasLoggedOn) {
            return true;
        }

        boolean enoughTimeSinceLogin = _source.getMillisSinceLastLogin() > MILLIS_LOGIN_TIME_LIMIT;
        boolean noPlayers = _source.getTotalPlayersOnline() == 0;

        return enoughTimeSinceLogin && noPlayers;
    }
}
