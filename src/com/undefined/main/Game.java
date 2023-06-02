 package com.undefined.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import com.undefined.entitys.Enemy;
import com.undefined.entitys.Entity;
import com.undefined.entitys.Player;
import com.undefined.entitys.Projectile;
import com.undefined.graphics.Spritesheet;
import com.undefined.graphics.UI;
import com.undefined.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	public static ArrayList<Entity> entities;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Projectile> projectile;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;
	public static String gameStatus = "MENU";
	public static boolean isPaused = false;
	
	public static UI ui;
	public static boolean restartGame = false;
	public static int currentLevel = 1, maxLevel = 2;
	public static boolean saveGame = false;
	public static double mx;
	public static double my;
	
	private Menu menu;
	
	private InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixel.ttf");
	private InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("pixel.ttf");
	public static Font newFont;
	public static Font newFont2;
	
	private static final long serialVersionUID = 1L;
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean isRunning = true;
	private Thread thread;
	private static JFrame jFrame;
	private BufferedImage layer =  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	public Game() {
//		Sound.BACKGROUNDSOUND.loop();		
		menu = new Menu();
		rand = new Random();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(50f);
			newFont2 = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(32f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		projectile = new ArrayList<Projectile>();
		spritesheet = new Spritesheet("/spritesheet.png");
		
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		ui = new UI();
		entities.add(player);
		world = new World("/level1.png");
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
	
	private synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	private synchronized void stop() {
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
		if (gameStatus == "NORMAL") {
			if (saveGame == true) {
				saveGame = false;
				
				String[] key = {"level"};
				int[] value = {currentLevel}; 
				
				Menu.saveGame(key, value, 10);
				System.out.print("Jogo Salvo!");
			}
			restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for (int i = 0; i < projectile.size(); i++) {
				projectile.get(i).tick();
			}
			
			if (enemies.size() == 0) {
				currentLevel++;
				if (currentLevel > maxLevel) {
					currentLevel = 1;
				}
				String newWorld = "/level"+currentLevel+".png";
				World.restartGame(newWorld);
			}
		} else if (gameStatus == "GAME OVER") {
			framesGameOver++;
			if (framesGameOver > 40) {
				framesGameOver = 0;
				if (showMessageGameOver) {
					showMessageGameOver = false;
				} else {
					showMessageGameOver = true;
				}
			}
			
			if (restartGame == true) {
				restartGame = false;
				gameStatus = "NORMAL";
				currentLevel = 1;
				String newWorld = "/level"+currentLevel+".png";
				World.restartGame(newWorld);
			}
		} else if (gameStatus == "MENU") {
			menu.tick();
		} else if (gameStatus == "PAUSE") {
			menu.tick();
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
		for (int i = 0; i < projectile.size(); i++) {
			projectile.get(i).render(g);
		}
		ui.render(g);
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(layer, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 17));
		g.setColor(Color.WHITE);
		g.drawString("Munição: "+player.ammo, 600, 22);
		
		if (gameStatus == "GAME OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			
			g.setFont(new Font("arial", Font.BOLD, 60));
			g.setColor(Color.RED);
			g.drawString("GAME OVER", (WIDTH*SCALE) /2 - 182, (HEIGHT*SCALE) /2 - 20);
			
			g.setFont(new Font("arial", Font.BOLD, 32));
			g.setColor(Color.WHITE);
			
			if (showMessageGameOver) {
				g.drawString(">Pressione Enter para reiniciar<", (WIDTH*SCALE) /2 - 250, (HEIGHT*SCALE) /2 + 60);
			} 
			
		} else if (gameStatus == "MENU") {
			menu.render(g);
		} else if (gameStatus == "PAUSE") {
			menu.render(g);
		}
		
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
		if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_A  || e.getKeyCode() == KeyEvent.VK_LEFT) {
			Player.left = true;			
		}
		
		if (e.getKeyCode() == KeyEvent.VK_W  || e.getKeyCode() == KeyEvent.VK_UP) {
			Player.up = true;
			
			if (gameStatus == "MENU" || gameStatus == "PAUSE") {
				Menu.up = true;
			}
			
		} else if (e.getKeyCode() == KeyEvent.VK_S   || e.getKeyCode() == KeyEvent.VK_DOWN) {
			Player.down = true;
			
			
			if (gameStatus == "MENU" || gameStatus == "PAUSE") {
				Menu.down = true;
			}
		}
	
		if (e.getKeyCode() == KeyEvent.VK_G) {
			player.isShot = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.isJumping = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
			if (gameStatus == "MENU" || gameStatus == "PAUSE") {
				Menu.enter = true;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (gameStatus == "NORMAL") {
				isPaused = true;
				gameStatus = "PAUSE";
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			if (gameStatus == "NORMAL") {
				saveGame = true;
			}
				
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_D   || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_A  || e.getKeyCode() == KeyEvent.VK_LEFT) {
			Player.left = false;
		}
			
		
		if (e.getKeyCode() == KeyEvent.VK_W   || e.getKeyCode() == KeyEvent.VK_UP) {
			Player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S   || e.getKeyCode() == KeyEvent.VK_DOWN) {
			Player.down = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_G) {
			player.isShot = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		player.isMouseShot = true;
		player.mx = e.getX() / 3;
		player.my = e.getY() / 3;
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}
	
}
