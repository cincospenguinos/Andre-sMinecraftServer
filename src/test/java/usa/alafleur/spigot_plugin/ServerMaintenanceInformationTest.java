package usa.alafleur.spigot_plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerMaintenanceInformationTest {

    @Test
    public void Test_Instantiates() {
        new ServerMaintenanceInformation();
    }

    @Test
    public void Test_IndicatesTimeSinceStartup() {
        ServerMaintenanceInformation source = new ServerMaintenanceInformation();
        assertEquals(0, source.getMinutesSinceStartup());
    }

    @Test
    public void Test_IndicatesNumberOfPlayersOnline() {
        MockServer server = new MockServer();
        server.TotalOnlinePlayers = 0;
        ServerMaintenanceInformation source = new ServerMaintenanceInformation();
        source.setServer(server);

        assertEquals(0, source.getTotalPlayersOnline());

        server.TotalOnlinePlayers = 10;
        assertEquals(10, source.getTotalPlayersOnline());
    }
}
