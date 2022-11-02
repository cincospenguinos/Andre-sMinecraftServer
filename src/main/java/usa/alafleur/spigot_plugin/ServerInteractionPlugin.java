package usa.alafleur.spigot_plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Level;

/**
 * ServerInteractionPlugin, where the magic happens. Think of it as the Main class.
 */
public class ServerInteractionPlugin extends JavaPlugin implements Listener, Runnable {
    private static final long TERMINATION_TASK_DELAY_TICKS = 1000L;
    private static final long TERMINATION_TASK_DELAY_PERIOD_TICKS = 3000L;

    private static final int INTERACTION_SERVER_PORT = 25566;

    private ServerMaintenanceInformation _sourceOfTruth;
    private InteractionServer _interactionServer;

    @Override
    public void onEnable() {
        super.onEnable();

        _sourceOfTruth = new ServerMaintenanceInformation(getServer());
        _interactionServer = new InteractionServer(INTERACTION_SERVER_PORT, _sourceOfTruth, getLogger());
        _interactionServer.start();

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, this, TERMINATION_TASK_DELAY_TICKS, TERMINATION_TASK_DELAY_PERIOD_TICKS);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        _sourceOfTruth.playerLoggedOn();
    }

    /**
     * NOTE: This determines whether it is time or not to shut down the server
     */
    @Override
    public void run() {
        ServerTerminationDetermination determination = new ServerTerminationDetermination(_sourceOfTruth);
        if (determination.timeToStopServer()) {
            _interactionServer.stop();
            getLogger().log(Level.INFO, "Stopping the server...");
            getServer().shutdown();
        }

        getLogger().log(Level.INFO, "Not time to stop the server.");
    }
}
