package com.austinerb.project0.engine;

public class HUDManager {

	private Game game;
	
	private HUD hud;
	
	public HUDManager(Game game) {
		this.game = game;
		
		hud = new HUD(game);
		this.game.getRenderer().setHud(hud);
	}
	
	public void update() {
		hud.update();
	}
	
	public HUD getHUD() {
		return hud;
	}
}
