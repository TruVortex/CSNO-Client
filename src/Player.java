import java.awt.*;
import java.util.ArrayList;

public class Player {

    private final double RADIUS = ClientPanel.PLAYER_RADIUS;
    private final DPolygon[] POLYGONS = new DPolygon[13];

    /**
     * Initializes a player object with the specified id
     * @param polygons the polygons to be drawn
     * @param id the id of the player
     */
    public Player(ArrayList<DPolygon> polygons, int id) {
        for (int i = 0; i < 13; i++) {
            POLYGONS[i] = new DPolygon(new double[]{ -1, -1, -1, -1 }, new double[]{ -1, -1, -1, -1 }, new double[]{ -1, -1, -1, -1 }, new Color(255, 0, 0, 100));
            POLYGONS[i].setID(i < 9 ? id : -id);
            polygons.add(POLYGONS[i]);
        }
    }

    /**
     * Updates the coordinates of the player to ({@code x},{@code y},{@code z})
     * @param x the X-coordinate of the player
     * @param y the Y-coordinate of the player
     * @param z the Z-coordinate of the player
     * @param crouchZ the crouch modifier of the player
     * @param spawnProtection if the player has spawn protection or not
     */
    public void update(double x, double y, double z, double crouchZ, boolean spawnProtection) {
        double startX = x - RADIUS;
        double endX = x + RADIUS;
        double startY = y - RADIUS;
        double endY = y + RADIUS;
        double torso = ClientPanel.PLAYER_HEIGHT * 5 / 6 + crouchZ;
        double startXH = x - RADIUS / 3;
        double endXH = x + RADIUS / 3;
        double startYH = y - RADIUS / 3;
        double endYH = y + RADIUS / 3;
        double head = ClientPanel.PLAYER_HEIGHT + crouchZ;
        POLYGONS[0].update(new double[]{ startX, startX, endX, endX }, new double[]{ startY, endY, endY, startY }, new double[]{ torso, torso, torso, torso }, spawnProtection);
        POLYGONS[1].update(new double[]{ startX, startX, startX, startX }, new double[]{ startY, endY, endY, startY }, new double[]{ z, z, z + torso / 2, z + torso / 2 }, spawnProtection);
        POLYGONS[2].update(new double[]{ startX, startX, startX, startX }, new double[]{ startY, endY, endY, startY }, new double[]{ z + torso / 2, z + torso / 2, z + torso, z + torso }, spawnProtection);
        POLYGONS[3].update(new double[]{ startX, endX, endX, startX }, new double[]{ startY, startY, startY, startY }, new double[]{ z, z, z + torso / 2, z + torso / 2 }, spawnProtection);
        POLYGONS[4].update(new double[]{ startX, endX, endX, startX }, new double[]{ startY, startY, startY, startY }, new double[]{ z + torso / 2, z + torso / 2, z + torso, z + torso }, spawnProtection);
        POLYGONS[5].update(new double[]{ startX, endX, endX, startX }, new double[]{ endY, endY, endY, endY }, new double[]{ z, z, z + torso / 2, z + torso / 2 }, spawnProtection);
        POLYGONS[6].update(new double[]{ startX, endX, endX, startX }, new double[]{ endY, endY, endY, endY }, new double[]{ z + torso / 2, z + torso / 2, z + torso, z + torso }, spawnProtection);
        POLYGONS[7].update(new double[]{ endX, endX, endX, endX }, new double[]{ startY, endY, endY, startY }, new double[]{ z, z, z + torso / 2, z + torso / 2 }, spawnProtection);
        POLYGONS[8].update(new double[]{ endX, endX, endX, endX }, new double[]{ startY, endY, endY, startY }, new double[]{ z + torso / 2, z + torso / 2, z + torso, z + torso }, spawnProtection);
        POLYGONS[9].update(new double[]{ startXH, startXH, startXH, startXH }, new double[]{ startYH, endYH, endYH, startYH }, new double[]{ z + torso, z + torso, z + head, z + head }, spawnProtection);
        POLYGONS[10].update(new double[]{ startXH, endXH, endXH, startXH }, new double[]{ startYH, startYH, startYH, startYH }, new double[]{ z + torso, z + torso, z + head, z + head }, spawnProtection);
        POLYGONS[11].update(new double[]{ startXH, endXH, endXH, startXH }, new double[]{ endYH, endYH, endYH, endYH }, new double[]{ z + torso, z + torso, z + head, z + head }, spawnProtection);
        POLYGONS[12].update(new double[]{ endXH, endXH, endXH, endXH }, new double[]{ startYH, endYH, endYH, startYH }, new double[]{ z + torso, z + torso, z + head, z + head }, spawnProtection);
    }
}
