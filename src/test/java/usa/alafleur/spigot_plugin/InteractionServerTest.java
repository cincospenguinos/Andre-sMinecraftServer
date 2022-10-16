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
    public static final InteractionServer TEST_SERVER = new InteractionServer(1234);

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
        Socket client = new Socket("localhost", 1234);
        assertTrue(client.isConnected());

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out.println("ping");
        assertEquals("pong", in.readLine().trim());
        client.close();
    }
}