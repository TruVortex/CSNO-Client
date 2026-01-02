import java.awt.*;
import java.util.ArrayList;

public class Floor {

    private final double RADIUS = ClientPanel.PLAYER_RADIUS;
    private final double START_X;
    private final double END_X;
    private final double START_Y;
    private final double END_Y;
    private final double Z;

    /**
     * Constructs a floor with the specified starting and ending points and colour and adds it to be drawn
     * @param startX the starting X-coordinate of the floor
     * @param endX the ending X-coordinate of the floor
     * @param startY the starting Y-coordinate of the floor
     * @param endY the ending Y-coordinate of the floor
     * @param z the Z-coordinate of the floor
     * @param colour the colour of the floor
     * @param polygons the polygons to be drawn
     */
    public Floor(int startX, int endX, int startY, int endY, double z, Color colour, ArrayList<DPolygon> polygons) {
        START_X = startX;
        END_X = endX;
        START_Y = startY;
        END_Y = endY;
        Z = z;
        for (double x = startX; x < endX; x++) {
            for (double y = startY; y < endY; y++) {
                polygons.add(new DPolygon(new double[]{ x, x, x + 1, x + 1 }, new double[]{ y, y + 1, y + 1, y }, new double[]{ z, z, z, z }, colour));
            }
        }
    }

    /**
     * Returns if the player intersects with a rectangle projecting above and below the Z-coordinate of the floor
     * @param x the X-coordinate of the player
     * @param y the Y-coordinate of the player
     * @return if the player intersects with a rectangle projecting above and below the Z-coordinate of the floor
     */
    public boolean encloses(double x, double y) {
        return x >= START_X - RADIUS && x <= END_X + RADIUS && y >= START_Y - RADIUS && y <= END_Y + RADIUS;
    }

    /**
     * Returns if the player is standing on the floor
     * @param x the X-coordinate of the player
     * @param y the Y-coordinate of the player
     * @param z the Z-coordinate of the player
     * @return if the player is standing on the floor
     */
    public boolean below(double x, double y, double z) {
        return encloses(x, y) && Math.abs(z - ClientPanel.EYE_HEIGHT - this.Z) <= 0.0000001;
    }

    /**
     * Returns if the player will phase through the floor
     * @param x the X-coordinate of the player
     * @param y the Y-coordinate of the player
     * @param z the Z-coordinate of the player
     * @return if the player will phase through the floor
     */
    public boolean contains(double x, double y, double z) {
        return encloses(x, y) && z <= this.Z && z + 2 >= this.Z;
    }

    public double getZ() {
        return Z;
    }
}
