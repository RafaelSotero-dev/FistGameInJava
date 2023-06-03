package com.undefined.entitys;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.undefined.main.Game;
import com.undefined.world.Camera;

public class Entity {

	public static BufferedImage LIFE_ENT = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage GUN_ENT = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET_ENT = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_ENT = Game.spritesheet.getSprite(2*16, 32, 16, 16);
	public static BufferedImage GUNRIGHT_ENT = Game.spritesheet.getSprite(8*16, 0, 16, 16);
	public static BufferedImage GUNLEFT_ENT = Game.spritesheet.getSprite(9*16, 0, 16, 16);
	public static BufferedImage DAMAGEDGUNRIGHT_ENT = Game.spritesheet.getSprite(8*16, 16, 16, 16);
	public static BufferedImage DAMAGEDGUNLEFT_ENT = Game.spritesheet.getSprite(9*16, 16, 16, 16);
	
	public BufferedImage sprite; 

	protected double x, y, width, height;
	
	protected int z = 0;
	protected int maskx, masky, mwidth, mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = x;
		this.masky = y;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public int getMaskx() {
		return (int)this.maskx;
	}
	
	public void setMaskx(int maskX) {
		this.maskx = maskX;
	}
	
	public int getMasky() {
		return (int)this.masky;
	}
	
	public void setMasky(int Masky) {
		this.masky = Masky;
	}
	
	public int getMwidth() {
		return (int)this.mwidth;
	}
	
	public void setMwidth(int Mwidth) {
		this.mwidth = Mwidth;
	}
	
	public int getMheight() {
		return (int)this.mwidth;
	}
	
	public void setMheight(int Mheight) {
		this.mheight = Mheight;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getWidth() {
		return (int)this.width;
	}
	
	public int getHeight() {
		return (int)this.height;
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public void tick() {};
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle((int) (e1.getX()), (int) (e1.getY()),  (int) e1.getWidth(), (int) e1.getHeight());
		Rectangle e2Mask = new Rectangle((int)e2.getX(), (int)e2.getY(), e2.getWidth(), e2.getHeight());
		
		if (e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
	}
	
}
