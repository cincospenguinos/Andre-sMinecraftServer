package usa.alafleur.spigot_plugin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class InteractionServerTest {
    public static final int MOCK_TOTAL_PLAYERS_ONLINE_COUNT = 3;
    public static final int TEST_SERVER_PORT = 1234;

    public static final InteractionServer TEST_SERVER = new InteractionServer(TEST_SERVER_PORT);

    @BeforeAll
    public static void setupAll() {
        MockSourceOfTruth mockSource = new MockSourceOfTruth();
        mockSource.TotalPlayersOnline = MOCK_TOTAL_PLAYERS_ONLINE_COUNT;
        TEST_SERVER.setSourceOfTruth(mockSource);
        TEST_SERVER.start();
    }

    @AfterAll
    public static void teardownAll() {
        TEST_SERVER.stop();
    }

    @Test
    public void Test_InteractionServerPings() throws IOException {
        assertEquals("pong", submitRequest("ping"));
    }

    @Test
    public void Test_InteractionServerGivesOnlineCount() throws IOException {
        assertEquals(Integer.toString(MOCK_TOTAL_PLAYERS_ONLINE_COUNT), submitRequest("total_players_online"));
    }

    @Test
    public void Test_InteractionServerDoesActuallyStopWhenToldToDoSo() throws InterruptedException {
        final InteractionServer server = new InteractionServer(4321);
        server.start();
        new Thread(server::stop).start();
        Thread.sleep(30);
        assertTrue(server.isStopped());
    }

    private String submitRequest(String request) throws IOException {
        Socket client = new Socket("localhost", TEST_SERVER_PORT);
        client.setSoTimeout(300);
        new PrintWriter(client.getOutputStream(), true).println(request);
        String response = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
        client.close();

        return response.trim();
    }
}