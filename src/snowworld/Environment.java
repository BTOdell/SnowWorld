package snowworld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;
import java.util.LinkedList;

public class Environment implements Tickable, Renderable {
	
	private final LinkedList<Flake> flakes;
	private final TimedEvent flakeEvent = new TimedEvent(() -> {
		final Flake newFlake = new Flake(Environment.this.width);
		newFlake.init();
		this.flakes.add(newFlake);
	}, () -> {
		return Methods.random(20_000_000L, 100_000_000L);
	}, () -> {
		return Methods.random(20_000_000L, 100_000_000L);
	});
	
	private final Wind wind;
	private final TimedEvent windEvent = new TimedEvent(() -> {
		Environment.this.wind.change();
	}, () -> {
		return Methods.random(10_000_000L, 5_000_000_000L);
	}, () -> {
		return Methods.random(10_000_000L, 5_000_000_000L);
	});
	
	private int width = -1;
	private int height = -1;
	
	private GroundData ground = null;
	
	public Environment() {
		this.flakes = new LinkedList<>();
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
		
		if (this.ground == null) {
			throw new IllegalStateException("GroundData hasn't been initialized yet.");
		}
		
		System.out.println("Delta Time: " + deltaTime);
		
		this.flakeEvent.tick(currentTime, deltaTime);
		this.windEvent.tick(currentTime, deltaTime);
		
		final Iterator<Flake> it = this.flakes.iterator();
		while (it.hasNext()) {
			final Flake flake = it.next();
			flake.tick(currentTime, deltaTime);
			this.wind.apply(flake);
			final int collide = this.ground.collidesAt(flake.getX(), flake.getY() + flake.getWidth() / 2.0D);
			if (collide == 0) {
				continue;
			}
			it.remove();
			if (collide == 1) {
				this.ground.add(flake);
			}
		}
		this.ground.smoothGround();
		
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
		final Iterator<Flake> it = this.flakes.iterator();
		while (it.hasNext()) {
			Flake flake = it.next();
			if (flake != null) {
				flake.render(g);
			}
		}
		if (this.ground != null) {
			this.ground.render(g);
		}
	}

}