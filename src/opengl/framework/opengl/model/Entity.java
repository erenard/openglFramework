package opengl.framework.opengl.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public abstract class Entity {

	protected Vector3f position = new Vector3f(0, 0, 0);
	protected Vector3f orientation = new Vector3f(0, 0, 0);
	
	private int ID;
	private static int nextID = 0;
	
	protected Entity() {
		if(this instanceof Pickable)
			ID = nextID++;
	}
	
	public int getId() {
		return ID;
	}
	
	public final void render() {
		if(this instanceof Pickable)
			GL11.glPushName(ID);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glRotatef(orientation.x, 1, 0, 0);
		GL11.glRotatef(orientation.y, 0, 1, 0);
		GL11.glRotatef(orientation.z, 0, 0, 1);
		_render();
		GL11.glPopMatrix();
		if(this instanceof Pickable)
			GL11.glPopName();
	}
	
	protected abstract void _render();

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getOrientation() {
		return orientation;
	}

	public void setOrientation(Vector3f orientation) {
		this.orientation = orientation;
	}
	
	public void translate(Vector3f position) {
		this.position.translate(position.x, position.y, position.z);
	}

	public void rotate(Vector3f orientation) {
		this.orientation.translate(orientation.x, orientation.y, orientation.z);
	}
}
