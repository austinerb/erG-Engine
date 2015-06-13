package com.austinerb.project0.engine;

import com.austinerb.project0.entities.Model;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

// loads and stores all assets into usable resources

public class ResourceManager {
	
	public static final String TEXTURE = ".png";
	public static final String MODEL = ".json";
	public static final String ANIMATION = ".scml";
	
	private Assets assets; // a reference to all assets to be loaded
	private AssetManager assetManager; // loads all resources other than animations, ...
	private Resources resources; // stores a reference to all loaded resources
	
	private Array<AssetTemp> assetNames; // names of assets loaded
	
	public ResourceManager() {
		assets = new Assets();
		assetManager = new AssetManager();
		resources = new Resources();
		
		assetNames = new Array<AssetTemp>();
		
		loadResources();
	}
	
	private void loadResources() {
		
		for (int i = 0; i <assets.getAllAssets().size(); i++) {
			
			// MODELS
			
			if (assets.assetIsClass(assets.get(i).getAssetName(), Model.class)) {
				try {
					String texture = assets.get(i).getFile() + TEXTURE;
					String model = assets.get(i).getFile() + MODEL;
					String modelName = assets.get(i).getAssetName() + MODEL;
					
					if (Gdx.files.internal(texture).exists() && Gdx.files.internal(model).exists()) {
						assetManager.load(texture, Texture.class);
						assetNames.add(new AssetTemp(texture, Texture.class));
						addResource(modelName, Gdx.files.internal(model));
					}
					
				} catch (Exception e) { e.printStackTrace(); }
			} 
			
			
			// ANIMATIONS
			
			else if (assets.assetIsClass(assets.get(i).getAssetName(), Animation.class)) {
				try {
					String animation = assets.get(i).getFile() + ANIMATION;
					String animationName = assets.get(i).getAssetName() + ANIMATION;
					
					if (Gdx.files.internal(animation).exists()) {
						addResource(animationName, animation);
					}
					
				} catch (Exception e) { e.printStackTrace(); }
			} 
			
			
			// TEXTURES
			
			else if (assets.assetIsClass(assets.get(i).getAssetName(), Texture.class)) {
				try {
					String texture = assets.get(i).getFile() + TEXTURE;
					assetManager.load(texture, Texture.class);
					assetNames.add(new AssetTemp(texture, Texture.class));
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
		
		assetManager.finishLoading();	
		
		poolAssets();
	}
	
	// pool all the assets into one array
	private void poolAssets() {
		for (int i = 0; i < assetManager.getLoadedAssets(); i++) {
			addResource(assetNames.get(i).getName(), assetManager.get(assetNames.get(i).getName(), assetNames.get(i).getType())); //TODO: fix hard coded Texture.class
		}
	}
	
	// returns an object such, as a texture, using the name of the file
	public Object getResource(String assetName) {
		return resources.get(assetName);
	}
	
	private void addResource(String resourceName, Object resource) {
		resources.add(resourceName, resource);
	}
}
