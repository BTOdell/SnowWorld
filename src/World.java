import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class World extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static String VERSION = "3.0";
	
	private static final int NANO_DELAY = 30000000;
	
	private final Environment environment;
	
	private Thread renderThread = null;
	private final Runnable renderRunnable = new Runnable() {
		public void run() {
			while (running) {
				
				long startTime = System.nanoTime();
				
				// tick
				environment.tick();
				
				// render
				BufferStrategy bs = getBufferStrategy();
				if (bs != null) {
					Graphics g1 = bs.getDrawGraphics();
					try {
						environment.render((Graphics2D) g1);
					} finally {
						g1.dispose();
					}
					if (!bs.contentsLost()) {
						bs.show();
					}
				}
				
				long elapsed = System.nanoTime() - startTime;
				sleep(NANO_DELAY - elapsed);
				
			}
		}
	};
	private boolean running = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.setProperty("sun.awt.noerasebackground", "true"); // prevents the background from auto clearing
				World world = new World();
				world.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public World() {
		super();
		this.environment = new Environment();
		
		this.running = true;
		this.renderThread = new Thread(this.renderRunnable);
		this.renderThread.setName("Render Thread");
		this.renderThread.setPriority(Thread.MAX_PRIORITY);
		this.renderThread.setDaemon(true);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dim);
		this.setTitle("Snow World v" + VERSION);
		this.setUndecorated(true);
		this.setResizable(false);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});
		this.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {
				init();
			}
			public void componentResized(ComponentEvent e) {
				if (!World.this.environment.isSizeSet()) {
					World.this.environment.setSize(World.this.getWidth(), World.this.getHeight());
				}
			}
			public void componentMoved(ComponentEvent e) {
			}
			public void componentHidden(ComponentEvent e) {
			}
		});
		this.setIgnoreRepaint(true);
	}
	
	private void init() {
		this.createBufferStrategy(2);
		
		this.renderThread.start();
	}
	
	@Override
	public void update(Graphics g1) {
		if (!getIgnoreRepaint()) {
			super.update(g1);
		}
	}
	
	@Override
	public void paint(Graphics g1) {
		if (!getIgnoreRepaint()) {
			super.paint(g1);
		}
	}
	
	private static void sleep(long sleepNanos) {
		if (sleepNanos <= 0) {
			return;
		}
		long sleepMillis = sleepNanos / 1000000L;
		sleepNanos -= sleepMillis * 1000000L;
		try {
			Thread.sleep(sleepMillis, (int) sleepNanos);
		} catch (InterruptedException e) {}
	}

}