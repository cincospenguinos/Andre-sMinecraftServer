package usa.alafleur.spigot_plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTerminationDeterminationTest {
    @Test
    public void Test_SaysNoToRecentlyStarted() {
        MockSourceOfTruth mockSource = new MockSourceOfTruth();
        mockSource.MinutesSinceStartup = 0;
        mockSource.TotalPlayersOnline = 0;
        mockSource.MillisSinceLastLogin = -1;

        ServerTerminationDetermination determination = new ServerTerminationDetermination(mockSource);
        assertFalse(determination.timeToStopServer());
    }

    @Test
    public void Test_SaysYesToLongAfterStart() {
        MockSourceOfTruth mockSource = new MockSourceOfTruth();
        mockSource.MinutesSinceStartup = 11;
        mockSource.TotalPlayersOnline = 0;
        mockSource.MillisSinceLastLogin = -1;

        ServerTerminationDetermination determination = new ServerTerminationDetermination(mockSource);
        assertTrue(determination.timeToStopServer());
    }

    @Test
    public void Test_SaysNoWhenFolksAreOn() {
        MockSourceOfTruth mockSource = new MockSourceOfTruth();
        mockSource.MinutesSinceStartup = 20;
        mockSource.TotalPlayersOnline = 1;
        mockSource.MillisSinceLastLogin = 999999999999999L;

        ServerTerminationDetermination determination = new ServerTerminationDetermination(mockSource);
        assertFalse(determination.timeToStopServer());
    }

    @Test
    public void Test_SaysNoWithRecentLogin() {
        MockSourceOfTruth mockSource = new MockSourceOfTruth();
        mockSource.MinutesSinceStartup = 11;
        mockSource.TotalPlayersOnline = 0;
        mockSource.MillisSinceLastLogin = 5000L;

        ServerTerminationDetermination determination = new ServerTerminationDetermination(mockSource);
        assertFalse(determination.timeToStopServer());
    }

    @Test
    public void Test_SaysYesToMoreThanTenMinutesSinceStartupAndNoPlayersAndThirtySecondsSinceLogin() {
        MockSourceOfTruth mockSource = new MockSourceOfTruth();
        mockSource.MinutesSinceStartup = 11;
        mockSource.TotalPlayersOnline = 0;
        mockSource.MillisSinceLastLogin = 30001L;

        ServerTerminationDetermination determination = new ServerTerminationDetermination(mockSource);
        assertTrue(determination.timeToStopServer());
    }
}
