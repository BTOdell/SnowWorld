import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class World extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static String VERSION = "2.0";
	
	public ThreadGroup threadGroup = null;
	private Environment env = null;
	private Thread envThread;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new World();
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public World() {
		super();
		threadGroup = new ThreadGroup("World thread group");
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setContentPane(getEnvironment());
		this.setSize(env.screen.getSize());
		this.setTitle("Snow world v" + VERSION);
		this.setUndecorated(true);
		this.setResizable(false);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});
		this.setVisible(true);
		
		envThread = new Thread(threadGroup, new Runnable() {
			public void run() {
				env.init();
				while (true) {
					env.tick();
				}
			}
		});
		envThread.setName("Environment thread");
		envThread.setDaemon(true);
		envThread.start();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private Environment getEnvironment() {
		if (env == null) {
			env = new Environment(this);
			env.setLayout(null);
		}
		return env;
	}

}