package opengl.framework.opengl.model;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;


public abstract class Scene {

	protected Timer timer = new Timer();
	
	protected Collection<Entity> entities = new ArrayList<Entity>();
	
	public abstract void enter();
	
	public abstract void leave();
	
	public void render() {
		// Clear Depth Buffer
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		// render using OpenGL
		for(Entity entity : entities) {
			entity.render();
		}
	}
	
	public void renderPickable() {
		// render using OpenGL
		for(Entity entity : entities) {
			if(entity instanceof Pickable)
				entity.render();
		}
	}
	
	public void update() {
		float delta = timer.getTime();
		for(Entity entity : entities) {
			if (entity instanceof Animable) {
				Animable animable = (Animable) entity;
				animable.update(delta);
			}
		}
	}
	
	public IntBuffer glSelectMode() {
	    //Calculate select buffer capacity and allocate data if necessary
		//Each object take in maximium : 4 * name stack depth
		IntBuffer param = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_MAX_NAME_STACK_DEPTH, param);
	    int capacity = entities.size() * 4 * param.get(0);
	    IntBuffer selectBuffer = BufferUtils.createIntBuffer(capacity);

	    //Send select buffer to OpenGl and use select mode to track object hits
	    GL11.glSelectBuffer(selectBuffer);
	    GL11.glRenderMode(GL11.GL_SELECT);
	    
	    return selectBuffer;
	}

	public Pickable pick(int entityId) {
		for(Entity entity : entities) {
			if(entity instanceof Pickable && entity.getId() == entityId)
				return (Pickable) entity;
		}
		return null;
	}

	public Collection<Pickable> getPickables() {
		Collection<Pickable> pickables = new ArrayList<Pickable>();
		for(Entity entity : entities) {
			if(entity instanceof Pickable)
				pickables.add((Pickable) entity);
		}
		return pickables;
	}
}

