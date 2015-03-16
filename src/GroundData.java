import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GroundData {
	
	private double[] heightMap = null;
	private int height;
	
	public GroundData(int width, int height) {
		setSize(width, height);
	}
	
	public void setSize(int width, int height) {
		this.heightMap = new double[width];
		this.height = height;
	}
	
	private boolean inBounds(double x) {
		return x >= 0.0D && x < (double) heightMap.length;
	}
	
	private void increment(int x, double by) {
		heightMap[x] += by;
	}
	
	private void decrement(int x, double by) {
		heightMap[x] -= by;
	}
	
	public int collidesAt(double x, double y) {
		return inBounds(x) ? (y >= (height - heightMap[(int) x]) ? 1 : 0) : (y >= this.height ? 2 : 0);
	}
	
	public void smoothGround() {
		double minDif = 0.5D;
		int adjacent;
		for (int curr = 0; curr < heightMap.length; curr++) {
			adjacent = curr + 1;
			if (adjacent < heightMap.length) {
				if (heightMap[curr] > heightMap[adjacent]) {
					double dif = Math.abs(heightMap[adjacent] - heightMap[curr]) / 1.5D;
					if (dif >= minDif) {
						decrement(curr, dif);
						increment(adjacent, dif);
					}
				}
			}
			adjacent = curr - 1;
			if (adjacent >= 0) {
				if (heightMap[curr] > heightMap[adjacent]) {
					double dif = Math.abs(heightMap[adjacent] - heightMap[curr]) / 1.5D;
					if (dif >= minDif) {
						decrement(curr, dif);
						increment(adjacent, dif);
					}
				}
			}
		}
	}
	
	public void add(Flake flake) {
		double flakeRadius = flake.getWidth() / 2.0D;
		double flakeCenter = flake.x + flakeRadius;
		double flakeFloor = Math.max(0.0D, Math.floor(flake.x));
		int max = Math.min(heightMap.length - 1, (int) Math.floor(flake.x + flake.width));
		for (int i = (int) flakeFloor; i <= max; i++) {
			double x = (double) i - flakeCenter;
			increment(i, flake.area(x, x + 1.0D));
		}
	}
	
	public void render(Graphics2D g) {
		for (int i = 0; i < heightMap.length; i++) {
			g.fill(new Rectangle2D.Double(i, height - heightMap[i], 1, heightMap[i]));
		}
	}
	
}
