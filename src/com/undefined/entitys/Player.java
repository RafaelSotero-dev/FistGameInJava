package com.undefined.entitys;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.undefined.main.Game;
import com.undefined.main.Sound;
import com.undefined.world.Camera;
import com.undefined.world.World;

public class Player extends Entity {

	public static boolean right, left, up, down;
	public int life = 100;
	public int ammo = 0;
	public boolean isShot = false;
	public boolean isMouseShot = false;
	public boolean isDamaged = false;
	public int damagedSpriteDuration = 0;
	public boolean gun = false;
	public final int RIGHT_DIR = 0, LEFT_DIR = 1;
	public int dir = RIGHT_DIR;
	public double speed = 1.4;
	public double mx, my;
	
	public boolean jump = false;
	public boolean isJumping = false;
	public int z = 0;
	public int curJumping = 0;
	public boolean jumpUp = false, jumpDown = false;
 	
	private boolean moved;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage damagedPlayer;
		
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		damagedPlayer = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for (int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32  + (i*16), 0, 16, 16);
		}
		
		for (int i = 0; i < rightPlayer.length; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(80  - (i*16), 16, 16, 16);
		}
		
	}

	public void tick() {
		moved = false;
		
		if (isJumping) {
			jump = true;
			if (jump) {
				jumpUp = true;
				jumpDown = false;
				isJumping = false;
			}
		}
		
		
		if (jump) {			
			if (jumpUp) {
				curJumping+=2;
			}
			if (jumpDown) {
				curJumping-=2;
				if (curJumping <= 0) {
					jumpUp = false;
					jumpDown = false;
					jump = false;
				}
			}
			
			z = curJumping;
			if (curJumping >= 40) {
				jumpUp = false;
				jumpDown = true;
			}
		}
		
		if (right && World.isFreeToPass((int) (x+speed), getY(), z)) {
			moved = true;
			dir = RIGHT_DIR;
			x+=speed;
		} else if (left && World.isFreeToPass((int) (x-speed), getY(), z)) {
			moved = true;
			dir = LEFT_DIR;
			x-=speed;
		}
			
		if (up && World.isFreeToPass(getX(), (int) (y-speed), z)) {
			moved = true;
			y-=speed;
		} else if (down && World.isFreeToPass(getX(), (int) (y+speed), z)) {
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
		
		if (isDamaged) {
			damagedSpriteDuration++;
			if (damagedSpriteDuration >= 8) {
				damagedSpriteDuration = 0;
				isDamaged = false;
			}
		}

		checkItens();
		
		Camera.x = Camera.clamp(getX() - (Game.WIDTH/ 2), 0,  (World.WIDTH*16) - Game.WIDTH);
		Camera.y = Camera.clamp(getY() - (Game.HEIGHT/ 2), 0, (World.HEIGHT*16) - Game.HEIGHT);
		
		if (isShot == true) {
			isShot = false;
			if (ammo > 0 && gun == true) {
				int dx = 0;
				int px = 0;
				int py = 9;
				if (dir ==  RIGHT_DIR) {
					px = 19;
					dx = 1;
				} else {
					px = -6;
					dx = -1;
				}
				Projectile projectile = new Projectile((int)this.getX()+px,(int) this.getY()+py, 3, 3, null, dx, 0);
				Game.projectile.add(projectile);
				ammo--;
				Sound.SHOTSOUND.play();
			}
		}
		
		if (isMouseShot) {
			isMouseShot = false;
			if (ammo > 0 && gun == true) {

				double angle = 0;
				int px = 0;
				int py = 9;
				if (dir ==  RIGHT_DIR) {
					px = 19;
					angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+px - Camera.x));
				} else {
					px = -6;
					angle = Math.atan2(my - (this.getY()+py - Camera.y), mx - (this.getX()+px - Camera.x));
				}
				

				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				Projectile projectile = new Projectile((int)this.getX()+px,(int) this.getY()+py, 3, 3, null, dx, dy);
				Game.projectile.add(projectile);
				ammo--;
				Sound.SHOTSOUND.play();
			}
		}

		if (life <= 0) {
			Game.gameStatus = "GAME OVER";
			Sound.GAMEOVERSOUND.play();
		}
	}
	
	public void checkItens() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof LifePack) {
				if (Entity.isColliding(this, e)) {
					if (life == 100) 
						return;
					
					life+=LifePack.increaseLife;
					if (life >=100) 
						life = 100;
					
					Game.entities.remove(i);
					return;
				}
			}
			
			if (e instanceof Bullet) {
				if (Entity.isColliding(this, e)) {
					ammo+=1000;
					Game.entities.remove(i);
					return;
				}
			}
			
			if (e instanceof Gun) {
				if (Entity.isColliding(this, e)) {
					gun = true;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if (isDamaged) {
			g.drawImage(damagedPlayer, getX() - Camera.x, getY() - Camera.y - z, getWidth(), getHeight(), null);
			if (dir == RIGHT_DIR && gun) {
				g.drawImage(Entity.DAMAGEDGUNRIGHT_ENT, getX() - Camera.x + 8, getY() - Camera.y - z, getWidth(), getHeight(), null);
			}
			if (dir == LEFT_DIR && gun) {
				g.drawImage(Entity.DAMAGEDGUNLEFT_ENT, getX() - Camera.x - 8, getY() - Camera.y - z, getWidth(), getHeight(), null);
			}
		} else {
			if (dir == RIGHT_DIR) {
				g.drawImage(rightPlayer[index], getX() - Camera.x, getY() - Camera.y - z, getWidth(), getHeight(), null);
				if (gun) {
					g.drawImage(Entity.GUNRIGHT_ENT, getX() - Camera.x + 8, getY() - Camera.y - z, getWidth(), getHeight(), null);
				}
			} else if (dir == LEFT_DIR) {
				g.drawImage(leftPlayer[index], getX() - Camera.x, getY() - Camera.y - z, getWidth(), getHeight(), null);
				if (gun) {
					g.drawImage(Entity.GUNLEFT_ENT, getX() - Camera.x - 8, getY() - Camera.y - z, getWidth(), getHeight(), null);
				}
			}
		}
	}
	
}
