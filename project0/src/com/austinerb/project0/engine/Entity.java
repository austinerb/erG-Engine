package com.austinerb.project0.engine;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

// has collision data (body in a world)

public class Entity extends Drawable {
	protected Random random;

	protected Body body;
	protected BodyType type = BodyType.StaticBody;

	protected boolean isStatic;
	protected boolean isPermeable;
	protected boolean isImpermeable;

	public Entity(Game game, Vector2 location, String assetName,
			boolean isAnimated) {
		super(game, location, assetName, isAnimated);

		random = new Random();

		isStatic = false;
		isPermeable = false;
		isImpermeable = false;
	}

	// called in scene class after the world has been initialized
	// to be implemented by sub class
	public void init(World world) {
		if (body != null) return;
		
		createBody(world);
		
		if (body == null) return;
		
		body.setTransform(body.getPosition(), GameUtil.dtr(angle));
	}
	
	public void createBody(World world) {}
	
	public void render(SpriteBatch spriteBatch) {
		super.render(spriteBatch);
	}

	public void update() {
		super.update();
	}

	protected void updatePosition() {
		if (body != null) {
			position.x = GameUtil.mtp(body.getPosition().x);
			position.y = GameUtil.mtp(body.getPosition().y);
			angle = GameUtil.rtd(body.getAngle());
		}
		if (sprite != null) {
			sprite.setPosition(position.x - sprite.getWidth()/2, position.y - sprite.getHeight()/2);
			sprite.setRotation(angle);
		}
		if (animation != null) {
			animation.update(position, angle);
		}
	}

	public void setPosition(Vector2 newPos) {
		if (body == null) return;
		body.setTransform(new Vector2(GameUtil.ptm(newPos.x), GameUtil.ptm(newPos.y)), GameUtil.dtr(angle));
		updatePosition();
	}
	
	public void setAngle(float angle) {
		if (body != null) 
			body.setTransform(body.getPosition(), GameUtil.dtr(angle));
		this.angle = angle;
	}
	
	public void remove(int i) {
		super.remove(i);
		if (body == null) return;

		if (i == 0)
			body.setTransform(-1000000, -1000000, angle);
		else 
			game.getSceneHandler().getCurrentWorld().destroyBody(body);
	}

	// ////// collision handling ///////

	// to be implemented by sub class
	public Contact beginContact(Fixture fixture, Fixture contact, Contact c) {
		return c;
	}
	
	public Contact preSolve(Fixture fixture, Fixture contact, Contact c) {
		return c;
	}
	
	public Contact postSolve(Fixture fixture, Fixture contact, Contact c) {
		return c;
	}

	public Contact endContact(Fixture fixture, Fixture contact, Contact c) {
		return c;
	}	

	// ////// setters and getters ////////

	public void setEditing(boolean flag) {
		super.setEditing(flag);

		if (body == null)
			return;

		if (isEditing) {
			type = body.getType();
			body.setType(BodyType.StaticBody);
		} else {
			body.setType(type);
		}
	}

	public boolean contains(float x, float y) {
		if (body == null)
			return false;

		ArrayList<Fixture> fixtures = body.getFixtureList();

		for (int i = 0; i < fixtures.size(); i++) {
			if (fixtures.get(i).testPoint(x, y)) {
				return true;
			}
		}

		return false;
	}

	public Entity copy(Vector2 location) {
		return new Entity(game, location, assetName, isAnimated);
	}
	
	public void setIsStatic() {
		isStatic = true;
	}

	public void setIsPermeable() {
		isPermeable = true;
	}

	public boolean getIsPermeable() {
		return isPermeable;
	}

	public void setIsImpermeable() {
		isImpermeable = true;
	}

	public boolean getIsImpermeable() {
		return isImpermeable;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
