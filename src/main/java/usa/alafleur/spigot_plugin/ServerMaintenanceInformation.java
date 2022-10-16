package usa.alafleur.spigot_plugin;

public class ServerMaintenanceInformation {
    private final long startupTime;

    public ServerMaintenanceInformation() {
        startupTime = System.currentTimeMillis();
    }

    public long getMinutesSinceStartup() {
        long now = System.currentTimeMillis();
        return (now - startupTime) / (60L * 1000L);
    }
}
