package com.austinerb.project0.engine;

import java.util.ArrayList;

import com.austinerb.project0.entities.Enemy;
import com.austinerb.project0.entities.Ladder;
import com.austinerb.project0.entities.LightBody;
import com.austinerb.project0.entities.Model;
import com.austinerb.project0.entities.Node;
import com.austinerb.project0.entities.Platform;
import com.austinerb.project0.entities.Spawner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

// allows editing of the current scene

public class Builder extends Drawable {

	
	// TODO: add categories
	
	
	private Game game;

	private Drawable selected;
	private float selectedMovementSpeed = 4;

	// all available drawables that can be placed into the world
	private ArrayList<Drawable> pallet = new ArrayList<Drawable>();
	private int palletIndex = 0;

	private Vector2 cursorPos = new Vector2(0, 0); // in world coordinates
	private float movementSpeed = 10;
	private float rotationSpeed = 5; // degrees

	private float timeout = 0;
	private float timeoutL = 15;

	public Builder(Game game) {
		this.game = game;

		createPallet();
	}

	private void createPallet() {
		//pallet.add(new Node(game, cursorPos, Assets.NI_NODE));
		pallet.add(new Enemy(game, cursorPos, Assets.GUY_ANIMATION));
		pallet.add(new Spawner(game, cursorPos, Assets.GUY_ANIMATION));
		pallet.add(new Platform(game, cursorPos, Assets.TEXTURES_PLATFORM));
		pallet.add(new LightBody(game, cursorPos, Assets.NI_LIGHTBODY));
		pallet.add(new Ladder(game, cursorPos, Assets.MODELS_WORLD_LADDER));
		
		pallet.add(new Model(game, cursorPos, Assets.MODELS_WORLD_LAND_1));
		pallet.add(new Model(game, cursorPos, Assets.MODELS_WORLD_LAND_2));
		pallet.add(new Model(game, cursorPos, Assets.MODELS_WORLD_LAND_3));
		pallet.add(new Model(game, cursorPos, Assets.MODELS_WORLD_LAND_4));	
		pallet.add(new Model(game, cursorPos, Assets.MODELS_WORLD_LAND_5));	
		pallet.add(new Model(game, cursorPos, Assets.MODELS_WORLD_LAND_6));	
	}

	public void update() {
		if (!game.isBuilder()) {
			selected = null;
			return;
		}

		game.getCameraManager().setFocus(this);

		updateCursorPos();
		handleInput();
		updateHUD();
	}

	private void handleInput() {
		// movement
		if (Gdx.input.isKeyPressed(Keys.D)) {
			position.x += movementSpeed;
		} else if (Gdx.input.isKeyPressed(Keys.A)) {
			position.x -= movementSpeed;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			position.y += movementSpeed;
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			position.y -= movementSpeed;
		}
		// zoom
		if (Gdx.input.isKeyPressed(Keys.MINUS)) {
			game.getCameraManager().incScale(.03f);
		} else if (Gdx.input.isKeyPressed(Keys.PLUS)) {
			game.getCameraManager().incScale(-.03f);
		}

		// object placement and selection
		if (selected == null) {
			// mouse
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				add();
			} else if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
				select();
			}
			// cycle
			if (timeout < 0) {
				if (Gdx.input.isKeyPressed(Keys.LEFT)) {
					cycle(1);
					timeout = timeoutL;
				} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
					cycle(-1);
					timeout = timeoutL;
				}
			}
		} else {
			// selected movement
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				selected.setPosition(new Vector2(
						selected.getPosition().x += selectedMovementSpeed,
						selected.getPosition().y));
			} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				selected.setPosition(new Vector2(
						selected.getPosition().x -= selectedMovementSpeed,
						selected.getPosition().y));
			}
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				selected.setPosition(new Vector2(selected.getPosition().x,
						selected.getPosition().y += selectedMovementSpeed));
			} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				selected.setPosition(new Vector2(selected.getPosition().x,
						selected.getPosition().y -= selectedMovementSpeed));
			}
			// rotation
			if (Gdx.input.isKeyPressed(Keys.COMMA)) {
				selected.setAngle(selected.angle += rotationSpeed);
			} else if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
				selected.setAngle(selected.angle -= rotationSpeed);
			}
			// change render order
			if (Gdx.input.isKeyPressed(Keys.O)) {
				game.getSceneHandler().getCurrentScene().moveUpLayer(selected);
			} else if (Gdx.input.isKeyPressed(Keys.L)) {
				game.getSceneHandler().getCurrentScene().moveDownLayer(selected);
			}
			// deselect
			if (Gdx.input.isKeyPressed(Keys.ENTER)) {
				deselect();
			}
			// remove selected
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
				removeSelected();
			}
		}
		timeout--;

		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE) && Gdx.input.isKeyPressed(Keys.NUM_0)) {
			LevelEditor.save(game.getSceneHandler().getCurrentScene(), "assets/world_0.txt");
		}
	}

	private void cycle(int i) {
		palletIndex += i;
		if (palletIndex < 0)
			palletIndex = pallet.size() - 1;
		if (palletIndex >= pallet.size())
			palletIndex = 0;
	}

	private void add() {
		Drawable d = pallet.get(palletIndex).copy(cursorPos);
		game.getSceneHandler().getCurrentScene().add(d);
		selected = d;
	}

	private void select() {
		selected = game.getSceneHandler().getCurrentScene()
				.getByLoc(cursorPos.x, cursorPos.y);
		if (selected == null)
			return;
		selected.setEditing(true);
		game.getSceneHandler().getCurrentScene().wakeAll();
	}

	private void deselect() {
		selected.setEditing(false);
		selected = null;
	}

	private void removeSelected() {
		selected.setRemove();
		deselect();
		game.getSceneHandler().getCurrentScene().wakeAll();
	}

	private void updateCursorPos() {
		cursorPos = game.getCameraManager().screenToGlobalPos(Gdx.input.getX(),
				Gdx.graphics.getHeight() - Gdx.input.getY());
	}
	
	// hud
	private void updateHUD() {
		HUD h = game.getHUDManager().getHUD();
		
		if (pallet.size() != 0) {
			h.pallet = pallet.get(palletIndex).assetName;
		}
		
		if (selected != null) {
			h.selected = selected.assetName;
		}
	}
}
