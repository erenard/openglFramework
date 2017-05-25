package opengl.framework.test.selection;

import java.nio.FloatBuffer;

import opengl.framework.opengl.model.Scene;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


public class TestScene extends Scene {

	public TestScene() {
		Ground ground = new Ground();
		entities.add(ground);
		Cube cube1 = new Cube();
		entities.add(cube1);
		Cube cube2 = new Cube();
		cube2.translate(new Vector3f(-4, 0,-2));
		entities.add(cube2);
		Cube cube3 = new Cube(0);
		cube3.translate(new Vector3f(4, 0,-4));
		entities.add(cube3);
	}
	
	@Override
	public void enter() {
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
	}

	@Override
	public void leave() {
		GL11.glDisable(GL11.GL_LIGHT1);
	}
}
