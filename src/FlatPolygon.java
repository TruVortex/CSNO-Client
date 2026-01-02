import java.awt.*;

public class FlatPolygon {

    private final Polygon POLYGON;

    public FlatPolygon(double[] x, double[] y) {
        POLYGON = new Polygon();
        for (int i = 0; i < x.length; i++) {
            POLYGON.addPoint((int) x[i], (int) y[i]);
        }
    }

    public void draw(Graphics g) {
        g.fillPolygon(POLYGON);
    }

    public boolean contains(double x, double y) {
        return POLYGON.contains(x, y);
    }
}
