package com.austinerb.project0.entities;

import java.util.ArrayList;

import com.austinerb.project0.engine.Entity;
import com.austinerb.project0.engine.Game;
import com.austinerb.project0.engine.GameUtil;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

// can by controlled and has animation

public class Enemy extends Entity {

	// ///////// movement
	protected boolean canMoveLeft = true;
	protected boolean canMoveRight = true;
	protected boolean moveLeft = false;
	protected boolean moveRight = false;
	protected boolean grounded = false;
	protected boolean jump = false;
	protected int jumpWait = 0;
	protected boolean isClimbing = false;
	protected boolean isClimbingUp = false;
	protected boolean isClimbingDown = false;

	// entities can be added to this list for interaction
	protected ArrayList<Entity> interactionEntities = new ArrayList<Entity>();

	// //////// fixtures
	protected int groundSensor = 0;
	protected int wallSensorLeft = 1;
	protected int wallSensorRight = 2;
	protected int interactionSensor = 3;
	protected int groundContact = 4;

	// can jump if sensor contacts is greater than
	protected int sensorContacts = 0;
	protected int ladderContacts = 0;

	protected ArrayList<Contact> platformContacts = new ArrayList<Contact>();
	protected boolean canCollidePlatform = true;

	// ///////// player attributes
	protected float jumpVel = .3f;
	protected float movementVel = 10f;

	// mask
	private short mask = FilterData.NONE;
	
	// pathfinding
	private Vector2 destination;
	private float proximity = 10f;

	public Enemy(Game game, Vector2 location, String assetName) {
		super(game, location, assetName, true);
		destination = location;
	}

	public void update() {
		super.update();

		if (body == null)
			return;
		
		ai();
		//setFocus();
		handleMovements();
		resetVariables();
	}

	// ///////// control : AI ///////////

	protected void ai() {
		//if (game.isBuilder())
		//	return;
		
		isClimbing = true;
		locateDestination();
		pathFind();
	}
	
	private void locateDestination() {
		for (int i = 0; i < interactionEntities.size(); i++) {
			Entity e = interactionEntities.get(i);
			if (e instanceof Node) {
				if (((Node)e).getBestPath() != null) { 
					destination = ((Node)e).getBestPath().getPosition();
					return;
				}
			}
		}
	}
	
	private void pathFind() {
		float xdist = destination.x - position.x;
		float ydist = destination.y - position.y;
		
		if (Math.abs(xdist) > proximity) {
			if (xdist > 0) {
				moveRight = true;
			} else {
				moveLeft = true;
			}
		}
		if (Math.abs(ydist) > proximity) {
			if (ydist > 0) {
				isClimbingUp = true;
			} else {
				isClimbingDown = true;
			}
		}
	}
	
	/////////////////////////////////
	private void setFocus() {
		if (game.isBuilder())
			return;

		game.getCameraManager().setFocus(this);
	}

	// //////// movement ///////////

	protected void handleMovements() {
		jumpWait--;

		if (sensorContacts > 0 && jumpWait < 0) {
			grounded = true;
		}

		if (grounded) {
			canMoveRight = true;
			canMoveLeft = true;
		}

		// climbing
		if (isClimbing) {
			stopMovement();
		}
		if (isClimbingUp) {
			moveUp();
		} else if (isClimbingDown) {
			moveDown();
		}

		// movements
		if (moveLeft && canMoveLeft) {
			moveLeft();
		} else if (moveRight && canMoveRight) {
			moveRight();
		} else {
			stopMovementx();
		}

		// animations
		if (isClimbing) {
			animation.set("climbing", false);
		} else if (moveLeft && canMoveLeft) {
			animation.set("walking", true);
		} else if (moveRight && canMoveRight) {
			animation.set("walking", false);
		} else if (grounded) {
			animation.set("idle", false);
		} else {
			animation.set("air", true);
		}

		if (grounded && jump) {
			jump();
			jumpWait = 15;
		}
	}

	protected void moveLeft() {
		float yvel = body.getLinearVelocity().y;

		body.setLinearVelocity(-movementVel, yvel);
	}

	protected void moveRight() {
		float yvel = body.getLinearVelocity().y;

		body.setLinearVelocity(movementVel, yvel);
	}

	protected void jump() {
		body.applyLinearImpulse(0, jumpVel, body.getPosition().x,
				body.getPosition().y); // , true);
	}

	protected void moveUp() {
		float xvel = body.getLinearVelocity().x;

		body.setLinearVelocity(xvel, movementVel / 3);
	}

