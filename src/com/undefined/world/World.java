package com.undefined.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.undefined.entitys.Bullet;
import com.undefined.entitys.Enemy;
import com.undefined.entitys.Entity;
import com.undefined.entitys.Gun;
import com.undefined.entitys.LifePack;
import com.undefined.entitys.Player;
import com.undefined.graphics.Spritesheet;
import com.undefined.graphics.UI;
import com.undefined.main.Game;

public class World {

	public static Tiles[] tiles;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 16; 
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			tiles = new Tiles[WIDTH*HEIGHT];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					
					switch(pixelAtual) {
						case 0xFFFFFFFF:
							//WALL
							tiles[xx + (yy*WIDTH)] = new WallTile(xx*16, yy*16, Tiles.WALL_ENT);
						break;
						
						case 0xFF000000:
							//FLOOR
							tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tiles.FLOOR_ENT);
						break;
						
						case 0xFF0026FF:
							//PLAYER
							Game.player.setX((int)(xx*16));
							Game.player.setY((int)(yy*16));
							tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tiles.FLOOR_ENT);
						break;
						
						case 0xFFFF56E8:
							//LIFEPACK
							LifePack life =  new LifePack(xx*16, yy*16, 16, 16, Entity.LIFE_ENT);
							Game.entities.add(life);
							tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tiles.FLOOR_ENT);
						break;
						
						case 0xFF19A819:
							//GUN
							Game.entities.add(new Gun(xx*16, yy*16, 16, 16, Entity.GUN_ENT));
							tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tiles.FLOOR_ENT);
						break;
						
						case 0xFFFFFA0A:
							//BULLET
							Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_ENT));
							tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tiles.FLOOR_ENT);
						break;
						
						case 0xFFFF0E0A:
							//ENEMY
							Enemy e = new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_ENT);
							Game.entities.add(e);
							Game.enemies.add(e);
							tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tiles.FLOOR_ENT);
						break;
						
						default:
							tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tiles.FLOOR_ENT);
						break;
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void restartGame(String newWorld) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.ui = new UI();
		Game.entities.add(Game.player);
		Game.world = new World(newWorld);
		return;
	}
	
	public static boolean isFreeToPass(int xnext, int ynext, int zplayer) {
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE-1) / TILE_SIZE;
		
		if (!(tiles[x1 + (y1*WIDTH)] instanceof WallTile || tiles[x2 + (y2*WIDTH)] instanceof WallTile
			|| tiles[x3 + (y3*WIDTH)] instanceof WallTile || tiles[x4 + (y4*WIDTH)] instanceof WallTile)) {
			return true;
		}
		
		if (zplayer > 0) {
			return true;
		}
		
		return false;
		
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x / TILE_SIZE;
		int ystart = Camera.y / TILE_SIZE;
		
		int xfinal = xstart + (Game.WIDTH / TILE_SIZE);
		int yfinal = ystart + (Game.HEIGHT / TILE_SIZE);
		
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT )
					continue;
				
				Tiles t = tiles[xx + (yy*WIDTH)];
				t.render(g);
			}
		}
	}
}



