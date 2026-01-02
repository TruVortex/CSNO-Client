import java.awt.*;
import java.util.ArrayList;

public class Box {
    /**
     * Constructs a box with the specified parameters
     * @param startX the starting X-coordinate of the box
     * @param endX the ending X-coordinate of the box
     * @param startY the starting Y-coordinate of the box
     * @param endY the ending Y-coordinate of the box
     * @param height the height of the box
     * @param floorColour the colour of the box's floor
     * @param floors the floors to check collision with
     * @param wallColour the colour of the box's walls
     * @param walls the walls to check collision with
     * @param polygons the polygons to be drawn
     */
    public Box(int startX, int endX, int startY, int endY, double height, Color floorColour, ArrayList<Floor> floors, Color wallColour, ArrayList<Wall> walls, ArrayList<DPolygon> polygons) {
        if (floorColour != null) {
            floors.add(new Floor(startX, endX, startY, endY, height, floorColour, polygons));
        }
        walls.add(new Wall(startX, endX, startY, startY, 0, height, wallColour, polygons));
        walls.add(new Wall(startX, endX, endY, endY, 0, height, wallColour, polygons));
        walls.add(new Wall(startX, startX, startY, endY, 0, height, wallColour, polygons));
        walls.add(new Wall(endX, endX, startY, endY, 0, height, wallColour, polygons));
    }
}
