package opengl.framework.test.draw2d;

import java.io.IOException;

import opengl.framework.opengl.loader.Loader;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


public class Draw2DTest {
	private void createDisplay(int targetWidth, int targetHeight) throws LWJGLException {

		DisplayMode chosenMode = null;

		DisplayMode[] modes = Display.getAvailableDisplayModes();

		for (int i = 0; i < modes.length; i++) {
			if ((modes[i].getWidth() == targetWidth) && (modes[i].getHeight() == targetHeight)) {
				chosenMode = modes[i];
				break;
			}
		}
		Display.setDisplayMode(chosenMode);
		Display.setTitle(this.getClass().getName());
		Display.create();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
//		GL11.glEnable(GL11.GL_CULL_FACE);
		
		//Smooth Shading
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		//Fond Noir
		GL11.glClearColor(0f, 0f, 0f, 0f);
		
		//Profondeur
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		//Qualité du calcul de perspective
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

		//GL Scene
		GL11.glViewport(0, 0, targetWidth, targetHeight);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float) targetWidth) / ((float) targetHeight), 0.1f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();		
	}

	private void mainLoop() throws Exception {
		boolean gameRunning = true;
		while (gameRunning) {
			// render using OpenGL
			drawGLScene();

			// now tell the screen to update
			Display.update();
			Display.sync(30);

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
				GL11.glTranslated(i - (nbWidth / 2.0), j - (nbHeight / 2.0),-8.0);
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

	public Draw2DTest(int w, int h) {
		try {
			createDisplay(w, h);
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
		new Draw2DTest(800, 600);
	}
}
