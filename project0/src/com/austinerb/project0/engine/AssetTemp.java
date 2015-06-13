package com.austinerb.project0.engine;

public class AssetTemp {

	private String name;
	private Class<?> type;
	
	public AssetTemp(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}
	
}
