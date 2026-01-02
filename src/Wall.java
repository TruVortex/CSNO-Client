import java.awt.*;
import java.util.ArrayList;

public class Wall {

    private final double RADIUS = ClientPanel.PLAYER_RADIUS;
    private final int START_X;
    private final int END_X;
    private final int START_Y;
    private final int END_Y;
    private final int OFFSET;
    private final double HEIGHT;

    /**
     * Constructs a wall with the specified starting and ending points and colour and adds it to be drawn
     * @param startX the starting X-coordinate of the wall
     * @param endX the ending X-coordinate of the wall
     * @param startY the starting Y-coordinate of the wall
     * @param endY the ending Y-coordinate of the wall
     * @param offset the height offset of the wall
     * @param height the height of the wall
     * @param colour the colour of the wall
     * @param polygons the polygons to be drawn
     */
    public Wall(int startX, int endX, int startY, int endY, int offset, double height, Color colour, ArrayList<DPolygon> polygons) {
        START_X = startX;
        END_X = endX;
        START_Y = startY;
        END_Y = endY;
        OFFSET = offset;
        HEIGHT = height;
        if (startX == endX) {
            for (double y = startY; y < endY - 1; y += 2) {
                for (int h = offset; h < Math.floor(height) + offset; h++) {
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ y, y + 0.67, y + 0.67, y }, new double[]{ h, h, h + 1, h + 1 }, colour));
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ y + 0.67, y + 1.34, y + 1.34, y + 0.67 }, new double[]{ h, h, h + 1, h + 1 }, colour));
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ y + 1.34, y + 2, y + 2, y + 1.34 }, new double[]{ h, h, h + 1, h + 1 }, colour));
                }
                if (Math.floor(height) != height) {
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ y, y + 0.67, y + 0.67, y }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ y + 0.67, y + 1.34, y + 1.34, y + 0.67 }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ y + 1.34, y + 2, y + 2, y + 1.34 }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                }
            }
            if ((endY - startY) % 2 != 0) {
                for (int h = 0; h < Math.floor(height); h++) {
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ endY - 1, endY - 0.5, endY - 0.5, endY - 1 }, new double[]{ h, h, h + 1, h + 1 }, colour));
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ endY - 0.5, endY, endY, endY - 0.5 }, new double[]{ h, h, h + 1, h + 1 }, colour));
                }
                if (Math.floor(height) != height) {
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ endY - 1, endY - 0.5, endY - 0.5, endY - 1 }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                    polygons.add(new DPolygon(new double[]{ startX, startX, startX, startX }, new double[]{ endY - 0.5, endY, endY, endY - 0.5 }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                }
            }
        } else {
            for (double x = startX; x < endX - 1; x += 2) {
                for (int h = offset; h < Math.floor(height) + offset; h++) {
                    polygons.add(new DPolygon(new double[]{ x, x + 0.67, x + 0.67, x }, new double[]{ startY, startY, startY, startY }, new double[]{ h, h, h + 1, h + 1 }, colour));
                    polygons.add(new DPolygon(new double[]{ x + 0.67, x + 1.34, x + 1.34, x + 0.67 }, new double[]{ startY, startY, startY, startY }, new double[]{ h, h, h + 1, h + 1 }, colour));
                    polygons.add(new DPolygon(new double[]{ x + 1.34, x + 2, x + 2, x + 1.34 }, new double[]{ startY, startY, startY, startY }, new double[]{ h, h, h + 1, h + 1 }, colour));
                }
                if (Math.floor(height) != height) {
                    polygons.add(new DPolygon(new double[]{ x, x + 0.67, x + 0.67, x }, new double[]{ startY, startY, startY, startY }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                    polygons.add(new DPolygon(new double[]{ x + 0.67, x + 1.34, x + 1.34, x + 0.67 }, new double[]{ startY, startY, startY, startY }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                    polygons.add(new DPolygon(new double[]{ x + 1.34, x + 2, x + 2, x + 1.34 }, new double[]{ startY, startY, startY, startY }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                }
            }
            if ((endX - startX) % 2 != 0) {
                for (int h = 0; h < Math.floor(height); h++) {
                    polygons.add(new DPolygon(new double[]{ endX - 1, endX - 0.5, endX - 0.5, endX - 1 }, new double[]{ startY, startY, startY, startY }, new double[]{ h, h, h + 1, h + 1 }, colour));
                    polygons.add(new DPolygon(new double[]{ endX - 0.5, endX, endX, endX - 0.5 }, new double[]{ startY, startY, startY, startY }, new double[]{ h, h, h + 1, h + 1 }, colour));
                }
                if (Math.floor(height) != height) {
                    polygons.add(new DPolygon(new double[]{ endX - 1, endX - 0.5, endX - 0.5, endX - 1 }, new double[]{ startY, startY, startY, startY }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                    polygons.add(new DPolygon(new double[]{ endX - 0.5, endX, endX, endX - 0.5 }, new double[]{ startY, startY, startY, startY }, new double[]{ Math.floor(height), Math.floor(height), height, height }, colour));
                }
            }
        }
    }

    /**
     * Returns if a player at the specified coordinates is in the wall or not
     * @param x the X-position of the player
     * @param y the Y-position of the player
     * @param z the Z-position of the player
     * @return if a player at the specified coordinates is in the wall or not
     */
    public boolean contains(double x, double y, double z) {
        return x >= START_X - RADIUS && x <= END_X + RADIUS && y >= START_Y - RADIUS && y <= END_Y + RADIUS && z < OFFSET + HEIGHT - 0.0000001;
    }
}
