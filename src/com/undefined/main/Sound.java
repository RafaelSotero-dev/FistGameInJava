package com.undefined.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	
	public static final Sound BACKGROUNDSOUND = new Sound("/music.wav");
	public static final Sound HITSOUND = new Sound("/hit.wav");
	public static final Sound SHOTSOUND = new Sound("/shot.wav");
	public static final Sound GAMEOVERSOUND = new Sound("/gameover.wav");
	public static final Sound SELECTOPTIONSSOUND = new Sound("/select.wav");
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch (Throwable e) {}
	}
	
	public void play() {
		try {
			new Thread() {
				
				public void run() {
					clip.play();
				}
				
			}.start();
		}catch (Throwable e) {}
	}
	
	public void loop() {
		try {
			new Thread() {
				
				public void run() {
					clip.loop();
				}
				
			}.start();
		}catch (Throwable e) {}
	}
	
}
