package usa.alafleur.spigot_plugin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteractionServer implements Runnable {
    private static final long CLIENT_CHECK_INTERVAL_MILLIS = 10L;

    private ServerSourceOfTruth _source;
    private int listeningPort;
    private final AtomicBoolean keepRunning;
    private Logger _logger;

    public InteractionServer(int port) {
        listeningPort = port;
        keepRunning = new AtomicBoolean(true);
    }

    public InteractionServer(int port, ServerMaintenanceInformation sourceOfTruth, Logger logger) {
        listeningPort = port;
        keepRunning = new AtomicBoolean(true);
        _source = sourceOfTruth;
        _logger = logger;
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(listeningPort);

            while (keepRunning.get()) {
                Thread.sleep(CLIENT_CHECK_INTERVAL_MILLIS);

                Socket client = socket.accept();
                String request = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
                String response = responseFor(request);
                new PrintWriter(client.getOutputStream(), true).println(response);
                client.close();
            }
        } catch (InterruptedException | IOException e) {
            if (_logger != null) {
                _logger.log(Level.SEVERE, e.toString());
            }
        }
    }

    private String responseFor(String request) {
        if (request.equals("ping")) {
            return "pong";
        }

        if (request.equals("total_players_online")) {
            return Integer.toString(_source.getTotalPlayersOnline());
        }

        return "invalid_request";
    }

    public void stop() {
        keepRunning.set(false);
    }

    public void setSourceOfTruth(ServerSourceOfTruth source) {
        _source = source;
    }

    public void setLogger(Logger logger) {
        _logger = logger;
    }
}
