package com.undefined.entitys;

import java.awt.image.BufferedImage;

public class LifePack extends Entity {

	public static int increaseLife = 8;
	
	public LifePack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
}
