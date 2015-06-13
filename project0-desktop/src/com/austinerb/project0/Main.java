package com.austinerb.project0;

import com.austinerb.project0.engine.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "";	
		cfg.useGL20 = true;
		
		cfg.width = 1280; 
		cfg.height = 720; 
		
		boolean fullscreen = false;
		
		if (fullscreen) {
			cfg.width = 1920; 
			cfg.height = 1080; 
			cfg.fullscreen = true;
		}
		
		new LwjglApplication(new Game(), cfg);
	}
}