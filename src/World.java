import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class World extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static String VERSION = "3.0";
	
	private static final int DELAY = 30;
	
	private final Environment environment;
	
	private Thread renderThread = null;
	private final Runnable renderRunnable = new Runnable() {
		public void run() {
			environment.init();
			while (running) {
				
				long startTime = System.currentTimeMillis();
				
				// tick
				
				
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
				
				long elapsed = System.currentTimeMillis() - startTime;
				sleep(DELAY - elapsed);
				
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
				System.out.println("World shown!");
			}
			public void componentResized(ComponentEvent e) {
			}
			public void componentMoved(ComponentEvent e) {
			}
			public void componentHidden(ComponentEvent e) {
			}
		});
		this.setIgnoreRepaint(true);
		
		this.createBufferStrategy(2);
		
		
		
	}
	
	private static void sleep(long millis) {
		if (millis <= 0) {
			return;
		}
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}

}