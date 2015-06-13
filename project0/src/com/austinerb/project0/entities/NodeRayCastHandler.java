package com.austinerb.project0.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class NodeRayCastHandler implements RayCastCallback {

	public ArrayList<Vector2> point = new ArrayList<Vector2>();
	public ArrayList<Vector2> normal = new ArrayList<Vector2>();;
	public ArrayList<Fixture> fixture = new ArrayList<Fixture>();;
	public ArrayList<Float> fraction = new ArrayList<Float>();;
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {		
		if (fixture.getBody().getUserData() instanceof Node) {
			this.point.add(point);
			this.normal.add(normal);
			this.fixture.add(fixture);
			this.fraction.add(fraction);
		}
		
		return -1;
	}

	public ArrayList<Vector2> getPoint() {
		return point;
	}
	
	public ArrayList<Vector2> getNormal() {
		return normal;
	}
	
	public ArrayList<Fixture> getFixture() {
		return fixture;
	}

	public ArrayList<Float> getFraction() {
		return fraction;
	}
}
