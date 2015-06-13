package com.austinerb.project0.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Drawable implements Cloneable {

	protected Game game;

	protected Animation animation;
	protected Sprite sprite;

	protected Vector2 position; // actual world position
	protected float angle = 0; // in degrees
	
	protected Vector2 focusPosition; // where the camera will focus on
	
	protected String assetName;
	protected boolean isAnimated;
	// should render if visible, or other factors
	protected boolean shouldRender = true; 
	protected boolean remove = false;
	protected boolean isEditing = false;
	
	public Drawable() {
		position = new Vector2(0, 0);
		focusPosition = position;
	}

	public Drawable(Game game, Vector2 position, String assetName,
			boolean isAnimated) {
		this.game = game;
		this.position = position;
		this.focusPosition = this.position;
		this.assetName = assetName;
		this.isAnimated = isAnimated;
		
		create();
	}
	
	// can be called later 
	protected void create() {
		if (isAnimated) {
			animation = new Animation((String) game.getResourceManager()
					.getResource(assetName + ResourceManager.ANIMATION));
		} else {
			Texture texture = (Texture) game.getResourceManager().getResource(
					assetName + ResourceManager.TEXTURE);
			if (texture == null)
				return;
			TextureRegion textureRegion = new TextureRegion(texture);
			sprite = new Sprite(textureRegion);
		}
	}

	public void update() {
		updatePosition();
	}

	public void render(SpriteBatch spriteBatch) {
		if (animation != null) {
			animation.render(spriteBatch);
		}
		if (sprite != null) {
			sprite.draw(spriteBatch);
		}
	}

	// to be implemented by sub class
	protected void updatePosition() {}
	
	// i represents the number of times this method has been called
	public void remove(int i) {}

	public boolean contains(float x, float y) {
		if (sprite == null) return false;
		
		return sprite.getBoundingRectangle().contains(x, y);
	}
	
	// set to return null if not to be saved to file
	public Class<?> getSaveClass() {
		return this.getClass();
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Drawable copy(Vector2 location) {
		return new Drawable(game, location, assetName,
				isAnimated);
	}
	
	public void setEditing(boolean flag) {
		isEditing = flag;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getFocus() {
		return focusPosition;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public boolean getShouldRender() {
		return shouldRender;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public boolean isRemove() {
		return remove;
	}

	public void setRemove() {
		this.remove = true;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}

	public String getAssetName() {
		return assetName;
	}
}
