package usa.alafleur.spigot_plugin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteractionServer implements Runnable {
    private ServerSourceOfTruth _source;
    private Logger _logger;
    private final Thread _serverThread;

    private final int _listeningPort;
    private final AtomicBoolean _keepRunning;

    public InteractionServer(int port) {
        _listeningPort = port;
        _keepRunning = new AtomicBoolean(true);
        _serverThread = new Thread(this);
    }

    public InteractionServer(int port, ServerMaintenanceInformation sourceOfTruth, Logger logger) {
        _listeningPort = port;
        _keepRunning = new AtomicBoolean(true);
        _source = sourceOfTruth;
        _logger = logger;
        _serverThread = new Thread(this);
    }

    public void start() {
        _serverThread.start();
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(_listeningPort);

            while (_keepRunning.get()) {
                Socket client = socket.accept();
                String request = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
                String response = responseFor(request);
                new PrintWriter(client.getOutputStream(), true).println(response);
                client.close();
            }

            _serverThread.interrupt();
        } catch (IOException e) {
            log(e.toString());
        }
    }

    private void log(String message) {
        if (_logger == null) {
            return;
        }

        _logger.log(Level.INFO, message);
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
        _keepRunning.set(false);
        _serverThread.interrupt();
    }

    public boolean isStopped() {
        return !_keepRunning.get();
    }

    public void setSourceOfTruth(ServerSourceOfTruth source) {
        _source = source;
    }
}
