package spotlight.spotlight.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Vector3D {
    public static final Vector3D ORIGIN = new Vector3D(0.0, 0.0, 0.0);
    public final double x;
    public final double y;
    public final double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Location location) {
        this(location.toVector());
    }

    public Vector3D(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector cannot be NULL.");
        } else {
            this.x = vector.getX();
            this.y = vector.getY();
            this.z = vector.getZ();
        }
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public Vector3D add(Vector3D other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be NULL");
        } else {
            return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
        }
    }

    public Vector3D add(double x, double y, double z) {
        return new Vector3D(this.x + x, this.y + y, this.z + z);
    }

    public Vector3D subtract(Vector3D other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be NULL");
        } else {
            return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
        }
    }

    public Vector3D subtract(double x, double y, double z) {
        return new Vector3D(this.x - x, this.y - y, this.z - z);
    }

    public Vector3D multiply(int factor) {
        return new Vector3D(this.x * (double)factor, this.y * (double)factor, this.z * (double)factor);
    }

    public Vector3D multiply(double factor) {
        return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector3D divide(int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Cannot divide by null.");
        } else {
            return new Vector3D(this.x / (double)divisor, this.y / (double)divisor, this.z / (double)divisor);
        }
    }

    public Vector3D divide(double divisor) {
        if (divisor == 0.0) {
            throw new IllegalArgumentException("Cannot divide by null.");
        } else {
            return new Vector3D(this.x / divisor, this.y / divisor, this.z / divisor);
        }
    }

    public Vector3D abs() {
        return new Vector3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    public String toString() {
        return String.format("[x: %s, y: %s, z: %s]", this.x, this.y, this.z);
    }

    public static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        double epsilon = 9.999999747378752E-5;
        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
        if (Math.abs(c.x) > e.x + ad.x) {
            return false;
        } else if (Math.abs(c.y) > e.y + ad.y) {
            return false;
        } else if (Math.abs(c.z) > e.z + ad.z) {
            return false;
        } else if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + 9.999999747378752E-5) {
            return false;
        } else if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + 9.999999747378752E-5) {
            return false;
        } else {
            return !(Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + 9.999999747378752E-5);
        }
    }

    public static Vector getDirectionBetweenLocations(Vector Start, Vector End) {
        return End.subtract(Start);
    }
}
