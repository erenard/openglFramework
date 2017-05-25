package opengl.framework.test.draw3d;

import opengl.framework.opengl.model.Animable;
import opengl.framework.opengl.model.Entity;
import opengl.framework.opengl.model.Pickable;

import org.lwjgl.opengl.GL11;


public class Cube extends Entity implements Animable, Pickable {

	private boolean highlighted;
	private final float speed;
	
	public Cube() {
		super();
		this.speed = 1.0f + (float) Math.random();
	}
	
	public Cube(float speed) {
		super();
		this.speed = speed;
	}
	
	@Override
	protected void _render() {
		if(!highlighted) {
			GL11.glColor4f(1, 0, 0, 1);
		} else {
			GL11.glColor4f(0, 0, 1, 1);
		}
		
		GL11.glBegin(GL11.GL_QUADS);
		
		// Front Face
		GL11.glNormal3f( 0.0f, 0.0f, 1.0f);		// Normal Pointing Towards Viewer
		GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Point 1 (Front)
		GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Point 2 (Front)
		GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Point 3 (Front)
		GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Point 4 (Front)
		// Back Face
		GL11.glNormal3f( 0.0f, 0.0f,-1.0f);		// Normal Pointing Away From Viewer
		GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Point 1 (Back)
		GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Point 2 (Back)
		GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Point 3 (Back)
		GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Point 4 (Back)
		// Top Face
		GL11.glNormal3f( 0.0f, 1.0f, 0.0f);		// Normal Pointing Up
		GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Point 1 (Top)
		GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Point 2 (Top)
		GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Point 3 (Top)
		GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Point 4 (Top)
		// Bottom Face
		GL11.glNormal3f( 0.0f,-1.0f, 0.0f);		// Normal Pointing Down
		GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Point 1 (Bottom)
		GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Point 2 (Bottom)
		GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Point 3 (Bottom)
		GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Point 4 (Bottom)
		// Right face
		GL11.glNormal3f( 1.0f, 0.0f, 0.0f);		// Normal Pointing Right
		GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Point 1 (Right)
		GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Point 2 (Right)
		GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Point 3 (Right)
		GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Point 4 (Right)
		// Left Face
		GL11.glNormal3f(-1.0f, 0.0f, 0.0f);		// Normal Pointing Left
		GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Point 1 (Left)
		GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Point 2 (Left)
		GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Point 3 (Left)
		GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Point 4 (Left)
		
		GL11.glEnd();
	}

	@Override
	public void update(float delta) {
		orientation.x = 35.0f * delta * speed;
		orientation.y = 20.0f * delta * speed;
	}

	@Override
	public void highLight() {
		highlighted = true;
	}

	@Override
	public void downLight() {
		highlighted = false;
	}

}
