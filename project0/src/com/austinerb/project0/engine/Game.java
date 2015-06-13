package com.austinerb.project0.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;

public class Game implements ApplicationListener {
	public float WIDTH;
	public float HEIGHT;

	private FPSLogger fps;
	private CameraManager cameraManager;
	private ResourceManager resourceManager;
	private Renderer renderer;
	private SceneHandler sceneHandler;
	private Trash trash;
	private HUDManager hudmanager;
	private Builder builder;

	// modes
	private boolean isDebug = false; // displays debug render view
	private boolean isBuilder = false; // allows editing of the level

	@Override
	public void create() {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		fps = new FPSLogger();
		resourceManager = new ResourceManager();
		renderer = new Renderer();
		cameraManager = new CameraManager(this);
		sceneHandler = new SceneHandler(this);
		hudmanager = new HUDManager(this);
		builder = new Builder(this);
		trash = new Trash();
	}

	@Override
	public void render() {
		fps.log();

		// update
		cameraManager.update();
		sceneHandler.update();
		hudmanager.update();
		trash.update();
		builder.update();

		// render
		renderer.render(cameraManager.getCamera().combined);
		renderer.renderLights(sceneHandler.getCurrentScene().rayHandler);
		renderer.renderLines(cameraManager.getCamera().combined);
		renderer.renderHUD(cameraManager.getCameraHUD().combined);
		renderer.clearQueue();
		cameraManager.render();

		handleInput();
	}

	private void handleInput() {
		// set mode: player/builder
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			isBuilder = false;
			isDebug = false;
		} else if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			isBuilder = true;
			isDebug = true;
		} 

		// enable/disable debug mode
		if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
			isDebug = true;
		} else if (Gdx.input.isKeyPressed(Keys.NUM_4)) {
			isDebug = false;
		} 
		// enable/disable lights
		if (Gdx.input.isKeyPressed(Keys.NUM_5)) {
			sceneHandler.getCurrentScene().setLightsEnabled(false);
		} else if (Gdx.input.isKeyPressed(Keys.NUM_6)) {
			sceneHandler.getCurrentScene().setLightsEnabled(true);
		} 
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE) && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			Gdx.app.exit();
		}
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public HUDManager getHUDManager() {
		return hudmanager;
	}

	public SceneHandler getSceneHandler() {
		return sceneHandler;
	}
	
	public boolean isDebug() {
		return isDebug;
	}

	public boolean isBuilder() {
		return isBuilder;
	}
	
	public Trash getTrash() {
		return trash;
	}
	
	// ////////////////////

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
