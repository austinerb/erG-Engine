package com.austinerb.project0.engine;

import java.util.ArrayList;

public class Trash {
	private ArrayList<Drawable> remove; // removes during this update
	private ArrayList<Drawable> toRemove; // removes during next update

	public Trash() {
		remove = new ArrayList<Drawable>();
		toRemove = new ArrayList<Drawable>();
	}

	public void add(Drawable d) {
		toRemove.add(d);
	}

	public void update() {
		// removes objects
		for (int i = 0; i < remove.size(); i++) {
			remove.get(i).remove(1);
			remove.remove(i);
			i--;
		}

		for (int i = 0; i < toRemove.size(); i++) {
			remove.add(toRemove.get(i));
			toRemove.remove(i);
			i--;
		}
	}
}
