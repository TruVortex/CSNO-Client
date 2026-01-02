import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {

    public static String ip;
    public static int port;

    public static void main(String[] args) {
        ClientFrame game = new ClientFrame();
        while (true) {
            try (
                Socket clientSocket = new Socket(ip, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String input = in.readLine();
                if (input.equals("CONNECTED")) {
                    game.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    game.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            out.println("DISCONNECT");
                            System.exit(0);
                        }
                    });
                    game.setCursor(game.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
                    ClientPanel.centerMouse();
                    ClientPanel.inGame = true;
                    ClientPanel.respawn();
                    while ((input = in.readLine()) != null && ip != null) {
                        if (input.equals("GET")) {
                            out.println(ClientPanel.camPos[0] + " " + ClientPanel.camPos[1] + " " + (ClientPanel.camPos[2] - ClientPanel.EYE_HEIGHT - ClientPanel.crouchZ) + " " + (ClientPanel.crouchZ == 0 ? 0 : -ClientPanel.PLAYER_HEIGHT / 4) + " " + ClientPanel.spawnProtection);
                        } else if (input.equals("EVENTS")) {
                            while (!ClientPanel.EVENTS.isEmpty()) {
                                out.println(ClientPanel.EVENTS.poll());
                            }
                            out.println("END");
                        } else if (input.equals("RESPAWN")) {
                            ClientPanel.respawn();
                            out.println(ClientPanel.camPos[0] + " " + ClientPanel.camPos[1] + " " + (ClientPanel.camPos[2] - ClientPanel.EYE_HEIGHT - ClientPanel.crouchZ) + " " + (ClientPanel.crouchZ == 0 ? 0 : -ClientPanel.PLAYER_HEIGHT / 4) + " " + ClientPanel.spawnProtection);
                        } else if (input.equals("UPDATE")) {
                            StringTokenizer tokens = new StringTokenizer(in.readLine());
                            ClientPanel.health = tokens.nextToken();
                            ClientPanel.kills = tokens.nextToken();
                            ClientPanel.deaths = tokens.nextToken();
                            while (!(input = in.readLine()).equals("END")) {
                                tokens = new StringTokenizer(input);
                                ClientPanel.PLAYERS[Integer.parseInt(tokens.nextToken())].update(Double.parseDouble(tokens.nextToken()), Double.parseDouble(tokens.nextToken()), Double.parseDouble(tokens.nextToken()), Double.parseDouble(tokens.nextToken()), Boolean.parseBoolean(tokens.nextToken()));
                            }
                        }
                    }
                    out.println("DISCONNECT");
                    game.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            } catch (IOException ignored) {}
        }
    }
}
