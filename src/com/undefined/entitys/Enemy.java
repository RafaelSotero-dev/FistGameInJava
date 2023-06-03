package com.undefined.entitys;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.undefined.main.Game;
import com.undefined.main.Sound;
import com.undefined.world.Camera;
import com.undefined.world.World;

public class Enemy extends Entity{

	public int life = 10;
	
	private boolean moved;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private static final int RIGHT_DIR = 0, LEFT_DIR = 1;
	private int dir = RIGHT_DIR;
	private double speed = 0.8 ;
	private boolean isDamaged = false;
	private boolean firstHit = false;
	private int damagedSpriteDuration = 0;
	
	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage damegedEnemy;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		damegedEnemy = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for (int i = 0; i < rightEnemy.length; i++) {
			rightEnemy[i] = Game.spritesheet.getSprite(32  + (i*16), 32, 16, 16);
		}
		
		for (int i = 0; i < rightEnemy.length; i++) {
			leftEnemy[i] = Game.spritesheet.getSprite(80  - (i*16), 48, 16, 16);
		}
	}

	public void tick() {
		if (this.calculateDistance(getX(), getY(), Game.player.getX(), Game.player.getY()) < 100 || firstHit) {
			if (isCollidingWithPlayer() == false) {
				if (x <  Game.player.getX() && World.isFreeToPass((int) (x+speed), getY(), 0)
						&& !isColliding((int) (x+speed), getY())) {
					moved = true;
					dir = RIGHT_DIR;
					x+=speed;
				}
				else if (x > Game.player.getX() && World.isFreeToPass((int) (x-speed), getY(), 0)
						&& !isColliding((int) (x-speed), getY())) {
					moved = true;
					dir = LEFT_DIR;
					x-=speed;
				}
				
				if (y <  Game.player.getY() && World.isFreeToPass(getX(), (int) (y+speed), 0)
						&& !isColliding(getX(), (int) (y+speed))) {
					moved = true;
					y+=speed;
				}
				else if (y > Game.player.getY() && World.isFreeToPass(getX(), (int) (y-speed), 0)
						&& !isColliding(getX(), (int) (y-speed))) {
					moved = true;
					y-=speed;
				}
			} else {
				if (Game.rand.nextInt(100) < 15) {
					Game.player.life-= Game.rand.nextInt(5);
					Sound.HITSOUND.play();
					Game.player.isDamaged = true;
				}
			}
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
		
		isCollidingWithBullet();
		
		if (isDamaged) {
			damagedSpriteDuration++;
			if (damagedSpriteDuration >= 8) {
				damagedSpriteDuration = 0;
				isDamaged = false;
			}
		}
		
		if (life <= 0) {
			destroySelf();
			return;
		}
		
	}
	
	public void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}
	
	public void isCollidingWithBullet() {
		for (int i = 0; i < Game.projectile.size(); i++) {
			Entity e = Game.projectile.get(i);
			if (e instanceof Projectile) {
				if (Entity.isColliding(this, e)) {
					isDamaged = true;
					firstHit = true;
					life--;
					Sound.HITSOUND.play();
					Game.projectile.remove(i);
					return;
				}
			}
		}
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle currentEnemy = new Rectangle(getX(), getY(), 10, 10);
		
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 12, 12);
		
		return currentEnemy.intersects(player);
	}
		
	public boolean isColliding(int xnext, int ynext) {
		Rectangle currentEnemy = new Rectangle(xnext, ynext, 10, 10);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			
			Rectangle otherEnemy = new Rectangle(e.getX(), e.getY(), 10, 10);
			if (currentEnemy.intersects(otherEnemy)) {
				return true;
			}
		}
		
		return false;
	}
		
	public void render(Graphics g) {
		if (isDamaged) {
			g.drawImage(damegedEnemy, getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
		} else {
			if (dir == RIGHT_DIR) {
				g.drawImage(rightEnemy[index], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
				
			} else if (dir == LEFT_DIR) {
				g.drawImage(leftEnemy[index], getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
				
			}
		}
	}
	
}
