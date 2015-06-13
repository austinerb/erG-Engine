package com.austinerb.project0.engine;

import java.util.ArrayList;

public class Resources {

	private ArrayList<String> resourceNames;
	private ArrayList<Object> resources;
	
	public Resources() {
		resourceNames = new ArrayList<String>();
		resources = new ArrayList<Object>();
	}
	
	public void add(String resourceName, Object resource) {
		int i = resourceName.lastIndexOf("/");
		
		resourceNames.add(resourceName.substring(i+1, resourceName.length()));
		resources.add(resource);
	}
	
	public Object get(String resourceName) {
		int index = resourceNames.indexOf(resourceName);
		if (index == -1) return null;
		return resources.get(index);
	}
}
