package usa.alafleur.spigot_plugin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class InteractionServer implements Runnable {
    private static final long CLIENT_CHECK_INTERVAL_MILLIS = 10L;

    private ServerSourceOfTruth _source;
    private int listeningPort;
    private final AtomicBoolean keepRunning;

    public InteractionServer(int port) {
        listeningPort = port;
        keepRunning = new AtomicBoolean(true);
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
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = in.readLine();
                PrintWriter out = new PrintWriter(client.getOutputStream());
                out.println("pong");
                out.flush();
                client.close();
            }
        } catch (InterruptedException e) {
            // TODO: Log interrupted exception information and stop the interaction server
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        keepRunning.set(false);
    }

    public void setSourceOfTruth(ServerSourceOfTruth source) {
        _source = source;
    }
}
