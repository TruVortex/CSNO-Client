/*
 * Henry Bao
 * January 19, 2025
 * This is a game based on Valve's CS:GO except with water guns
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class ClientPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {

    private final int FRAMES = 90;
    private double lastTime;
    private double deltaTime;

    public static boolean inGame;
    public static boolean drawControls;

    private final CSTextArea AREA_IP;
    private final CSTextArea AREA_PORT;
    private final CSButton BUTTON_CONTROLS;
    private final BufferedImage CONTROLS_IMAGE;
    private final CSButton BUTTON_JOIN;
    private final CSButton BUTTON_XIT;

    public static final double PLAYER_HEIGHT = 2;
    private static final double PLAYER_WIDTH = PLAYER_HEIGHT * 32 / 72;
    public static final double PLAYER_RADIUS = PLAYER_WIDTH / 2;
    private final double CROUCH_HEIGHT = PLAYER_HEIGHT * 54 / 72;
    public static final double EYE_HEIGHT = PLAYER_HEIGHT * 8 / 9;
    private final double MOVE_SPEED = PLAYER_HEIGHT * 250 / 72;
    private final double ROTATION_SPEED = 0.0006;

    private boolean mousePressed;
    public static double crouchZ;
    private double velocityZ;

    private static double rotateVert = -0.2;
    private static double rotateHorz;
    public static double[] camPos = new double[]{ 0, 0, 4.5 };
    public static double[] camFocus = new double[3];

    public static ArrayList<Floor> floorPolygons = new ArrayList<>();
    public static ArrayList<DPolygon> base = new ArrayList<>();
    public static ArrayList<DPolygon> pool = new ArrayList<>();
    public static ArrayList<Wall> wallPolygons = new ArrayList<>();
    public static ArrayList<DPolygon> drawablePolygons = new ArrayList<>();

    private final boolean[] keyPressed = new boolean[65490];

    private final BufferedImage CROSSHAIR;
    private final int[] GUN_POSITION = new int[]{ ClientFrame.SCREEN_SIZE.height - 230, ClientFrame.SCREEN_SIZE.height - 160, ClientFrame.SCREEN_SIZE.height - 100 };
    private final int[] GUN_TEXT_POSITION = new int[]{ ClientFrame.SCREEN_SIZE.height - 173, ClientFrame.SCREEN_SIZE.height - 100, ClientFrame.SCREEN_SIZE.height - 50 };
    private final BufferedImage[] GUN_IMAGES = new BufferedImage[3];
    private final double[] MOVEMENT_DEBUFF = { 215.0 / 250, 240.0 / 250, 1 };
    private static final WaterGun[] GUNS = new WaterGun[]{
        new WaterGun("Mijia Pulse", 27, 30, 0.1, 80, 2.4, false),
        new WaterGun("Water Pistol", 14, 20, 0.15, 80, 2.3, false),
        new WaterGun("Pool Noodle", 34, 0, 0.4, 0, 0, true)
    };
    private int gun = 0;

    public static final Player[] PLAYERS = new Player[5];
    public static final Queue<String> EVENTS = new ArrayDeque<>();

    public static boolean spawnProtection;
    public static String health = "100";
    public static String kills = "0";
    private final BufferedImage KILL_IMAGE;
    public static String deaths = "0";
    private final BufferedImage DEATH_IMAGE;

    public ClientPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        BUTTON_CONTROLS = new CSButton("Controls");
        BUTTON_CONTROLS.addActionListener(e -> {
            removeAll();
            drawControls = true;
        });
        AREA_IP = new CSTextArea("IP Address", 151);
        AREA_PORT = new CSTextArea("Port", 51);
        BUTTON_JOIN = new CSButton("Join");
        BUTTON_JOIN.addActionListener(e -> {
            try {
                Client.ip = AREA_IP.getText();
                Client.port = Integer.parseInt(AREA_PORT.getText());
            } catch (NumberFormatException ignored) {}
        });
        BUTTON_XIT = new CSButton("Exit");
        BUTTON_XIT.addActionListener(e -> System.exit(0));
        addAll();

        System.setProperty("sun.java2d.opengl", "true");

        floorPolygons.add(new Floor(-18, 18, -16, -5, 0, Color.DARK_GRAY, base));
        floorPolygons.add(new Floor(-18, 18, 7, 16, 0, Color.DARK_GRAY, base));
        floorPolygons.add(new Floor(-18, -4, -5, 7, 0, Color.DARK_GRAY, base));
        floorPolygons.add(new Floor(4, 18, -5, 7, 0, Color.DARK_GRAY, base));
        new Box(-18, 18, -16, 16, 3, null, null, Color.DARK_GRAY, wallPolygons, base);

        wallPolygons.add(new Wall(-4, -4, -5, 7, -2, 2, Color.DARK_GRAY, pool));
        wallPolygons.add(new Wall(-4, 4, -5, -5, -2, 2, Color.DARK_GRAY, pool));
        wallPolygons.add(new Wall(-4, 4, 7, 7, -2, 2, Color.DARK_GRAY, pool));
        wallPolygons.add(new Wall(4, 4, -5, 7, -2, 2, Color.DARK_GRAY, pool));
        new Floor(-4, 4, -5, 7, -2, Color.DARK_GRAY, pool);
        new Floor(-4, 4, -5, 7, 0, new Color(0, 0, 225, 100), base);

        new Box(-1, 1, 11, 12, PLAYER_HEIGHT, Color.DARK_GRAY, floorPolygons, Color.GRAY, wallPolygons, drawablePolygons);
        new Box(-2, -1, 11, 12, EYE_HEIGHT + 0.01, Color.DARK_GRAY, floorPolygons, Color.GRAY, wallPolygons, drawablePolygons);
        new Box(1, 2, 11, 12, CROUCH_HEIGHT, Color.DARK_GRAY, floorPolygons, Color.GRAY, wallPolygons, drawablePolygons);

        new Box(-7, -2, -12, -11, 3, null, null, Color.GRAY, wallPolygons, drawablePolygons);
        new Box(2, 7, -12, -11, 3, null, null, Color.GRAY, wallPolygons, drawablePolygons);

        new Box(-14, -7, -12, -11, 3, null, null, Color.MAGENTA, wallPolygons, drawablePolygons);
        new Box(-14, -7, 11, 12, 3, null, null, Color.MAGENTA, wallPolygons, drawablePolygons);
        new Box(-14, -13, -7, 7, 3, null, null, Color.MAGENTA, wallPolygons, drawablePolygons);
        new Box(-8, -7, -5, 5, 3, null, null, Color.MAGENTA, wallPolygons, drawablePolygons);
        new Box(-8, -7, 9, 11, 3, null, null, Color.MAGENTA, wallPolygons, drawablePolygons);
        new Box(-8, -7, -11, -9, 3, null, null, Color.MAGENTA, wallPolygons, drawablePolygons);
        wallPolygons.add(new Wall(-13, -8, 0, 0, 0, 3, Color.MAGENTA, drawablePolygons));

        new Box(7, 14, -12, -11, 3, null, null, Color.BLUE, wallPolygons, drawablePolygons);
        new Box(7, 14, 11, 12, 3, null, null, Color.BLUE, wallPolygons, drawablePolygons);
        new Box(13, 14, -7, 7, 3, null, null, Color.BLUE, wallPolygons, drawablePolygons);
        new Box(7, 8, -5, 5, 3, null, null, Color.BLUE, wallPolygons, drawablePolygons);
        new Box(7, 8, 9, 11, 3, null, null, Color.BLUE, wallPolygons, drawablePolygons);
        new Box(7, 8, -11, -9, 3, null, null, Color.BLUE, wallPolygons, drawablePolygons);
        wallPolygons.add(new Wall(8, 13, 0, 0, 0, 3, Color.BLUE, drawablePolygons));

        for (int i = 1; i <= 4; i++) {
            PLAYERS[i] = new Player(drawablePolygons, i);
        }

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResource("counter-strike.ttf").openStream()));
            CONTROLS_IMAGE = ImageIO.read(ClassLoader.getSystemResource("controls.png"));
            CROSSHAIR = ImageIO.read(ClassLoader.getSystemResource("crosshair.png"));
            GUN_IMAGES[0] = ImageIO.read(ClassLoader.getSystemResource("mijia-pulse.png"));
            GUN_IMAGES[1] = ImageIO.read(ClassLoader.getSystemResource("water-pistol.png"));
            GUN_IMAGES[2] = ImageIO.read(ClassLoader.getSystemResource("pool-noodle.png"));
            KILL_IMAGE = ImageIO.read(ClassLoader.getSystemResource("kills.png"));
            DEATH_IMAGE = ImageIO.read(ClassLoader.getSystemResource("deaths.png"));
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        moveFocus();

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        lastTime = System.currentTimeMillis() / 1000.0;
        new Timer(1000 / FRAMES, this).start();
    }

    /**
     * Adds all Java Swing components to the panel
     */
    private void addAll() {
        add(javax.swing.Box.createVerticalGlue());
        add(BUTTON_CONTROLS);
        add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        add(AREA_IP);
        add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        add(AREA_PORT);
        add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        add(BUTTON_JOIN);
        add(javax.swing.Box.createRigidArea(new Dimension(0, 30)));
        add(BUTTON_XIT);
        add(javax.swing.Box.createVerticalGlue());
        revalidate();
    }

    /**
     * Paints the DPolygons and HUD as required; sorts the DPolygons by distance
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2d);
        g2d.setColor(new Color(189, 211, 218));
        g2d.fillRect(0, 0, ClientFrame.SCREEN_SIZE.width, ClientFrame.SCREEN_SIZE.height);
        for (DPolygon drawablePolygon : base) {
            drawablePolygon.subdivide();
        }
        for (DPolygon drawablePolygon : pool) {
            drawablePolygon.subdivide();
        }
        for (DPolygon drawablePolygon : drawablePolygons) {
            drawablePolygon.subdivide();
        }
        Collections.sort(base);
        Collections.sort(drawablePolygons);
        for (DPolygon polygon : pool) {
            polygon.draw(g2d);
        }
        for (DPolygon polygon : base) {
            polygon.draw(g2d);
        }
        for (DPolygon polygon : drawablePolygons) {
            polygon.draw(g2d);
        }
        if (inGame) {
            g2d.drawImage(CROSSHAIR, ClientFrame.SCREEN_SIZE.width / 2 - 7, ClientFrame.SCREEN_SIZE.height / 2 - 7, 14, 14, this);
            g2d.setColor(Color.WHITE);
            if (GUNS[gun].isReloading() && GUNS[gun].getReloadProgress(lastTime) < 1) {
                g2d.drawArc(ClientFrame.SCREEN_SIZE.width / 2 - 10, ClientFrame.SCREEN_SIZE.height / 2 - 10, 20, 20, 90, (int) (-360 * GUNS[gun].getReloadProgress(lastTime)));
            }
            g2d.setFont(new Font("Bahnschrift", Font.BOLD, 30));
            g2d.drawString(kills, ClientFrame.SCREEN_SIZE.width - g2d.getFontMetrics().stringWidth(kills) - 80, 37);
            g2d.drawImage(KILL_IMAGE, ClientFrame.SCREEN_SIZE.width - 138, 15, 23, 23, this);
            g2d.drawString(deaths, ClientFrame.SCREEN_SIZE.width - g2d.getFontMetrics().stringWidth(deaths) - 15, 37);
            g2d.drawImage(DEATH_IMAGE, ClientFrame.SCREEN_SIZE.width - 73, 15, 23, 23, this);
            g2d.drawString(health, 15, ClientFrame.SCREEN_SIZE.height - 15);
            g2d.drawString("" + GUNS[gun].getAmmo(lastTime), ClientFrame.SCREEN_SIZE.width - g2d.getFontMetrics().stringWidth("" + GUNS[gun].getAmmo(lastTime)) - 55, ClientFrame.SCREEN_SIZE.height - 15);
            g2d.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
            g2d.drawString("/", ClientFrame.SCREEN_SIZE.width - g2d.getFontMetrics().stringWidth("/ 30") - 15, ClientFrame.SCREEN_SIZE.height - 18);
            g2d.drawString("" + GUNS[gun].getSize(), ClientFrame.SCREEN_SIZE.width - g2d.getFontMetrics().stringWidth("" + GUNS[gun].getSize()) - 15, ClientFrame.SCREEN_SIZE.height - 18);
            g2d.setFont(new Font("Bahnschrift", Font.PLAIN, 10));
            g2d.drawString(GUNS[gun].getName(), ClientFrame.SCREEN_SIZE.width - g2d.getFontMetrics().stringWidth(GUNS[gun].getName()) - 15, GUN_TEXT_POSITION[gun]);
            g2d.drawImage(GUN_IMAGES[gun], ClientFrame.SCREEN_SIZE.width - 135, GUN_POSITION[gun], 120, 48, this);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            for (int i = 0; i < 3; i++) {
                g2d.drawImage(GUN_IMAGES[i], ClientFrame.SCREEN_SIZE.width - 135, GUN_POSITION[i], 120, 48, this);
            }
        } else if (drawControls) {
            g2d.drawImage(CONTROLS_IMAGE, 0, 0, ClientFrame.SCREEN_SIZE.width, ClientFrame.SCREEN_SIZE.height, this);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Offensive Regular All Caps", Font.PLAIN, 20));
            int width2 = g2d.getFontMetrics().stringWidth(" Guns");
            g2d.setFont(new Font("Offensive Regular All Caps", Font.PLAIN, 50));
            int width1 = g2d.getFontMetrics().stringWidth("CS:NO");
            g2d.drawString("CS:NO", ClientFrame.SCREEN_SIZE.width / 2 - (width1 + width2) / 2, 200);
            g2d.setFont(new Font("Offensive Regular All Caps", Font.PLAIN, 20));
            g2d.drawString(" Guns", ClientFrame.SCREEN_SIZE.width / 2 + (width1 - width2) / 2, 200);
        }
    }

    /**
     * Processes either a Timer event or a JButton event
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        deltaTime = System.currentTimeMillis() / 1000.0 - lastTime;
        lastTime = System.currentTimeMillis() / 1000.0;
        if (inGame) {
            removeAll();
            handleInput();
        } else {
            rotateHorz += 0.1 * deltaTime;
            moveFocus();
        }
        repaint();
    }

    /**
     * Moves the player based on WASD, SPACE, CTRL, SHIFT, gravity, and collision with objects
     */
    private void handleInput() {
        boolean touchingFloor = false;
        for (Floor floor : floorPolygons) {
            if (floor.below(camPos[0], camPos[1], camPos[2]) || floor.contains(camPos[0], camPos[1], camPos[2] - EYE_HEIGHT + velocityZ * deltaTime - crouchZ)) {
                touchingFloor = true;
                camPos[2] = floor.getZ() + EYE_HEIGHT + crouchZ;
                velocityZ = 0;
                if (keyPressed[KeyEvent.VK_SPACE] && crouchZ == 0) {
                    velocityZ += 293.939 / 36;
                }
                break;
            }
        }
        double moveX = 0;
        double moveY = 0;
        Vector dirFocus = new Vector(camFocus[0] - camPos[0], camFocus[1] - camPos[1], 0, true);
        if (keyPressed[KeyEvent.VK_W]) {
            moveX += dirFocus.getX();
            moveY += dirFocus.getY();
        }
        if (keyPressed[KeyEvent.VK_S]) {
            moveX -= dirFocus.getX();
            moveY -= dirFocus.getY();
        }
        Vector dirSide = new Vector(camFocus[1] - camPos[1], camPos[0] - camFocus[0], 0, true);
        if (keyPressed[KeyEvent.VK_A]) {
            moveX += dirSide.getX();
            moveY += dirSide.getY();
        }
        if (keyPressed[KeyEvent.VK_D]) {
            moveX -= dirSide.getX();
            moveY -= dirSide.getY();
        }
        Vector moveVector = new Vector(moveX, moveY, 0, true);
        if (crouchZ != 0) {
            camPos[0] += MOVEMENT_DEBUFF[gun] * moveVector.getX() * MOVE_SPEED * deltaTime * 85 / 250;
            escapeWall(moveVector.getX(), 0);
            camPos[1] += MOVEMENT_DEBUFF[gun] * moveVector.getY() * MOVE_SPEED * deltaTime * 85 / 250;
            escapeWall(0, moveVector.getY());
        } else if (keyPressed[KeyEvent.VK_SHIFT]) {
            camPos[0] += MOVEMENT_DEBUFF[gun] * moveVector.getX() * MOVE_SPEED * deltaTime * 130 / 250;
            escapeWall(moveVector.getX(), 0);
            camPos[1] += MOVEMENT_DEBUFF[gun] * moveVector.getY() * MOVE_SPEED * deltaTime * 130 / 250;
            escapeWall(0, moveVector.getY());
        } else {
            camPos[0] += MOVEMENT_DEBUFF[gun] * moveVector.getX() * MOVE_SPEED * deltaTime;
            escapeWall(moveVector.getX(), 0);
            camPos[1] += MOVEMENT_DEBUFF[gun] * moveVector.getY() * MOVE_SPEED * deltaTime;
            escapeWall(0, moveVector.getY());
        }
        if (velocityZ != 0 || !touchingFloor) {
            velocityZ -= (800.0 / 36) * deltaTime;
            camPos[2] += velocityZ * deltaTime;
        }
        if (mousePressed) {
            GUNS[gun].shoot(drawablePolygons, EVENTS, lastTime, moveX != 0 || moveY != 0, velocityZ != 0);
        }
        if (camPos[2] < 0.5) {
            respawn();
            EVENTS.add("WET");
        }
        moveFocus();
    }

    /**
     * Checks collision with wall; moves out of wall if colliding with it
     * @param dx the magnitude of movement in the X-direction
     * @param dy the magnitude of movement in the Y-direction
     */
    private void escapeWall(double dx, double dy) {
        for (Wall wall : wallPolygons) {
            while (wall.contains(camPos[0], camPos[1], camPos[2] - EYE_HEIGHT + velocityZ * deltaTime - crouchZ)) {
                camPos[0] -= Math.signum(dx) * 0.001;
                camPos[1] -= Math.signum(dy) * 0.001;
            }
        }
    }

    /**
     * Sets the player's position at a random location and reloads their guns
     */
    public static void respawn() {
        GUNS[0].instantReload();
        GUNS[1].instantReload();
        spawnProtection = true;
        switch ((int) (Math.random() * 7)) {
            case 0:
                camPos[0] = -16;
                camPos[1] = 14;
                rotateHorz = -Math.PI / 1.999;
                break;
            case 1:
                camPos[0] = 16;
                camPos[1] = 14;
                rotateHorz = -Math.PI / 1.999;
                break;
            case 2:
                camPos[0] = -10.5;
                camPos[1] = -1.5;
                rotateHorz = -Math.PI / 1.999;
                break;
            case 3:
                camPos[0] = 10.5;
                camPos[1] = -1.5;
                rotateHorz = -Math.PI / 1.999;
                break;
            case 4:
                camPos[0] = -10.5;
                camPos[1] = 1.5;
                rotateHorz = Math.PI / 1.999;
                break;
            case 5:
                camPos[0] = 10.5;
                camPos[1] = 1.5;
                rotateHorz = Math.PI / 1.999;
                break;
            case 6:
                camPos[0] = 10.5;
                camPos[1] = -14;
                rotateHorz = Math.PI;
                break;
        }
        camPos[2] = 0;
        rotateVert = 0.001;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Processes keys entered by the player, modifies {@code keyPressed}
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
        spawnProtection = false;
        if (e.getKeyCode() == KeyEvent.VK_CONTROL && crouchZ == 0) {
            crouchZ = -PLAYER_HEIGHT * 9 / 32;
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            GUNS[gun].reload(System.currentTimeMillis() / 1000.0);
        } else if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_3) {
            GUNS[gun].endReload();
            gun = e.getKeyCode() - '1';
            GUNS[gun].reset();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            camPos = new double[]{ 0, 0, 4.5 };
            rotateVert = -0.2;
            inGame = false;
            Client.ip = null;
            Client.port = -1;
            drawControls = false;
            removeAll();
            addAll();
        }
    }

    /**
     * Processes keys entered by the player, modifies {@code keyPressed}
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            crouchZ = 0;
        }
    }

    /**
     * Changes {@code camFocus} based on the mouse position
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        moveMouse(e.getX(), e.getY());
    }

    /**
     * Changes {@code camFocus} based on the mouse position
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        moveMouse(e.getX(), e.getY());
    }

    /**
     * Changes {@code rotateHorz} and {@code rotateVert} based on the mouse position
     * @param xPos the x-position of the mouse
     * @param yPos the y-position of the mouse
     */
    private void moveMouse(int xPos, int yPos) {
        if (inGame) {
            double dx = xPos - ClientFrame.SCREEN_SIZE.width / 2;
            double dy = yPos - ClientFrame.SCREEN_SIZE.height / 2;
            rotateHorz += dx * ROTATION_SPEED;
            rotateVert -= dy * ROTATION_SPEED * (1 - Math.abs(rotateVert) * 0.5);
            if (rotateVert > 0.999999) {
                rotateVert = 0.999999;
            }
            if (rotateVert < -0.999999) {
                rotateVert = -0.999999;
            }
            moveFocus();
            centerMouse();
        }
    }

    /**
     * Calculates the position of the new {@code camFocus} based on {@code rotateHorz} and {@code rotateVert}
     */
    private void moveFocus() {
        double r = Math.sqrt(1 - rotateVert * rotateVert);
        camFocus[0] = camPos[0] + r * Math.cos(rotateHorz);
        camFocus[1] = camPos[1] + r * Math.sin(rotateHorz);
        camFocus[2] = camPos[2] + rotateVert;
    }

    /**
     * Centres the mouse in the middle of the screen
     */
    public static void centerMouse() {
        try {
            Robot robot = new Robot();
            robot.mouseMove(ClientFrame.SCREEN_SIZE.width / 2, ClientFrame.SCREEN_SIZE.height / 2);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Toggles {@code mousePressed}
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    /**
     * Toggles {@code mousePressed}
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
