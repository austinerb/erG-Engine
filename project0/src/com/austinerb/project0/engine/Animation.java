package com.austinerb.project0.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.player.SpriterPlayer;

// animation created with spriter
// anchor point of animation must always be at the bottom left most corner

public class Animation {
	
	private SpriterPlayer player;
	private SpriterLoader loader;
	
	private boolean inverted; // inverted animation
	
	public Animation(String fileName) {
		loader = new SpriterLoader();
		Spriter spriter = Spriter.getSpriter(fileName, loader);
		player = new SpriterPlayer(spriter, 0, loader);
		
		inverted = false;
	}
	
	public Animation(SpriterPlayer player, SpriterLoader spriterLoader) {
		this.player = player;
		this.loader = spriterLoader;
		
		inverted = false;
	}
	
	public void update(Vector2 globalPosition, float angle) {
		player.setAngle(angle);
		player.update(globalPosition.x, globalPosition.y);
	}
	
	// render the current frame at 
	public void render(SpriteBatch spriteBatch) {
		new SpriterDrawer(loader, spriteBatch).draw(player);
	}
	
	/////// setters and getters ///////
	public void set(String name, boolean inverted) {
		player.setAnimation(name);
		
		if (inverted != this.inverted) {
			player.flipX();
		} 
		
		this.inverted = inverted;
	}
}
