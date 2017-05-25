package opengl.framework.opengl;

import java.io.IOException;

import opengl.framework.opengl.model.GameState;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Timer;



public class GameWindow {
	
	private static GameWindow instance = new GameWindow();
	
	public static GameWindow getInstance() {
		return instance;
	}
	
	private int displayWidth = 800;
	private int displayHeight = 600;
	private final int displayFrequency = 60;
	
	public void create(String title, int width, int height) throws LWJGLException {
		this.displayWidth = width;
		this.displayHeight = height;
		
		DisplayMode chosenMode = null;

		DisplayMode[] modes = Display.getAvailableDisplayModes();

		for (int i = 0; i < modes.length; i++) {
			if(modes[i].getWidth() == displayWidth
			&& modes[i].getHeight() == displayHeight
			&& modes[i].getBitsPerPixel() == 32
			&& modes[i].getFrequency() == displayFrequency) {
				chosenMode = modes[i];
				break;
			}
		}
		Display.setDisplayMode(chosenMode);
		Display.setTitle(title);
		Display.create();
		Mouse.create();
		Keyboard.create();
	}
	
	public void destroy() {
		Display.destroy();
	}
	
	private GameState gameState = null;
	
	public void setGameState(GameState gameState) {
		if(this.gameState != null) {
			this.gameState.leave();
		}
		this.gameState = gameState;
		if(this.gameState != null) {
			this.gameState.enter();
		}
	}

	public void loadCursor(String string, int i, int j) throws IOException, LWJGLException {
		TextureLoader textureLoader = new TextureLoader();
		Cursor cursor = textureLoader.getCursor(string, i, j);
		Mouse.setNativeCursor(cursor);
	}

	public void startRendering() {
		boolean gameRunning = true;

		while (gameRunning) {
			Timer.tick();

			// render using OpenGL
			if(gameState != null) {
				gameState.update();
				gameState.render();
			}

			// now tell the screen to update
			Display.update();
			Display.sync(displayFrequency);

			// finally check if the user has requested that the display be shutdown
			if (Display.isCloseRequested() || gameState.isCloseRequested()) {
				gameRunning = false;
			}
		}
	}

	public void setFullSreen(boolean fullscreen) throws LWJGLException {
		Display.setFullscreen(fullscreen);
	}
}
