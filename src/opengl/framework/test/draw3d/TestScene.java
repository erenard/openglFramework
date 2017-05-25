package opengl.framework.test.draw3d;

import java.nio.FloatBuffer;

import opengl.framework.opengl.model.Scene;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class TestScene extends Scene {

	@Override
	public void enter() {
		Ground ground = new Ground();
		entities.add(ground);
		Cube cube = new Cube();
		entities.add(cube);
		{
			FloatBuffer lightAmbient = BufferUtils.createFloatBuffer(4);
			lightAmbient.put(0.5f);
			lightAmbient.put(0.5f);
			lightAmbient.put(0.5f);
			lightAmbient.put(1.0f);
			lightAmbient.rewind();
			GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, lightAmbient);				// Setup The Ambient Light
		}
		{
			FloatBuffer lightDiffuse = BufferUtils.createFloatBuffer(4);
			lightDiffuse.put(1.0f);
			lightDiffuse.put(1.0f);
			lightDiffuse.put(1.0f);
			lightDiffuse.put(1.0f);
			lightDiffuse.rewind();
			GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, lightDiffuse);				// Setup The Diffuse Light
		}
		{
			FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
			lightPosition.put(2.0f);
			lightPosition.put(10.0f);
			lightPosition.put(2.0f);
			lightPosition.put(1.0f);
			lightPosition.rewind();
			GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition);				// Setup The Diffuse Position
		}
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub

	}

}
