import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GroundData {
	
	private double[] heightMap = null;
	
	public GroundData(int width) {
		setSize(width);
	}
	
	public void setSize(int width) {
		this.heightMap = new double[width];
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
		return inBounds(x) ? (y >= heightMap[(int) x] ? 1 : 0) : (y >= getHeight() ? 2 : 0);
	}
	
	public void smoothGround() {
		double minDif = 0.5D;
		int adjacent;
		for (int curr = 0; curr < heightMap.length; curr++) {
			adjacent = curr + 1;
			if (adjacent < heightMap.length) {
				if (heightMap[curr].height > heightMap[adjacent].height) {
					double dif = Math.abs(heightMap[adjacent].height - heightMap[curr].height) / 1.5D;
					if (dif >= minDif) {
						decrement(curr, dif);
						increment(adjacent, dif);
					}
				}
			}
			adjacent = curr - 1;
			if (adjacent >= 0) {
				if (heightMap[curr].height > heightMap[adjacent].height) {
					double dif = Math.abs(heightMap[adjacent].height - heightMap[curr].height) / 1.5D;
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
			g.fill(heightMap[i]);
		}
	}
	
}
