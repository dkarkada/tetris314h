package assignment;

public class Pivot {
	double x;
	double y;
	public Pivot(double xcoord, double ycoord) {
		x = xcoord;
		y = ycoord;
	}
	public double getX() {return x;}
	public double getY() {return y;}
	public boolean equals(Object o) {
		if (o instanceof Pivot) {
			Pivot p = (Pivot) o;
			return p.x == x && p.y == y;
		}
		return false;
	}
}
