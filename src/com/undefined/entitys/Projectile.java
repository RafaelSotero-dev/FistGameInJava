package com.undefined.entitys;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.undefined.main.Game;
import com.undefined.world.Camera;

public class Projectile extends Entity{

	private double speed = 3.4;
	private int life = 50, curLife = 0;
	
	public double dx,dy;
	
	public Projectile(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);	
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x += dx*speed;
		y += dy*speed;
				
		curLife++;
				
		if (curLife == life) {
			curLife = 0;
			Game.projectile.remove(this);
			return;	
		}

	}
	

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect((int)this.getX() - Camera.x,(int) this.getY() - Camera.y, 2, 2);
	}
	
}
