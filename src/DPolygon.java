import java.awt.*;

public class DPolygon implements Comparable<DPolygon> {

    private double[] x;
    private double[] y;
    private double[] z;
    private double shift;
    private double drawX;
    private double drawY;
    private boolean drawn;
    private double dist;
    private FlatPolygon drawablePolygon;
    private Color colour;
    private int ID;

    /**
     * Creates a 3D-polygon with the specified points and colour
     * @param x the X-coordinates of the points
     * @param y the Y-coordinates of the points
     * @param z the Z-coordinates of the points
     * @param colour the colour of the polygon
     */
    public DPolygon(double[] x, double[] y, double[] z, Color colour) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.colour = colour;
    }

    /**
     * Generates the points of the polygon to be drawn on the screen
     */
    public void subdivide() {
        double total = 0;
        drawn = true;
        project(ClientPanel.camFocus[0], ClientPanel.camFocus[1], ClientPanel.camFocus[2]);
        double offsetX = 1000 * drawX;
        double offsetY = 1000 * drawY;
        double[] newX = new double[x.length];
        double[] newY = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            project(x[i], y[i], z[i]);
            if (shift < 0) {
                drawn = false;
                break;
            }
            newX[i] = ClientFrame.SCREEN_SIZE.width / 2 - offsetX + 1000 * drawX;
            newY[i] = ClientFrame.SCREEN_SIZE.height / 2 - offsetY + 1000 * drawY;
            total += Math.sqrt(
                (ClientPanel.camPos[0] - x[i]) * (ClientPanel.camPos[0] - x[i]) +
                (ClientPanel.camPos[1] - y[i]) * (ClientPanel.camPos[1] - y[i]) +
                (ClientPanel.camPos[2] - z[i]) * (ClientPanel.camPos[2] - z[i])
            );
        }
        dist = total / x.length;
        drawablePolygon = new FlatPolygon(newX, newY);
    }

    /**
     * Calculates the X-position and Y-position of a 3D point on screen by projecting it onto a plane normal the direction the player is facing
     * @param x the X-coordinate of the point
     * @param y the Y-coordinate of the point
     * @param z the z-coordinate of the point
     */
    private void project(double x, double y, double z) {
        double[] camPos = ClientPanel.camPos;
        double[] camFocus = ClientPanel.camFocus;
        Vector dirFocus = new Vector(camFocus[0] - camPos[0], camFocus[1] - camPos[1], camFocus[2] - camPos[2], true);
        Vector dirPoint = new Vector(x - camPos[0], y - camPos[1], z - camPos[2], true);
        shift = new Vector(camFocus[0] - camPos[0], camFocus[1] - camPos[1], camFocus[2] - camPos[2], false).dot(dirFocus) / dirPoint.dot(dirFocus);
        Vector point = new Vector(camPos[0] + dirPoint.getX() * shift, camPos[1] + dirPoint.getY() * shift, camPos[2] + dirPoint.getZ() * shift, false);
        Vector orthogonalX = new Vector(camPos[1] - camFocus[1], camFocus[0] - camPos[0], 0, true);
        Vector orthogonalY = orthogonalX.cross(dirFocus);
        drawX = point.dot(orthogonalX);
        drawY = point.dot(orthogonalY);
    }

    public void draw(Graphics g) {
        if (drawn) {
            g.setColor(colour);
            drawablePolygon.draw(g);
        }
    }

    /**
     * Updates the coordinates of the points of this polygon
     * @param x the X-coordinates of the points
     * @param y the Y-coordinates of the points
     * @param z the Z-coordinates of the points
     * @param spawnProtection if the player has spawn protection or not
     */
    public void update(double[] x, double[] y, double[] z, boolean spawnProtection) {
        this.x = x;
        this.y = y;
        this.z = z;
        if (spawnProtection) {
            colour = new Color(255, 0, 0, 100);
        } else {
            colour = Color.RED;
        }
    }

    public boolean contains(double x, double y) {
        return drawn && drawablePolygon.contains(x, y);
    }

    public boolean isPlayer() {
        return colour.equals(Color.RED);
    }

    /**
     * Returns if the polygon is part of a player who has spawn protection
     * @return if the polygon is part of a player who has spawn protection
     */
    public boolean isGhost() {
        return colour.equals(new Color(255, 0, 0, 100));
    }

    public boolean isDrawn() {
        return drawn;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    public double getZ() {
        return z[0];
    }

    public double getDist() {
        return dist;
    }

    @Override
    public int compareTo(DPolygon o) {
        return Double.compare(o.dist, dist);
    }
}
