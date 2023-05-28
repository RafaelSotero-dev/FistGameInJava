package com.undefined.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.undefined.entitys.Bullet;
import com.undefined.entitys.Enemy;
import com.undefined.entitys.Entity;
import com.undefined.entitys.Gun;
import com.undefined.entitys.LifePack;
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
							Game.entities.add(new LifePack(xx*16, yy*16, 16, 16, Entity.LIFE_ENT));
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
							//BULLET
							Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_ENT));
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
	
	public static boolean isFreeToPass(int xnext, int ynext) {
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext + TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext + TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE-1) / TILE_SIZE;
		
		return !(
				(tiles[x1 + (y1*WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*WIDTH)] instanceof WallTile)
				);
		
	}
	
	public void render(Graphics g) {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				Tiles t = tiles[xx + (yy*WIDTH)];
				t.render(g);
			}
		}
	}
}



