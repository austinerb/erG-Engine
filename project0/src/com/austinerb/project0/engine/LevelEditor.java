package com.austinerb.project0.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;

import com.badlogic.gdx.math.Vector2;

// allows to save a scene object to a file
// and load a scene from a file

public class LevelEditor {

	public static Scene load(Game game, String path) {
		Scene scene = new Scene(game);

		BufferedReader br = null;

		try {
			String line;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {

				String assetName = "";
				float x = 0;
				float y = 0;
				float angle = 0;
				Class<?> c = null;
				Constructor<?> con = null;

				if (line.contains("!OBJECT")) {
					line = br.readLine();
					if (line != null && line.contains("!CLASS:")) {
						int i1 = line.indexOf(":");
						int i2 = line.lastIndexOf(":");
						String sub = line.substring(i1 + 1, i2);
						try {
							c = Class.forName(sub);
							con = c.getConstructor(Game.class, Vector2.class, String.class);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					line = br.readLine();
					if (line != null && line.contains("!ASSETNAME:")) {
						int i1 = line.indexOf(":");
						int i2 = line.lastIndexOf(":");
						assetName = line.substring(i1 + 1, i2);
					}
					line = br.readLine();
					if (line != null && line.contains("!X:")) {
						int i1 = line.indexOf(":");
						int i2 = line.lastIndexOf(":");
						String sub = line.substring(i1 + 1, i2);
						x = Float.valueOf(sub);
					}
					line = br.readLine();
					if (line != null && line.contains("!Y:")) {
						int i1 = line.indexOf(":");
						int i2 = line.lastIndexOf(":");
						String sub = line.substring(i1 + 1, i2);
						y = Float.valueOf(sub);
					}
					line = br.readLine();
					if (line != null && line.contains("!ANGLE:")) {
						int i1 = line.indexOf(":");
						int i2 = line.lastIndexOf(":");
						String sub = line.substring(i1 + 1, i2);
						angle = Float.valueOf(sub);
					}
					if (con != null) {
						try {
							Drawable d = (Drawable) con.newInstance(game,
									new Vector2(x, y), assetName);
							scene.add(d);
							scene.get(scene.allDrawables.size()-1).angle = angle;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					line = br.readLine();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return scene;
	}

	// //////////////

	public static void save(Scene scene, String path) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path), "utf-8"));
			writeData(scene, writer);
		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
				System.out.println("SAVED!");
			} catch (Exception ex) {
			}
		}
	}

	private static void writeData(Scene scene, BufferedWriter writer)
			throws IOException {
		for (int i = 0; i < scene.allDrawables.size(); i++) {
			Drawable d = scene.allDrawables.get(i);

			if (d.getSaveClass() != null) {
				String className = d.getClass().toString();
				className = d.getSaveClass().getName();
				String assetName = d.assetName;
				String x = Float.toString(d.getPosition().x);
				String y = Float.toString(d.getPosition().y);
				String angle = Float.toString(d.angle);

				writer.write("!OBJECT");
				writer.newLine();
				writer.write("    !CLASS:" + className + ":");
				writer.newLine();
				writer.write("    !ASSETNAME:" + assetName + ":");
				writer.newLine();
				writer.write("    !X:" + x + ":");
				writer.newLine();
				writer.write("    !Y:" + y + ":");
				writer.newLine();
				writer.write("    !ANGLE:" + angle + ":");
				writer.newLine();
				writer.newLine();
			}
		}
	}
}
