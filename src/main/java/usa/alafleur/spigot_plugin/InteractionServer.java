package usa.alafleur.spigot_plugin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteractionServer implements Runnable {
    private ServerSourceOfTruth _source;
    private final int listeningPort;
    private Logger _logger;

    private final AtomicBoolean keepRunning;
    private final AtomicInteger _shutdownValue;
    private final SecureRandom _secureRandom;

    public InteractionServer(int port) {
        listeningPort = port;
        keepRunning = new AtomicBoolean(true);
        _secureRandom = new SecureRandom();
        _shutdownValue = new AtomicInteger();
    }

    public InteractionServer(int port, ServerMaintenanceInformation sourceOfTruth, Logger logger) {
        listeningPort = port;
        keepRunning = new AtomicBoolean(true);
        _source = sourceOfTruth;
        _logger = logger;
        _secureRandom = new SecureRandom();
        _shutdownValue = new AtomicInteger();
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(listeningPort);

            while (keepRunning.get()) {
                Socket client = socket.accept();
                String request = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
                String response = responseFor(request);
                new PrintWriter(client.getOutputStream(), true).println(response);
                client.close();
            }
        } catch (IOException e) {
            log(e.toString());
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
        byte[] bytes = new byte[4];
        _secureRandom.nextBytes(bytes);
        ByteBuffer wrappedBytes = ByteBuffer.wrap(bytes);
        int secureValue = wrappedBytes.asIntBuffer().get();
        _shutdownValue.set(secureValue);

        try (Socket socket = new Socket("localhost", listeningPort)) {
            new PrintWriter(socket.getOutputStream(), true).println(_shutdownValue);
            new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            keepRunning.set(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStopped() {
        return !keepRunning.get();
    }

    public void setSourceOfTruth(ServerSourceOfTruth source) {
        _source = source;
    }

    private void log(String message) {
        if (_logger == null) {
            return;
        }

        _logger.log(Level.INFO, message);
    }

    public void setLogger(Logger logger) {
        _logger = logger;
    }
}
