package snowworld;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Flake implements Tickable, Renderable {
	
	//private static final double MIN = 1.0D, MAX = 4.0D;
	private static final double MIN = 1.0D, MAX = 4.0D;
	
	private final Ellipse2D.Double shape;
	
	private double vel;
	
	public Flake(double radius) {
		this(0, 0, radius);
	}
	
	public Flake(double x, double y, double radius) {
		this.shape = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
		this.vel = Methods.random(2.0D, 5.0D);
	}
	
	public Flake(int sceneWidth) {
		double dia = Methods.random(MIN, MAX);
		this.shape = new Ellipse2D.Double(Methods.random(-600.0D, ((double) sceneWidth) + 600.0D), 0D, dia, dia);
		this.vel = Methods.random(2.0D, 5.0D);
	}
	
	public double getX() {
		return this.shape.x;
	}
	
	public void setX(double x) {
		this.shape.x = x;
	}
	
	public double getY() {
		return this.shape.y;
	}
	
	public double getWidth() {
		return this.shape.width;
	}
	
	public void tick() {
		this.shape.y += vel;
	}
	
	public double area(double x1, double x2) {
		double radius = this.shape.width / 2.0D;
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

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(long currentTime, long deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
	}
	
}