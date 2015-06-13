package com.austinerb.project0.entities;

import com.austinerb.project0.engine.Entity;
import com.austinerb.project0.engine.Game;
import com.austinerb.project0.engine.GameUtil;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Platform extends Entity {
	
	public Platform(Game game, Vector2 location, String assetName) {
		super(game, location, assetName, false);
	}
	
	public void createBody(World world) {
		if (sprite == null) return;

		// create body
		BodyDef bd = new BodyDef();
		bd.fixedRotation = true;
		bd.type = BodyType.StaticBody;
		body = world.createBody(bd);
		body.setTransform(GameUtil.ptm(position.x), GameUtil.ptm(position.y), 0);

		// ////// fixtures
		// platform
		EdgeShape es = new EdgeShape();
		es.set(-GameUtil.ptm(sprite.getWidth())/2, GameUtil.ptm(sprite.getHeight())/2, GameUtil.ptm(sprite.getWidth())/2, GameUtil.ptm(sprite.getHeight())/2);
		FixtureDef fd = new FixtureDef();
		fd.shape = es;
		fd.density = 1f;
		fd.friction = .5f;
		fd.restitution = 0f;
		fd.filter.categoryBits = FilterData.PLATFORM;
		body.createFixture(fd);
		
		// useless sensor
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(GameUtil.ptm(sprite.getWidth())/2, GameUtil.ptm(sprite.getHeight())/2, new Vector2(), 0);
		fd = new FixtureDef();
		fd.shape = ps;
		fd.isSensor = true;
		body.createFixture(fd);
		
		// setting user data
		body.setUserData(this);
	}
	
	public Platform copy(Vector2 location) {
		return new Platform(game, location, assetName);
	}
}
