package com.undefined.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.undefined.main.Game;

public class Tiles {

	public static BufferedImage WALL_ENT = Game.spritesheet.getSprite(16, 0, 16, 16);
	public static BufferedImage FLOOR_ENT = Game.spritesheet.getSprite(0, 0, 16, 16);
	
	private int x,y;
	private BufferedImage sprite;
	
	public Tiles(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	
}
