package com.austinerb.project0.engine;

import com.badlogic.gdx.physics.box2d.World;

// updates the current scene
// switches between scenes

public class SceneHandler {

	private Scene currentScene;
	
	public SceneHandler(Game game) {
		currentScene = LevelEditor.load(game, "assets/world_0.txt");
	}
	
	public void update() {
		currentScene.update();
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public World getCurrentWorld() {
		return currentScene.getWorld();
	}
}
