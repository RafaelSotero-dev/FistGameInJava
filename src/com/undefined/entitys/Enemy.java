package com.undefined.entitys;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.undefined.main.Game;
import com.undefined.world.Camera;
import com.undefined.world.World;

public class Enemy extends Entity{

	private boolean moved;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private static final int RIGHT_DIR = 0, LEFT_DIR = 1;
	private int dir = RIGHT_DIR;
	private double speed = 1; 
	
	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		
		for (int i = 0; i < rightEnemy.length; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(32  + (i*16), 32, 16, 16);
		}
		
		for (int i = 0; i < rightEnemy.length; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(80  - (i*16), 48, 16, 16);
		}
	}

	public void tick() {
		if (x < Game.player.getX() && World.isFreeToPass((int) (x+speed), getY())) {
			moved = true;
			dir = RIGHT_DIR;
			x+=speed;
		}
		else if (x > Game.player.getX() && World.isFreeToPass((int) (x-speed), getY())) {
			moved = true;
			dir = LEFT_DIR;
			x-=speed;
		}
		
		if (y < Game.player.getY() && World.isFreeToPass(getX(), (int) (y+speed))) {
			moved = true;
			y+=speed;
		}
		else if (y > Game.player.getY() && World.isFreeToPass(getX(), (int) (y-speed))) {
			moved = true;
			y-=speed;
		}
		
		if (moved) {
			frames++;
			if (frames >= maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if (dir == RIGHT_DIR) {
			g.drawImage(rightEnemy[index], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
		} else if (dir == LEFT_DIR) {
			g.drawImage(leftEnemy[index], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
		}
	}
	
}
