package com.undefined.graphics;

import java.awt.Color;
import java.awt.Graphics;

import com.undefined.main.Game;

public class UI {
		
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(2, 2, 54, 10);
		
		g.setColor(Color.RED);
		g.fillRect(4, 4, 50, 6);
		g.setColor(Color.GREEN);
		g.fillRect(4, 4, Game.player.life/2, 6);
	}
	
}