	protected void moveDown() {
		float xvel = body.getLinearVelocity().x;

		body.setLinearVelocity(xvel, -movementVel / 3);
	}

	protected void stopMovementx() {
		float yvel = body.getLinearVelocity().y;
		body.setLinearVelocity(0, yvel);
	}

	protected void stopMovementy() {
		float xvel = body.getLinearVelocity().x;
		body.setLinearVelocity(xvel, 0);
	}

	protected void stopMovement() {
		body.setLinearVelocity(0, 0);
	}

	// ////// collision handling ///////

	public Contact beginContact(Fixture fixture, Fixture contact, Contact c) {
		// platform
		if (contact.getBody().getUserData() instanceof Platform
				&& (body.getLinearVelocity().y > 0 && !fixture.isSensor()
						&& c.getWorldManifold().getNormal().y < 0 || !canCollidePlatform)) {
			c.setEnabled(false);
			platformContacts.add(c);
		}

		// climb
		if (contact.getBody().getUserData() instanceof Ladder
				&& !fixture.isSensor()) {
			ladderContacts++;
		}

		if (fixture.getUserData() == null)
			return c;

		// interaction
		if (fixture.getUserData().equals(interactionSensor)) {
			addInteractionEnity((Entity) contact.getBody().getUserData());
		}

		// jump
		if (fixture.getUserData().equals(groundSensor) && !contact.isSensor()) {
			sensorContacts++;
		}

		return c;
	}

	public Contact preSolve(Fixture fixture, Fixture contact, Contact c) {
		// platform
		if (containsContact(c)) {
			c.setEnabled(false);
		}

		if (contact.getBody().getUserData() instanceof Platform
				&& !containsContact(c) && !canCollidePlatform) {
			c.setEnabled(false);
			platformContacts.add(c);
		}

		if (fixture.getUserData() == null)
			return c;

		// prevent wall sticking
		if (fixture.getUserData().equals(wallSensorLeft) && !contact.isSensor()) {
			canMoveLeft = false;
		}
		if (fixture.getUserData().equals(wallSensorRight)
				&& !contact.isSensor()) {
			canMoveRight = false;
		}

		return c;
	}

	public Contact postSolve(Fixture fixture, Fixture contact, Contact c) {
		// platform
		if (containsContact(c)) {
			c.setEnabled(true);
		}

		if (fixture.getUserData() == null)
			return c;

		// prevent wall sticking
		if (fixture.getUserData().equals(wallSensorLeft)) {
			canMoveLeft = false;
		}
		if (fixture.getUserData().equals(wallSensorRight)) {
			canMoveRight = false;
		}

		return c;
	}

	public Contact endContact(Fixture fixture, Fixture contact, Contact c) {
		// platform
		if (containsContact(c)) {
			c.setEnabled(true);
			removeContact(c);
		}

		// climb
		if (contact.getBody().getUserData() instanceof Ladder
				&& !fixture.isSensor()) {
			ladderContacts--;
		}

		if (fixture.getUserData() == null)
			return c;

		// interaction
		if (fixture.getUserData().equals(interactionSensor)) {
			removeInteractionEnity((Entity) contact.getBody().getUserData());
		}

		// jump
		if (fixture.getUserData().equals(groundSensor) && !contact.isSensor()) {
			sensorContacts--;
		}

		return c;
	}

	private boolean containsContact(Contact contact) {
		for (int i = 0; i < platformContacts.size(); i++) {
			Contact temp = platformContacts.get(i);

			if (contact.getFixtureA().equals(temp.getFixtureA())
					&& contact.getFixtureB().equals(temp.getFixtureB())) {
				return true;
			}
			if (contact.getFixtureA().equals(temp.getFixtureB())
					&& contact.getFixtureB().equals(temp.getFixtureA())) {
				return true;
			}
		}

		return false;
	}

	private void removeContact(Contact contact) {
		for (int i = 0; i < platformContacts.size(); i++) {
			Contact temp = platformContacts.get(i);
			if (contact.getFixtureA().equals(temp.getFixtureA())
					&& contact.getFixtureB().equals(temp.getFixtureB())) {
				platformContacts.remove(i);
				return;
			}
			if (contact.getFixtureA().equals(temp.getFixtureB())
					&& contact.getFixtureB().equals(temp.getFixtureA())) {
				platformContacts.remove(i);
				return;
			}
		}
	}

	// ////// other /////////

