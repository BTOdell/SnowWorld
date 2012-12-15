import java.awt.geom.Ellipse2D;

public class Flake extends Ellipse2D.Double {
	
	private static final long serialVersionUID = 1L;
	
	//private static final double MIN = 1.0D, MAX = 4.0D;
	private static final double MIN = 1.0D, MAX = 4.0D;
	
	double vel;
	
	public Flake(double radius) {
		this(0, 0, radius);
	}
	
	public Flake(double x, double y, double radius) {
		super(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	public Flake(int sceneWidth) {
		super();
		double dia = Methods.random(MIN, MAX);
		setFrame(Methods.random(-600.0D, ((double) sceneWidth) + 600.0D), 0D, dia, dia);
		vel = Methods.random(2.0D, 5.0D);
	}
	
	public void tick() {
		y += vel;
	}
	
	public double area(double x1, double x2) {
		double radius = width / 2.0D;
		if (x1 > x2) {
			double temp = x1;
			x1 = x2;
			x2 = temp;
		}
		x1 = Math.min(radius, Math.max(-radius, x1));
		x2 = Math.min(radius, Math.max(-radius, x2));
		return integral(x2, radius) - integral(x1, radius);
	}
	
	private static double integral(double x, double radius) {
		double rM = radius * radius;
		double sqrt = Math.sqrt(rM - x * x);
		return (x * sqrt) + (rM * Math.atan(x / sqrt));
	}
	
}