package snowworld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;
import java.util.LinkedList;

public class Environment implements Tickable, Renderable {
	
	private final LinkedList<Flake> flakes;
	private final TimedEvent flakeEvent = new TimedEvent(new Runnable() {
		public void run() {
			flakes.add(new Flake(Environment.this.width));
		}
	}, new Function<Long>() {
		public Long invoke() {
			return Methods.random(10_000_000L, 50_000_000L);
		}
	}, new Function<Long>() {
		public Long invoke() {
			return Methods.random(10_000_000L, 50_000_000L);
		}
	});
	
	private final Wind wind;
	private final TimedEvent windEvent = new TimedEvent(new Runnable() {
		public void run() {
			Environment.this.wind.change();
		}
	}, new Function<Long>() {
		public Long invoke() {
			return Methods.random(10_000_000L, 5_000_000_000L);
		}
	}, new Function<Long>() {
		public Long invoke() {
			return Methods.random(10_000_000L, 5_000_000_000L);
		}
	});
	
	private int width = -1;
	private int height = -1;
	
	private GroundData ground = null;
	
	public Environment() {
		this.flakes = new LinkedList<Flake>();
		this.wind = new Wind();
	}
	
	public boolean isSizeSet() {
		return this.width != -1 && this.height != -1;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.ground = new GroundData(width, height);
	}
	
	@Override
	public void init() {
		this.windEvent.init();
		this.windEvent.start();
		
		this.flakeEvent.init();
		this.flakeEvent.start();
	}
	
	@Override
	public void tick(long currentTime, long deltaTime) {
		
		if (ground == null) {
			throw new IllegalStateException("GroundData hasn't been initialized yet.");
		}
		
		System.out.println("Delta Time: " + deltaTime);
		
		this.flakeEvent.tick(currentTime, deltaTime);
		this.windEvent.tick(currentTime, deltaTime);
		
		int collide;
		Iterator<Flake> it = flakes.iterator();
		while (it.hasNext()) {
			Flake flake = it.next();
			flake.tick();
			wind.apply(flake);
			if ((collide = ground.collidesAt(flake.getX(), flake.getY() + flake.getWidth() / 2.0D)) == 0)
				continue;
			it.remove();
			if (collide == 1) {
				ground.add(flake);
			}
		}
		ground.smoothGround();
		
	}
	
	//private boolean renderOnce = true;
	
	@Override
	public void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//if (renderOnce) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
		//	renderOnce = false;
		//}
		g.setColor(Color.WHITE);
		try {
			Iterator<Flake> it = flakes.iterator();
			while (it.hasNext()) {
				Flake flake = it.next();
				if (flake != null) {
					flake.render(g);
				}
			}
		} catch (Exception e) {}
		if (ground != null) {
			ground.render(g);
		}
	}

}