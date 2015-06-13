package com.austinerb.project0.engine;

import java.io.InputStream;

import com.badlogic.gdx.files.FileHandle;

public interface BaseJsonReader {
	JsonValue parse (InputStream input);

	JsonValue parse (FileHandle file);
}