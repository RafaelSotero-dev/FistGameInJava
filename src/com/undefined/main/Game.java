package com.undefined.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.undefined.entitys.Entity;
import com.undefined.entitys.Player;
import com.undefined.graphics.Spritesheet;
import com.undefined.world.World;

public class Game extends Canvas implements Runnable, KeyListener {

	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	public static ArrayList<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	
	private static final long serialVersionUID = 1L;
	private boolean isRunning = true;
	private Thread thread;
	private static JFrame jFrame;
	private BufferedImage layer =  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	public Game() {
		this.addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/map.png");
	}
	
	private static void inFrame(Game g) {
		jFrame = new JFrame("Engine");
		jFrame.add(g);
		jFrame.pack();
		jFrame.setResizable(false);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}
	
	private void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	private void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		inFrame(game);
		game.start();
	}
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = layer.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(layer, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTick = 60.0;
		double ns = 1000000000 / amountOfTick;
		double delta = 0;
		int frame = 0;
		double time = System.currentTimeMillis();
		requestFocus();
		
		while (isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1) {
				tick();
				render();
				frame++;
				delta--;
			}
			
			if (System.currentTimeMillis() - time >= 1000) {
				System.out.println("FPS: " + frame);
				frame = 0;
				
				time+=1000;
			}
		}
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_D) {
			Player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			Player.left = true;			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_W) {
			Player.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			Player.down = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_D) {
			Player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_A)
			Player.left = false;
		
		if (e.getKeyCode() == KeyEvent.VK_W) {
			Player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			Player.down = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
