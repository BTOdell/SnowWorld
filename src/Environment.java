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

public class Environment {
	
	private final LinkedList<Flake> flakes;
	private final Wind wind;
	
	private int width;
	private int height;
	
	private GroundData ground = null;
	
	private int flakeMake = 5;
	
	public Environment() {
		this.flakes = new LinkedList<Flake>();
		this.wind = new Wind();
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.ground = new GroundData(width);
	}
	
	public void tick() {
		
		if (ground == null) {
			throw new IllegalStateException("GroundData hasn't been initialized yet.");
		}
		
		// randomize flakes
		if (Methods.random(0, 100) == 0) {
			if (Methods.random(0, 2) == 0 && flakeMake > 2) {
				flakeMake--;
			} else {
				flakeMake++;
			}
		}
		create(Methods.random(flakeMake - 2, flakeMake + 2));
		
		// randomize wind
		wind.change();
		
		int collide;
		Iterator<Flake> it = flakes.iterator();
		while (it.hasNext()) {
			Flake flake = it.next();
			flake.tick();
			wind.apply(flake);
			if ((collide = ground.collidesAt(flake.x, flake.y + flake.width / 2.0D)) == 0)
				continue;
			it.remove();
			if (collide == 1) {
				ground.add(flake);
			}
		}
		ground.smoothGround();
		
	}
	
	public void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, arg2, arg3)
		g.setColor(Color.WHITE);
		try {
			Iterator<Flake> it = flakes.iterator();
			while (it.hasNext()) {
				Flake flake = it.next();
				if (flake != null) {
					g.fill(flake);
				}
			}
		} catch (Exception e) {}
		if (ground != null) {
			ground.render(g);
		}
	}
	
	private void create(int quantity) {
		int width = getWidth();
		while (quantity > 0) {
			flakes.add(new Flake(width));
			quantity--;
		}
	}

}