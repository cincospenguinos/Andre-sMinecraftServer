package usa.alafleur.spigot_plugin;

public class MockSourceOfTruth implements ServerSourceOfTruth {
    public int MinutesSinceStartup;
    public int TotalPlayersOnline;
    public long MillisSinceLastLogin;

    @Override
    public int getMinutesSinceStartup() {
        return MinutesSinceStartup;
    }

    @Override
    public int getTotalPlayersOnline() {
        return TotalPlayersOnline;
    }

    @Override
    public void playerLoggedOn() {}

    @Override
    public long getMillisSinceLastLogin() {
        return MillisSinceLastLogin;
    }
}
