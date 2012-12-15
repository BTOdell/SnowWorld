import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

public class Environment extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int DELAY = 30;
	
	private World world;
	private LinkedList<Flake> flakes;
	private GroundData ground;
	private Wind wind;
	public Rectangle screen;
	int flakeMake = 5;
	
	private Thread windThread, flakeThread;
	
	public Environment(World world) {
		super();
		this.world = world;
		setBackground(Color.BLACK);
		flakes = new LinkedList<Flake>();
		wind = new Wind();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		screen = new Rectangle(0, 0, dim.width, dim.height);
	}
	
	/**
	 * Ground data must be initialized after the frame is constructed.</br>
	 * This is because the frame has to be packed so the ground data can get the width.
	 */
	public void init() {
		ground = new GroundData();
		
		windThread = new Thread(world.threadGroup, new Runnable() {
			public void run() {
				while (true) {
					wind.change();
					try {
						Thread.sleep(Methods.random(10, 20));
					} catch (InterruptedException e) {}
				}
			}
		});
		windThread.setName("Wind thread");
		windThread.setDaemon(true);
		windThread.start();
		
		flakeThread = new Thread(world.threadGroup, new Runnable() {
			public void run() {
				while (true) {
					if (Methods.random(0, 100) == 0) {
						if (Methods.random(0, 2) == 0 && flakeMake > 2) {
							flakeMake--;
						} else {
							flakeMake++;
						}
					}
					try {
						Thread.sleep(Methods.random(50, 70));
					} catch (InterruptedException e) {}
				}
			}
		});
		flakeThread.setName("Flake thread");
		flakeThread.setDaemon(true);
		flakeThread.start();
	}
	
	public void create(int quantity) {
		int width = getWidth();
		while (quantity > 0) {
			flakes.add(new Flake(width));
			quantity--;
		}
	}
	
	public void tick() {
		long start = System.currentTimeMillis();
		create(Methods.random(flakeMake - 2, flakeMake + 2));
		int collide;
		Iterator<Flake> it = flakes.iterator();
		while (it.hasNext()) {
			Flake flake = it.next();
			flake.tick();
			wind.apply(flake);
			if ((collide = ground.collidesAt(flake.x, flake.y + flake.width / 2.0D)) == 0)
				continue;
			it.remove();
			if (collide == 1)
				ground.add(flake);
		}
		ground.smoothGround();
		repaint();
		delay(DELAY - (System.currentTimeMillis() - start));
	}
	
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fill(screen);
		g.setColor(Color.WHITE);
		try {
			Iterator<Flake> it = flakes.iterator();
			while (it.hasNext()) {
				Flake flake = it.next();
				if (flake != null)
					g.fill(flake);
			}
		} catch (Exception e) {}
		if (ground != null)
			ground.render(g);
	}
	
	public void delay(long millis) {
		if (millis <= 0)
			return;
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}
	
	public class GroundData {
		
		Rectangle2D.Double[] data;
		
		public GroundData() {
			data = new Rectangle2D.Double[Environment.this.getWidth()];
			for (int i = 0; i < data.length; i++) {
				data[i] = new Rectangle2D.Double(i, Environment.this.getHeight(), 1, 0);
			}
		}
		
		private boolean inBounds(double x) {
			return x >= 0.0D && x < (double) data.length;
		}
		
		private void increment(int x, double by) {
			data[x].height += by;
			data[x].y -= by;
		}
		
		private void decrement(int x, double by) {
			data[x].height -= by;
			data[x].y += by;
		}
		
		public int collidesAt(double x, double y) {
			return inBounds(x) ? (y >= data[(int) x].y ? 1 : 0) : (y >= getHeight() ? 2 : 0);
		}
		
		public void smoothGround() {
			double minDif = 0.5D;
			int adjacent;
			for (int curr = 0; curr < data.length; curr++) {
				adjacent = curr + 1;
				if (adjacent < data.length) {
					if (data[curr].height > data[adjacent].height) {
						double dif = Math.abs(data[adjacent].height - data[curr].height) / 1.5D;
						if (dif >= minDif) {
							decrement(curr, dif);
							increment(adjacent, dif);
						}
					}
				}
				adjacent = curr - 1;
				if (adjacent >= 0) {
					if (data[curr].height > data[adjacent].height) {
						double dif = Math.abs(data[adjacent].height - data[curr].height) / 1.5D;
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
			int max = Math.min(data.length - 1, (int) Math.floor(flake.x + flake.width));
			for (int i = (int) flakeFloor; i <= max; i++) {
				double x = (double) i - flakeCenter;
				increment(i, flake.area(x, x + 1.0D));
			}
		}
		
		public void render(Graphics2D g) {
			for (int i = 0; i < data.length; i++) {
				g.fill(data[i]);
			}
		}
		
	}

}