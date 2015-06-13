package com.austinerb.project0.engine;

import com.austinerb.project0.entities.Actor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HUD extends Drawable {
	
	private Actor actor;
	
	private BitmapFont font;
	
	private String mode = "";
	
	// builder
	public String pallet = "";
	public String selected = "";
	
	// player
	private Drawable bg;
	private Drawable health;
	
	public HUD(Game game) {
		super(game, new Vector2(), "", false);
		
		bg = new Drawable(game, new Vector2(), Assets.TEXTURES_HUD_BG, false);
		health = new Drawable(game, new Vector2(), Assets.TEXTURES_HUD_HEALTH, false);
		
		font = new BitmapFont();
	}
	
	public void render(SpriteBatch spriteBatch) {
		//displayPlayer(spriteBatch);
		displayBuilder(spriteBatch);
		//display(spriteBatch);
		
		actor = null;
		selected = "";
	}
	
	private void display(SpriteBatch spriteBatch) {
		if (game.isBuilder()) {
			mode = "MODE: BUILDER";
		} else {
			mode = "MODE: PLAYER";
		}
		
		// display mode
		font.drawMultiLine(spriteBatch, mode, 20, game.HEIGHT - 15);
	}
	
	private void displayBuilder(SpriteBatch spriteBatch) {
		if (!game.isBuilder()) return;
		
		font.drawMultiLine(spriteBatch, "pallet: " + pallet, 20, 30);
		font.drawMultiLine(spriteBatch, "selected: " + selected, 20, 50);
	}
	
	private void displayPlayer(SpriteBatch spriteBatch) {
		if (actor == null || game.isBuilder() || game.isDebug()) return;
		// health bar
		bg.getSprite().setPosition(game.WIDTH/2, 30);
		bg.getSprite().setScale(100, 1);
		bg.render(spriteBatch);
		health.getSprite().setPosition(game.WIDTH/2, 30);
		health.getSprite().setScale(.8f*100, 1);
		health.render(spriteBatch);
	}

	///// setters and getters /////
	
	public void setActor(Actor actor) {
		this.actor = actor;
	}
}