	protected void updatePosition() {
		if (body != null) {
			position.x = GameUtil.mtp(body.getPosition().x);
			position.y = GameUtil.mtp(body.getPosition().y
					- body.getFixtureList().get(0).getShape().getRadius());
			angle = body.getAngle();
		}
		if (sprite != null) {
			sprite.setPosition(position.x, position.y);
			sprite.setRotation(angle);
		}
		if (animation != null) {
			animation.update(position, angle);
		}
	}

	protected void resetVariables() {
		canMoveLeft = true;
		canMoveRight = true;
		moveLeft = false;
		moveRight = false;
		grounded = false;
		jump = false;
		isClimbingUp = false;
		isClimbingDown = false;
		isClimbing = false;
	}

	// default character body
	public void createBody(World world) {
		float scale = 0.06f;

		// create body
		BodyDef bd = new BodyDef();
		bd.fixedRotation = true;
		bd.type = BodyType.DynamicBody;
		body = world.createBody(bd);
		body.setTransform(GameUtil.ptm(position.x), GameUtil.ptm(position.y), 0);
		body.setSleepingAllowed(false);

		// ////// fixtures
		// ground contact point
		CircleShape cs = new CircleShape();
		cs.setRadius(scale);
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 1f;
		fd.friction = .5f;
		fd.restitution = 0f;
		fd.filter.categoryBits = FilterData.ACTOR;
		fd.filter.maskBits = mask;
		body.createFixture(fd).setUserData(groundContact);

		// ground sensor
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(scale / 2, scale / 2, new Vector2(0, -scale), 0);
		fd = new FixtureDef();
		fd.shape = ps;
		fd.density = 0f;
		fd.friction = .5f;
		fd.restitution = 0f;
		fd.isSensor = true;
		fd.filter.categoryBits = FilterData.SENSOR;
		body.createFixture(fd).setUserData(groundSensor);

		// left main body of the character
		ps = new PolygonShape();
		ps.setAsBox(scale / 2, scale * 3.5f, new Vector2(
				-scale / 2 + scale / 5, scale * 3.5f), 0);
		fd = new FixtureDef();
		fd.shape = ps;
		fd.density = 1f;
		fd.friction = .5f;
		fd.restitution = 0f;
		fd.filter.categoryBits = FilterData.ACTOR;
		fd.filter.maskBits = mask;
		body.createFixture(fd).setUserData(wallSensorLeft);

		// right main body of the character
		ps = new PolygonShape();
		ps.setAsBox(scale / 2, scale * 3.5f, new Vector2(scale / 2 - scale / 5,
				scale * 3.5f), 0);
		fd = new FixtureDef();
		fd.shape = ps;
		fd.density = 1f;
		fd.friction = .5f;
		fd.restitution = 0f;
		fd.filter.categoryBits = FilterData.ACTOR;
		fd.filter.maskBits = mask;
		body.createFixture(fd).setUserData(wallSensorRight);

		// head
		cs = new CircleShape();
		cs.setRadius(scale);
		cs.setPosition(new Vector2(0, scale * 7f));
		fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 1f;
		fd.friction = .5f;
		fd.restitution = 0f;
		fd.filter.categoryBits = FilterData.ACTOR;
		fd.filter.maskBits = mask;
		body.createFixture(fd);

		// interaction sensor
		ps = new PolygonShape();
		ps.setAsBox(scale * 4, scale * 8, new Vector2(0, scale * 4), 0);
		fd = new FixtureDef();
		fd.shape = ps;
		fd.density = 0f;
		fd.friction = .5f;
		fd.restitution = 0f;
		fd.isSensor = true;
		fd.filter.categoryBits = FilterData.SENSOR;
		body.createFixture(fd).setUserData(interactionSensor);

		// setting user data
		body.setUserData(this);
	}

	// /////// Setters and Getters ///////////

	public Class<?> getSaveClass() {
			return Enemy.class;
	}

	public Enemy copy(Vector2 location) {
		return new Enemy(game, location, assetName);
	}

	public float getJumpVel() {
		return jumpVel;
	}

	public void setJumpVel(float jumpVel) {
		this.jumpVel = jumpVel;
	}

	protected void addInteractionEnity(Entity e) {
		if (e.equals(this))
			return; // if e is this
		if (interactionEntities.indexOf(e) != -1)
			return; // if it is already in the array

		interactionEntities.add(e);
	}

	protected void removeInteractionEnity(Entity e) {
		interactionEntities.remove(e);
	}
}
