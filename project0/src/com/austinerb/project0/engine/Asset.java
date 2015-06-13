package com.austinerb.project0.engine;

// stores location and name and type of file

public class Asset {

	private String assetName;	// name of the asset including the extension
	private String file;		// location of the asset including the file name and extension
	private Class<?> classType;	// class type. ex: Texture
	
	public Asset(String assetName) {
		this.assetName = assetName;
	}
	
	public Asset(String assetName, String path, Class<?> classType) {
		this.assetName = assetName;
		this.file = path + assetName;
		this.classType = classType;
	}
	
	public String getAssetName() {
		return assetName;
	}
	
	public String getFile() {
		return file;
	}

	public Class<?> getClassType() {
		return classType;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Asset)) return false;
		
		Asset compare = ((Asset)other);
		
		return compare.getAssetName().equals(this.getAssetName());
	}
}
