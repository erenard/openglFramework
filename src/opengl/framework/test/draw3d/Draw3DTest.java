package opengl.framework.test.draw3d;

import java.io.IOException;

import opengl.framework.opengl.GameWindow;
import opengl.framework.opengl.TextureLoader;
import opengl.framework.opengl.loader.Loader;
import opengl.framework.opengl.model.DefaultGameState;

import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


public class Draw3DTest {

	private final GameWindow gameWindow = GameWindow.getInstance();
	
	private void mainLoop() throws Exception {
		TextureLoader textureLoader = new TextureLoader();
		Cursor cursor = textureLoader.getCursor("cursor.png", 0, 0);
		Mouse.setNativeCursor(cursor);
		boolean gameRunning = true;
		while (gameRunning) {
			// render using OpenGL
			drawGLScene();

			// now tell the screen to update
			Display.update();
			Display.sync(60);

			// finally check if the user has requested that the display be shutdown
			if (Display.isCloseRequested()) {
				gameRunning = false;
			}
		}
	}

	private void drawGLScene() throws Exception {
		// Clear Screen And Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		int nbWidth = 8;
		int nbHeight = 6;
		
		for(int i = 0; i < nbWidth; i++) {
			for(int j = 0; j < nbHeight; j++) {
				GL11.glLoadIdentity();
				GL11.glColor3d((double)i / (double)nbWidth, (double)j / (double)nbHeight, 1.0);
				GL11.glTranslated(i - (nbWidth / 2.0), j - (nbHeight / 2.0), -8.0);
				drawQuad();
			}
		}
	}
	
	private void drawQuad() throws Exception {
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex3d( 0.0, 0.0, 0.0); //Bas Gauche
		GL11.glVertex3d( 1.0, 0.0, 0.0); //Bas Droite
		GL11.glVertex3d( 1.0, 1.0, 0.0); //Haut Droite
		GL11.glVertex3d( 0.0, 1.0, 0.0); //Haut Gauche

		GL11.glEnd();
	}

	public Draw3DTest() {
		try {
			gameWindow.create(this.getClass().getName(), 800, 600);
			gameWindow.loadCursor("cursor.png", 0, 0);
			gameWindow.setGameState(new DefaultGameState(new TestScene()));
			gameWindow.startRendering();

			mainLoop();
		} catch (Exception e) {
			e.printStackTrace();
			Sys.alert("Error", e.getMessage());
		} finally {
			Display.destroy();
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws IOException {
		Loader.appendNativePathToUserPath("native");
		new Draw3DTest();
	}
}
