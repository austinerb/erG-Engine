package com.austinerb.project0.engine;

public class GameUtil {
	public static final float PIXEL_METER = 128f; // pixels in a meter
	public static final float METER_PIXEL = 1.0f / PIXEL_METER; // meters in a
																// pixel
	
	// pixels to meters
	public static float ptm(float f) {
		return f * METER_PIXEL;
	}

	// meters to pixels
	public static float mtp(float f) {
		return f * PIXEL_METER;
	}

	// rad to degrees
	public static float rtd(float f) {
		return (float) (f * 180 / Math.PI);
	}

	// degrees to rad
	public static float dtr(float f) {
		return (float) (f * Math.PI / 180);
	}
}
