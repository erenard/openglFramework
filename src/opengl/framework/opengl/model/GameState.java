package opengl.framework.opengl.model;

import org.lwjgl.opengl.GL11;


public abstract class GameState {
	public abstract void enter();
	public abstract void update();

	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		_render();
		GL11.glFlush();
	}
	
	public abstract void _render();
	public abstract void leave();
	public abstract boolean isCloseRequested();
}
