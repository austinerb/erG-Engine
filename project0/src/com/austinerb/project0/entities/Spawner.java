package com.austinerb.project0.entities;

import com.austinerb.project0.engine.Assets;
import com.austinerb.project0.engine.Drawable;
import com.austinerb.project0.engine.Entity;
import com.austinerb.project0.engine.Game;
import com.austinerb.project0.engine.GameUtil;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Spawner extends Entity {
	
	private float width = .12f;
	private float height = .2f;
			
	// spawn limit
	private int limit = 1;
	// how many have spawned
	private int count = 0;
	// what this spawns
	private Drawable item;
	
	private int timeout = 0;
	private int timeoutL = 15;
	
	public Spawner(Game game, Vector2 location, String assetName) {
		super(game, location, Assets.NI_SPAWNER, false);
		
		item = new Actor(game, new Vector2(position.x, position.y), assetName);
		((Actor) item).setIsPlayer(true);
	}

	public void update() {
		super.update();
		
		spawn();
	}
	
	private void spawn() {
		if (count >= limit) return;
		
		if (timeout < 0) {	
			Vector2 newPos = new Vector2();
			newPos.x = position.x;
			newPos.y = position.y - GameUtil.mtp(height);
			game.getSceneHandler().getCurrentScene().add((Drawable) item.clone());
			
			count++;
			timeout = timeoutL;
		} 
		
		timeout--;
	}
	
	public void createBody(World world) {
		// create body
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		body = world.createBody(bd);
		body.setTransform(GameUtil.ptm(position.x), GameUtil.ptm(position.y), 0);

		// ////// fixtures
		// shapes
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width, height);

		// fixture definitions
		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.isSensor = true;

		// add fixtures
		body.createFixture(fd);

		// setting user data
		body.setUserData(this);
	}
	
	protected void updatePosition() {
		if (body != null) {
			position.x = GameUtil.mtp(body.getPosition().x);
			position.y = GameUtil.mtp(body.getPosition().y);
		}
	}
	
	public Spawner copy(Vector2 location) {
		return new Spawner(game, location, item.getAssetName());
	}
	
	public Class<?> getSaveClass() {
		return null;
	}
}
