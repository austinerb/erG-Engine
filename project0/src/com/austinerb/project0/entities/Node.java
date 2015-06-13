package com.austinerb.project0.entities;

import java.util.ArrayList;

import com.austinerb.project0.engine.Entity;
import com.austinerb.project0.engine.Game;
import com.austinerb.project0.engine.GameUtil;
import com.austinerb.project0.engine.Line;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

// used for AI path finding

public class Node extends Entity {

	private float radius = 2f;

	private NodeRayCastHandler raycaster = new NodeRayCastHandler();

	// if player is in range
	private boolean hasPlayer = false;
	private Vector2 point1 = new Vector2(), point2 = new Vector2();
	// how far the node will cast a ray
	private float range = GameUtil.mtp(10);
	// distance to child node (in pixels)

	// the nodes this node is pointing at
	private ArrayList<Node> childNodes = new ArrayList<Node>();
	// the nodes that are pointing at this
	private ArrayList<Node> parentNodes = new ArrayList<Node>();

	// closest total distance from player
	private float totalDistance = 0;
	// best node to follow based on its total distance
	private Node bestPath;

	// checked for other nodes
	private boolean checked = false;

	private float time = 0;

	public Node(Game game, Vector2 location, String assetName) {
		super(game, location, assetName, false);
	}

	public void createBody(World world) {
		// create body
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		body = world.createBody(bd);
		body.setTransform(GameUtil.ptm(position.x), GameUtil.ptm(position.y), 0);

		// ////// fixtures
		CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.isSensor = true;
		body.createFixture(fd);

		// setting user data
		body.setUserData(this);
	}

	public void update() {
		super.update();

		time += Gdx.graphics.getDeltaTime();

		updateRay();
		renderRay();
		detectNodes();
		updateDistanceForward();
	}

	// trys to find player going forwards
	private void updateDistanceForward() {
		if (hasPlayer) {
			totalDistance = 0;
			bestPath = this;
			return;
		}

		// set total distance and choose best path
		float lowestDistance = -1;
		Node best = null;

		for (int i = 0; i < parentNodes.size(); i++) {
			// if not a dead end
			if (parentNodes.get(i).totalDistance != -1) {
				// distance between this and the parent node
				float d = parentNodes.get(i).getPosition().dst(position);

				if (parentNodes.get(i).hasPlayer) {
					totalDistance = d;
					bestPath = parentNodes.get(i);
					return;
				}

				if (lowestDistance == -1) {
					lowestDistance = parentNodes.get(i).totalDistance + d;
				} else {
					lowestDistance = Math.min(lowestDistance,
							parentNodes.get(i).totalDistance + d);
				}

				// best path
				if (lowestDistance == parentNodes.get(i).totalDistance + d) {
					best = parentNodes.get(i);
				}
			}
		}

		totalDistance = lowestDistance;
		bestPath = best;

		// deadend
		if (parentNodes.size() == 0 && !hasPlayer) {
			totalDistance = -1;
			bestPath = null;
		}
		
		// try to trace backwards
		if (bestPath != null) return;
		for (int i = 0; i < childNodes.size(); i++) {
			if (childNodes.get(i).totalDistance != -1) {
				bestPath = childNodes.get(i);
			}
		}
	}

	private void updateRay() {
		Vector2 direction = new Vector2();
		direction.x = (float) (Math.cos(GameUtil.dtr(angle)) * range);
		direction.y = (float) (Math.sin(GameUtil.dtr(angle)) * range);

		point1 = new Vector2();
		point1.x = position.x;
		point1.y = position.y;

		point2 = new Vector2();
		point2.x = position.x + direction.x;
		point2.y = position.y + direction.y;
	}

	private void renderRay() {
		if (!game.isDebug())
			return;

		game.getRenderer().addQueueLine(new Line(point1, point2));
	}

	private void detectNodes() {
		if (time < 3)
			return;

		if (body == null)
			return;

		// if it has already checked for other nodes, return
		if (checked)
			return;

		Vector2 p1 = new Vector2();
		p1.x = GameUtil.ptm(point1.x);
		p1.y = GameUtil.ptm(point1.y);

		Vector2 p2 = new Vector2();
		p2.x = GameUtil.ptm(point2.x);
		p2.y = GameUtil.ptm(point2.y);

		// cast ray
		game.getSceneHandler().getCurrentScene().getWorld()
				.rayCast(raycaster, p1, p2);

		// add node to interaction entity list
		for (int i = 0; i < raycaster.getFixture().size(); i++) {
			childNodes.add(((Node) raycaster.getFixture().get(i).getBody()
					.getUserData()));
			childNodes.get(childNodes.size() - 1).addParent(this);
		}

		checked = true;
	}

	public Contact beginContact(Fixture fixture, Fixture contact, Contact c) {
		// add player
		if (contact.getBody().getUserData() instanceof Actor
				&& ((Actor) contact.getBody().getUserData()).getIsPlayer()) {
			hasPlayer = true;
			setHasPlayer(true);
			setHasPlayer(false);
		}

		return c;
	}

	// sets hasPlayer to false for other nodes
	// if true, only set children
	// if false, only set parents
	public void setHasPlayer(boolean flag) {
		if (flag) {
			for (int i = 0; i < childNodes.size(); i++) {
				childNodes.get(i).hasPlayer = false;
				childNodes.get(i).setHasPlayer(flag);
			}
		} else {
			for (int i = 0; i < parentNodes.size(); i++) {
				parentNodes.get(i).hasPlayer = false;
				parentNodes.get(i).setHasPlayer(flag);
			}
		}
	}

	private void addParent(Node node) {
		parentNodes.add(node);
	}

	public Node copy(Vector2 location) {
		return new Node(game, location, assetName);
	}

	public Node getBestPath() {
		return bestPath;
	}
}
