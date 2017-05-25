package opengl.framework.test.selection;

import java.io.IOException;

import opengl.framework.opengl.GameWindow;
import opengl.framework.opengl.loader.Loader;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;


public class Selection3DTest {

	public Selection3DTest(int width, int height, boolean fullscreen) {
		GameWindow gameWindow = GameWindow.getInstance();
		try {
			gameWindow.create(this.getClass().getName(), width, height);
			gameWindow.setFullSreen(fullscreen);
			Mouse.setCursorPosition(width / 2, height / 2);
			gameWindow.loadCursor("\\cursor.png", 0, 0);
			gameWindow.setGameState(new InGameState());
			gameWindow.startRendering();
		} catch (Exception e) {
			e.printStackTrace();
			Sys.alert("Error", e.getMessage());
		} finally {
			GameWindow.getInstance().destroy();
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Loader.appendNativePathToUserPath("native");
		new Selection3DTest(800, 600, false);
	}
}
