import java.awt.geom.Ellipse2D;

public class Flake extends Ellipse2D.Double {
	
	private static final long serialVersionUID = 1L;
	
	private static final double MIN = 1.0D, MAX = 4.0D;
	
	double vel;
	
	public Flake(int sceneWidth) {
		super();
		double size = Methods.random(MIN, MAX);
		setFrame(Methods.random(-600.0D, ((double) sceneWidth) + 600.0D), 0D, size, size);
		vel = Methods.random(2.0D, 5.0D);
	}
	
	public void tick() {
		y += vel;
	}
	
}