package com.austinerb.project0.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class CameraManager {

	private Game game;

	private float scale = 1; // zoom
	
	private OrthographicCamera camera;
	private OrthographicCamera cameraHUD;

	// world cameras
	private OrthographicCamera debugCamera;
	private Box2DDebugRenderer debugRenderer;

	private Drawable cameraFocus;

	public CameraManager(Game game) {
		this.game = game;

		camera = new OrthographicCamera(game.WIDTH, game.HEIGHT);
		camera.position.set(0, 0, 0);
		
		cameraHUD = new OrthographicCamera(game.WIDTH, game.HEIGHT);
		cameraHUD.position.set(game.WIDTH/2, game.HEIGHT/2, 0);
		
		debugCamera = new OrthographicCamera(GameUtil.ptm(game.WIDTH),
				GameUtil.ptm(game.HEIGHT));
		debugCamera.position.set(0, 0, 0);
		debugRenderer = new Box2DDebugRenderer();

		cameraFocus = new Drawable();
	}

	public void update() {
		if (game.isBuilder()) {
			camera.viewportWidth = game.WIDTH * scale;
			camera.viewportHeight = game.HEIGHT * scale;
			debugCamera.viewportWidth = GameUtil.ptm(game.WIDTH) * scale;
			debugCamera.viewportHeight = GameUtil.ptm(game.HEIGHT) * scale;
		} else {
			camera.viewportWidth = game.WIDTH;
			camera.viewportHeight = game.HEIGHT;
			debugCamera.viewportWidth = GameUtil.ptm(game.WIDTH);
			debugCamera.viewportHeight = GameUtil.ptm(game.HEIGHT);
		}

		camera.position.x = cameraFocus.getFocus().x;
		camera.position.y = cameraFocus.getFocus().y;

		if (!game.isBuilder()) {
			// constrain x position
			if (camera.position.x - camera.viewportWidth / 2 < game.getSceneHandler()
					.getCurrentScene().getLowerBounds().x) {
				camera.position.x = game.getSceneHandler().getCurrentScene()
						.getLowerBounds().x + camera.viewportWidth / 2;

			} else if (camera.position.x + camera.viewportWidth / 2 > game.getSceneHandler()
					.getCurrentScene().getUpperBounds().x) {
				camera.position.x = game.getSceneHandler().getCurrentScene()
						.getUpperBounds().x - camera.viewportWidth / 2;

			}
			// constrain y position
			if (camera.position.y - camera.viewportHeight / 2 < game.getSceneHandler()
					.getCurrentScene().getLowerBounds().y) {
				camera.position.y = game.getSceneHandler().getCurrentScene()
						.getLowerBounds().y + camera.viewportHeight / 2;

			} else if (camera.position.y + camera.viewportHeight / 2 > game.getSceneHandler()
					.getCurrentScene().getUpperBounds().y) {
				camera.position.y = game.getSceneHandler().getCurrentScene()
						.getUpperBounds().y - camera.viewportHeight / 2;

			}
		}

		debugCamera.position.x = GameUtil.ptm(camera.position.x);
		debugCamera.position.y = GameUtil.ptm(camera.position.y);

		camera.update();
		cameraHUD.update();
		debugCamera.update();
	}
	
	public void render() {
		if (game.isDebug()) {
			debugRenderer.render(game.getSceneHandler().getCurrentWorld(),
					debugCamera.combined);
		}
	}

	/**
	 *  convert a screen position to global
	 * @param sx
	 * @param sy
	 * @return
	 */
	public Vector2 screenToGlobalPos(float sx, float sy) {
		float x = camera.position.x + scale * (sx - game.WIDTH / 2);
		float y = camera.position.y + scale * (sy - game.HEIGHT / 2);

		return new Vector2(x, y);
	}

	public void setFocus(Drawable drawable) {
		cameraFocus = drawable;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public OrthographicCamera getDebugCamera() {
		return debugCamera;
	}
	
	public OrthographicCamera getCameraHUD() {
		return cameraHUD;
	}
	
	public float getScale() {
		return scale;
	}

	public void incScale(float f) {
		scale += f;
	}
}
