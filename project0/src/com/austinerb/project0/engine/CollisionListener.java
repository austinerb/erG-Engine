package com.austinerb.project0.engine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		if (entityA == null || entityB == null) return;
		
		contact = entityA.beginContact(contact.getFixtureA(), contact.getFixtureB(), contact);
		contact = entityB.beginContact(contact.getFixtureB(), contact.getFixtureA(), contact);
	}

	@Override
	public void endContact(Contact contact) {
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		if (entityA == null || entityB == null) return;
		
		contact = entityA.endContact(contact.getFixtureA(), contact.getFixtureB(), contact);
		contact = entityB.endContact(contact.getFixtureB(), contact.getFixtureA(), contact);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		if (entityA == null || entityB == null) return;
		
		contact = entityA.preSolve(contact.getFixtureA(), contact.getFixtureB(), contact);
		contact = entityB.preSolve(contact.getFixtureB(), contact.getFixtureA(), contact);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		if (entityA == null || entityB == null) return;
		
		contact = entityA.postSolve(contact.getFixtureA(), contact.getFixtureB(), contact);
		contact = entityB.postSolve(contact.getFixtureB(), contact.getFixtureA(), contact);
	}
}