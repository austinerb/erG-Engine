package com.austinerb.project0.entities;

import com.austinerb.project0.engine.Entity;
import com.austinerb.project0.engine.Game;
import com.austinerb.project0.engine.GameUtil;
import com.austinerb.project0.lights.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

// has a light attached to it

public class LightBody extends Entity {
	
	// for developing purposes
	private boolean minimize = false;
	
	private float radius = 3.5f;
	private PointLight pointLight;

	private boolean isOn = false;

	private float intensity = .5f;
	private float currentIntensity = 0;
	// speed at which the light turns on per second
	private float speed = 1f;

	// color
	private float r = 1;
	private float g = .9f;
	private float b = .9f;

	public LightBody(Game game, Vector2 location, String assetName) {
		super(game, location, assetName, false);
	}

	public void createBody(World world) {
		if (minimize) {
			radius = .1f;
			isOn = true;
		}
		
		// create body
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		body = world.createBody(bd);
		body.setTransform(GameUtil.ptm(position.x), GameUtil.ptm(position.y), 0);

		// ////// fixtures
		// light
		CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.isSensor = true;
		body.createFixture(fd);

		// setting user data
		body.setUserData(this);

		// create light
		pointLight = new PointLight(game.getSceneHandler().getCurrentScene()
				.getRayHandler(), 200, new Color(r, g, b, 0), intensity * 10,
				0, 0);
		pointLight.attachToBody(body, 0, 0);
		pointLight.setActive(isOn);
	}

	public void update() {
		super.update();

		turnOn();
	}

	private void turnOn() {
		if (!isOn || currentIntensity == intensity)
			return;

		currentIntensity = Math.min(intensity, currentIntensity
				+ (speed * Gdx.graphics.getDeltaTime()));
		pointLight.setColor(new Color(r, g, b, currentIntensity));
	}

	// turn on light
	public Contact beginContact(Fixture fixture, Fixture contact, Contact c) {
		if (contact.getBody().getUserData() instanceof Actor
				&& ((Actor) contact.getBody().getUserData()).getIsPlayer()) {
			isOn = true;
			pointLight.setActive(true);
		}

		return c;
	}

	protected void updatePosition() {
		if (body != null) {
			position.x = GameUtil.mtp(body.getPosition().x);
			position.y = GameUtil.mtp(body.getPosition().y);
			if (pointLight != null) {
				pointLight.setPosition(body.getPosition().x,
						body.getPosition().y);
			}
		}
	}

	public LightBody copy(Vector2 location) {
		return new LightBody(game, location, assetName);
	}
}
