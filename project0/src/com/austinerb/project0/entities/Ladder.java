package com.austinerb.project0.entities;

import com.austinerb.project0.engine.BodyEditorLoader;
import com.austinerb.project0.engine.Game;
import com.austinerb.project0.engine.GameUtil;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ladder extends Model {

	public Ladder(Game game, Vector2 location, String assetName) {
		super(game, location, assetName);
	}
	
	public void createBody(World world) {
		// 0. Create a loader for the file saved from the editor.
	    BodyEditorLoader loader = new BodyEditorLoader((FileHandle)game.getResourceManager().getResource(assetName + ".json"));
	 
	    // 1. Create a BodyDef, as usual.
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.StaticBody;
	 
	    // 2. Create a FixtureDef, as usual.
	    FixtureDef fd = new FixtureDef();
	    fd.density = 1;
	    fd.friction = 0.5f;
	    fd.restitution = 0f;
	    fd.isSensor = true;
	    fd.filter.categoryBits = FilterData.ENTITY;
	 
	    // 3. Create a Body, as usual.
	    body = world.createBody(bd);
	    body.setTransform(GameUtil.ptm(position.x), GameUtil.ptm(position.y), 0);
	    body.setUserData(this);
	 
	    // 4. Create the body fixture automatically by using the loader.
	    loader.attachFixture(body, assetName, fd, GameUtil.ptm(sprite.getWidth()));
	}
	
	public Ladder copy(Vector2 location) {
		return new Ladder(game, location, assetName);
	}
}
