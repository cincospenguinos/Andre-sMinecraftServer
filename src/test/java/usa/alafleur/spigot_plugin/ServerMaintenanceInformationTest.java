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
}
