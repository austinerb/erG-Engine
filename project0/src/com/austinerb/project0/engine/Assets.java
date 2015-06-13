package com.austinerb.project0.engine;

import java.util.ArrayList;

import com.austinerb.project0.entities.Model;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	
	private ArrayList<Asset> assets = new ArrayList<Asset>(); 
	
	/////// ANIMATIONS
	private final String animations = "assets/animations/";
	public static final String GUY_ANIMATION = "guy";	
	
	/////// TEXTURES
	private final String textures = "assets/textures/";
	public static final String TEXTURES_PLATFORM = "platform";
	// hud
	private final String textures_hud = textures + "hud/";
	public static final String TEXTURES_HUD_BG = "hud_bg";
	public static final String TEXTURES_HUD_HEALTH = "hud_health";
	public static final String TEXTURES_HUD_STAMINA = "hud_stamina";
	
	/////// MODELS 
	private final String models = "assets/models/";
	// world
	private final String models_world = models + "world/";
	public static final String MODELS_WORLD_LADDER = "ladder";
	public static final String MODELS_WORLD_LAND_1 = "land_1";
	public static final String MODELS_WORLD_LAND_2 = "land_2";
	public static final String MODELS_WORLD_LAND_3 = "land_3";
	public static final String MODELS_WORLD_LAND_4 = "land_4";
	public static final String MODELS_WORLD_LAND_5 = "land_5";
	public static final String MODELS_WORLD_LAND_6 = "land_6";
	
	/////// NON ITEMS (objects without textures)
	public static final String NI_SPAWNER = "spawner";
	public static final String NI_LIGHTBODY = "light";
	public static final String NI_NODE = "node";
	
	public Assets() {
		/////// ANIMATIONS
		assets.add(new Asset(GUY_ANIMATION, animations, Animation.class));
		
		/////// TEXTURES
		assets.add(new Asset(TEXTURES_PLATFORM, textures, Texture.class));
		// hud
		assets.add(new Asset(TEXTURES_HUD_BG, textures_hud, Texture.class));
		assets.add(new Asset(TEXTURES_HUD_HEALTH, textures_hud, Texture.class));
		assets.add(new Asset(TEXTURES_HUD_STAMINA, textures_hud, Texture.class));
		
		/////// MODELS
		// worlds
		assets.add(new Asset(MODELS_WORLD_LADDER, models_world, Model.class));
		assets.add(new Asset(MODELS_WORLD_LAND_1, models_world, Model.class));
		assets.add(new Asset(MODELS_WORLD_LAND_2, models_world, Model.class));
		assets.add(new Asset(MODELS_WORLD_LAND_3, models_world, Model.class));
		assets.add(new Asset(MODELS_WORLD_LAND_4, models_world, Model.class));
		assets.add(new Asset(MODELS_WORLD_LAND_5, models_world, Model.class));
		assets.add(new Asset(MODELS_WORLD_LAND_6, models_world, Model.class));
	}
	
	public Asset getAsset(String assetName) {
		int i = assets.indexOf(new Asset(assetName));
		if (i==-1) return null;
		return assets.get(i);
	}
	
	public ArrayList<Asset> getAllAssets() {
		return assets;
	}
	
	public Asset get(int index) {
		return assets.get(index);
	}
	
	public boolean assetIsClass(String assetName, Class<?> classType) {
		if (classType == getAsset(assetName).getClassType()) return true; // for when classType == null
		if (getAsset(assetName).getClassType() == null) return false;
		return getAsset(assetName).getClassType().equals(classType);
	}
}
