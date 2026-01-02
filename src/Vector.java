public class Vector {

    private double X;
    private double Y;
    private double Z;

    /**
     * Creates a vector with the specified X, Y, and Z magnitudes
     * @param x the X-magnitude
     * @param y the Y-magnitude
     * @param z the Z-magnitude
     * @param normalize whether to normalize the vector or not
     */
    public Vector(double x, double y, double z, boolean normalize) {
        if (normalize) {
            double magnitude = Math.sqrt(x * x + y * y + z * z);
            if (magnitude > 0) {
                X = x / magnitude;
                Y = y / magnitude;
                Z = z / magnitude;
            }
        } else {
            X = x;
            Y = y;
            Z = z;
        }
    }

    /**
     * Returns the normalized cross product vector of this vector and v
     * @param v the vector to be crossed with
     * @return the normalized cross product vector of this vector and v
     */
    public Vector cross(Vector v) {
        return new Vector(Y * v.Z - Z * v.Y, Z * v.X - X * v.Z, X * v.Y - Y * v.X, true);
    }

    /**
     * Returns the dot product of this vector and v
     * @param v the vector to be dotted with
     * @return the dot product of this vector and v
     */
    public double dot(Vector v) {
        return X * v.X + Y * v.Y + Z * v.Z;
    }

    /**
     * Returns the magnitude of this vector
     * @return the magnitude of this vector
     */
    public double magnitude() {
        return Math.sqrt(X * X + Y * Y + Z * Z);
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getZ() {
        return Z;
    }
}
