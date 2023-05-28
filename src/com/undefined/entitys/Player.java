package com.undefined.entitys;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.undefined.main.Game;
import com.undefined.world.Camera;
import com.undefined.world.World;

public class Player extends Entity {

	public static boolean right, left, up, down;

	private boolean moved;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private static final int RIGHT_DIR = 0, LEFT_DIR = 1;
	private int dir = RIGHT_DIR;
	private double speed = 1.4;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for (int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32  + (i*16), 0, 16, 16);
		}
		
		for (int i = 0; i < rightPlayer.length; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(80  - (i*16), 16, 16, 16);
		}
		
	}

	public void tick() {
		moved = false;
		
		if (right && World.isFreeToPass((int) (x+speed), getY())) {
			moved = true;
			dir = RIGHT_DIR;
			x+=speed;
		} else if (left && World.isFreeToPass((int) (x-speed), getY())) {
			moved = true;
			dir = LEFT_DIR;
			x-=speed;
		}
			
		if (up && World.isFreeToPass(getX(), (int) (y-speed))) {
			moved = true;
			y-=speed;
		} else if (down && World.isFreeToPass(getX(), (int) (y+speed))) {
			moved = true;
			y+=speed;
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
	
		Camera.x = Camera.clamp(getX() - (Game.WIDTH/ 2), 0,  (World.WIDTH*16) - Game.WIDTH);
		Camera.y = Camera.clamp(getY() - (Game.HEIGHT/ 2), 0, (World.HEIGHT*16) - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		if (dir == RIGHT_DIR) {
			g.drawImage(rightPlayer[index], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
		} else if (dir == LEFT_DIR) {
			g.drawImage(leftPlayer[index], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
		}
	}
	
}
