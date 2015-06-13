package com.austinerb.project0.engine;

import java.util.ArrayList;

import com.austinerb.project0.lights.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

	// renders all drawable objects added to this class
	// does NOT take into account if each drawable object is visible within the camera

public class Renderer {

	private SpriteBatch spriteBatch;
	private ArrayList<Drawable> queuedDrawables;
	
	// for ray casts, etc..
	private ShapeRenderer shapeRenderer;
	private ArrayList<Line> queuedLines;
	
	private boolean doRender = true;
	
	private Drawable hud;
	
	public Renderer() {
		spriteBatch = new SpriteBatch();
		queuedDrawables = new ArrayList<Drawable>();
		
		shapeRenderer = new ShapeRenderer();
		queuedLines = new ArrayList<Line>();
	}
	
	public void addQueue(Drawable drawable){
		queuedDrawables.add(drawable);
	}
	
	public void addQueueLine(Line line){
		queuedLines.add(line);
	}
	
	public void clearQueue() {
		queuedDrawables = new ArrayList<Drawable>();
		queuedLines = new ArrayList<Line>();
	}
	
	public void render(Matrix4 matrix) {
		// clears the screen
		if (!doRender) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			return;
		}
		
		Gdx.gl.glClearColor(.5f, .4f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// renders the queue
		spriteBatch.setProjectionMatrix(matrix);
		spriteBatch.begin();
		
		for(int i = 0; i < queuedDrawables.size(); i++) {
			if (queuedDrawables.get(i).getShouldRender()) {
				queuedDrawables.get(i).render(spriteBatch);
			}
		}

		spriteBatch.end();
	}
	
	public void renderLights(RayHandler rh) {
		if (!doRender) return;
		
		rh.updateAndRender();
	}
	
	// used for hud
	public void renderHUD(Matrix4 matrix) {
		if (!doRender) return;
		
		// renders the queue
		spriteBatch.setProjectionMatrix(matrix);
		spriteBatch.begin();
		
		if (hud != null) 
			hud.render(spriteBatch);
		
		spriteBatch.end();
		clearQueue();
	}
	
	//
	public void renderLines(Matrix4 matrix) {
		if (!doRender) return;
		
		shapeRenderer.setProjectionMatrix(matrix);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.begin(ShapeType.Line);
		for(int i = 0; i < queuedLines.size(); i++) {
			Line l = queuedLines.get(i);
			shapeRenderer.line(l.p1.x, l.p1.y, l.p2.x, l.p2.y);
		}
		shapeRenderer.end();
	}
	
	public void setDoRender(boolean flag) {
		doRender = flag;
	}
	
	public void setHud(Drawable hud) {
		this.hud = hud;
	}
}
