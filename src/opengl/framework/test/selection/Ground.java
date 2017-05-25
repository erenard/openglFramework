package opengl.framework.test.selection;

import opengl.framework.opengl.model.Entity;

import org.lwjgl.opengl.GL11;


public class Ground extends Entity {

	public Ground() {
		super();
		this.position.y = -1;
	}
	
	@Override
	protected void _render() {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f( 0.0f, 0.5f, 0.0f);
		GL11.glVertex3f(-100, 0, -100);
		GL11.glVertex3f( 100, 0, -100);
		GL11.glVertex3f( 100, 0,  100);
		GL11.glVertex3f(-100, 0,  100);
		GL11.glEnd();
	}

}
