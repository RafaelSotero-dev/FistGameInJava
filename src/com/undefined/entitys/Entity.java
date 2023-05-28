package com.undefined.entitys;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.undefined.main.Game;
import com.undefined.world.Camera;

public class Entity {

	public static BufferedImage LIFE_ENT = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage GUN_ENT = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET_ENT = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_ENT = Game.spritesheet.getSprite(2*16, 32, 16, 16);
	
	public BufferedImage sprite; 

	protected double x, y, width, height;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
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
	
	public void tick() {};
	
	public void render(Graphics g) {
		g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
	}
	
}
