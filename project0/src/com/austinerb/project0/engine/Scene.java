package com.austinerb.project0.engine;

import java.util.ArrayList;

import com.austinerb.project0.lights.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Scene {
	
	protected Game game;

	protected Array<Body> allBodies;
	protected World world; 
	protected ArrayList<Drawable> allDrawables;
	protected ArrayList<Drawable> toRemove;

	protected Vector2 lowerBounds;
	protected Vector2 upperBounds;
	
	// for lights
	protected RayHandler rayHandler;

	public Scene(Game game) {
		this.game = game;

		allBodies = new Array<Body>();
		world = new World(new Vector2(0, -9.8f), true);
		world.setContactListener(new CollisionListener());

		allDrawables = new ArrayList<Drawable>();
		toRemove = new ArrayList<Drawable>();

		lowerBounds = new Vector2();
		upperBounds = new Vector2();
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(new Color(.9f, .9f, 1, 0.05f));
		rayHandler.setBlur(true);
		rayHandler.setBlurNum(2);
	}

	// update the update and view range
	// renders background and foreground parallax drawables
	public void update() {
		// update lights
		rayHandler.setCombinedMatrix(game.getCameraManager().getDebugCamera().combined);
		
		// steps the world
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		for (int i = 0; i < allDrawables.size(); i++) {
			Drawable d = allDrawables.get(i);
			initializer(d);
			render(d);
			d.update();
			updateDimensions(d, i);
			
			// remove from scene
			if (d.isRemove()) {
				toRemove.add(d);
				allDrawables.remove(i);
				i--;
			}
		}
		
		remove();
	}

	protected void render(Drawable drawable) {
		game.getRenderer().addQueue(drawable);
	}

	// add entity into the world if it hasn't been yet
	private void initializer(Drawable d) {
		if (d instanceof Entity) {
			((Entity) d).init(world);
		}
	}
	
	private void remove() {
		for (int i = 0; i < toRemove.size(); i++) {
			Drawable d = toRemove.get(i);
			d.remove(0);
			game.getTrash().add(d);
			toRemove.remove(i);
			i--;
		}
	}

	//
	private void updateDimensions(Drawable d, int i) {
		if (d.sprite == null) return;
		
		if (i == 0) {
			lowerBounds.x = d.getPosition().x - d.sprite.getWidth()/2;
			lowerBounds.y = d.getPosition().y - d.sprite.getHeight()/2;
			upperBounds.x = d.getPosition().x + d.sprite.getWidth()/2;
			upperBounds.y = d.getPosition().y + d.sprite.getHeight()/2;
			return;
		}
		
		// lower bounds
		if (d.getPosition().x - d.sprite.getWidth()/2 < lowerBounds.x) {
			lowerBounds.x = d.getPosition().x - d.sprite.getWidth()/2;
		}
		if (d.getPosition().y - d.sprite.getHeight()/2 < lowerBounds.y) {
			lowerBounds.y = d.getPosition().y - d.sprite.getHeight()/2;
		}
		// upper bounds
		if (d.getPosition().x + d.sprite.getWidth()/2 > upperBounds.x) {
			upperBounds.x = d.getPosition().x + d.sprite.getWidth()/2;
		}
		if (d.getPosition().y + d.sprite.getHeight()/2 > upperBounds.y) {
			upperBounds.y = d.getPosition().y + d.sprite.getHeight()/2;
		}
	}

	// move down the render order
	public void moveDownLayer(Drawable d) {
		int index = allDrawables.indexOf(d);
		
		if (index == -1 || index == 0) return;
		
		Drawable temp = allDrawables.get(index-1);
		allDrawables.set(index-1, d);
		allDrawables.set(index, temp);
	}
	
	// move up the render order
	public void moveUpLayer(Drawable d) {
		int index = allDrawables.indexOf(d);
		
		if (index == -1 || index == allDrawables.size()-1) return;
		
		Drawable temp = allDrawables.get(index+1);
		allDrawables.set(index+1, d);
		allDrawables.set(index, temp);
	}
	
	// //// setters and getters /////////

	public Drawable getByLoc(float x, float y) {
		Drawable d = null;
		for (int i = 0; i < allDrawables.size(); i++) {
			if (allDrawables.get(i).contains(GameUtil.ptm(x), GameUtil.ptm(y))) {
				d = allDrawables.get(i);
			}
		}

		return d;
	}
	
	public void wakeAll() {
		world.getBodies(); //allbodies
		for (int i = 0; i < allBodies.size; i++) {
			allBodies.get(i).setAwake(true);
		}
	}

	public void setLightsEnabled(boolean flag) {
		if (flag) {
			rayHandler.setAmbientLight(new Color(.9f, .9f, 1, 0.01f));
		} else {
			rayHandler.setAmbientLight(new Color(0,0,0,1));
		}
	}
	
	public void add(Drawable d) {
		allDrawables.add(d);
	}

	public Drawable get(int index) {
		return allDrawables.get(index);
	}

	public World getWorld() {
		return world;
	}

	public Vector2 getLowerBounds() {
		return lowerBounds;
	}

	public Vector2 getUpperBounds() {
		return upperBounds;
	}
	
	public RayHandler getRayHandler() {
		return rayHandler;
	}
	
}
