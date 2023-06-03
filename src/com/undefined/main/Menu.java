package com.undefined.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.undefined.world.World;

public class Menu {

	private String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};
	private int currentOption = 0, maxOptions = options.length -1;
	
	public static boolean up,down, enter;
	
	public static boolean saveExist = false, saveGame = false;
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		
		for (int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			
			switch(spl2[0]) {
				case "level":
					World.restartGame("/level"+spl2[1]+".png");
					Game.gameStatus = "NORMAL";
			}
			
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if (file.exists()) {
			
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while ((singleLine = reader.readLine()) != null) {
						String[] splited = singleLine.split(":");
						 char[] val = splited[1].toCharArray();
						 splited[1] = "";
						 for (int i = 0; i < val.length; i++) {
							 val[i]-=encode;
							 splited[1]+=val[i];
						 }
						 
						 line+=splited[0];
						 line+=":";
						 line+=splited[1];
						 line+="/";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
					
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		return line;
	}
	
	public static void saveGame(String[] val, int[] val2, int encode) {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter("save.txt"));
		}catch (IOException e) {}
		
		for (int i = 0; i < val.length; i ++) {
			String content = val[0];
			content+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();

			for (int n = 0; n < value.length; n++) {
				value[n]+=encode;
				content+=value[n];				
			}
			try {
				writer.write(content);
				if (i < val.length - 1)
					writer.newLine();

			} catch (IOException e) {}
		}
		
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {}
	}
	
	public void tick() {
		File file = new File("save.txt");
		if (file.exists()) {
			saveExist = true;
		} else {
			saveExist = false;
		}
		
		if (up) {
			up = false;
			Sound.SELECTOPTIONSSOUND.play();
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOptions;
			}
		} else if (down) {
			down = false;
			currentOption++;
			Sound.SELECTOPTIONSSOUND.play();
			if (currentOption > maxOptions) {
				currentOption = 0;
			}
		}
		
		if (enter) {
			enter = false;
			if (options[currentOption] == "Novo Jogo") {
				Game.gameStatus = "NORMAL";
				file = new File("save.txt");
				file.delete();
			} else if (options[currentOption] == "Carregar Jogo") {
				file = new File("save.txt");
				if (file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			} else if (options[currentOption] == "Sair") {
				 System.exit(1);
			}
		} 
	}
		 
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		g.setFont(Game.newFont);
		g.setColor(Color.RED);
		g.drawString("UNDEFINED", (Game.WIDTH*Game.SCALE) /2 - 160, (Game.HEIGHT*Game.SCALE) /2 - 120);
		
		//Opições do menu
		g.setFont(Game.newFont2);
		g.setColor(Color.WHITE);
		
		if (Game.gameStatus == "PAUSE") {
			g.drawString("Resume", (Game.WIDTH*Game.SCALE) /2 - 58, (Game.HEIGHT*Game.SCALE) /2 - 20);
		} else {
			g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) /2 - 78, (Game.HEIGHT*Game.SCALE) /2 - 20);
		}
		
		g.setFont(Game.newFont2);
		g.setColor(Color.WHITE);
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) /2 - 115, (Game.HEIGHT*Game.SCALE) /2 + 30);
		
		g.setFont(Game.newFont2);
		g.setColor(Color.WHITE);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) /2 - 42, (Game.HEIGHT*Game.SCALE) /2 + 85);
		
		if (options[currentOption] == "Novo Jogo") {
			g.setFont(Game.newFont2);
			g.setColor(Color.WHITE);
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 120, (Game.HEIGHT*Game.SCALE) /2 - 20);
		} else if (options[currentOption] == "Carregar Jogo") {
			g.setFont(Game.newFont2);
			g.setColor(Color.WHITE);
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 148, (Game.HEIGHT*Game.SCALE) /2 + 30);
		} else if (options[currentOption] == "Sair") {
			g.setFont(Game.newFont2);
			g.setColor(Color.WHITE);
			g.drawString(">", (Game.WIDTH*Game.SCALE) /2 - 85, (Game.HEIGHT*Game.SCALE) /2 + 85);
		}
		
//		double MouseLocationRadians = Math.atan2(200+45 - Game.my, 200+45 - Game.mx);
//		System.out.println(Math.toDegrees(MouseLocationRadians));
//		g2.rotate(MouseLocationRadians, 200+45, 200+45);
//		g2.setColor(Color.RED);
//		g2.fillRect(200, 200, 90, 90);
		
//		g.setColor(Color.WHITE);
//		g.fillRect((Game.WIDTH*Game.SCALE) /2, (Game.HEIGHT*Game.SCALE) /2  -235, 1, Game.HEIGHT*Game.SCALE);
	}
}
