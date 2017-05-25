package opengl.framework.test.selection;

import java.util.Map;

import opengl.framework.opengl.ViewPort;
import opengl.framework.opengl.model.Entity;
import opengl.framework.opengl.model.GameState;
import opengl.framework.opengl.model.Pickable;
import opengl.framework.opengl.model.Scene;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector3f;



public class InGameState extends GameState {

	private ViewPort mainViewPort;
	private ViewPort secondaryViewPort;
	
	private final Scene mainScene = new TestScene();
	
	Point selection;
	
	@Override
	public void enter() {
		DisplayMode displayMode = Display.getDisplayMode();
		int width = displayMode.getWidth();
		int height = displayMode.getHeight();
		
		int secondarySize = (int) Math.round(height / 3.0);
		
		mainViewPort = new ViewPort(0, 0, width, height);
		mainViewPort.setScene(mainScene);
		secondaryViewPort = new ViewPort(width - secondarySize, height - secondarySize, secondarySize, secondarySize);
		secondaryViewPort.setScene(mainScene);
		mainScene.enter();
	}

	@Override
	public void update() {
		Mouse.poll();
		boolean isLeftDown = Mouse.isButtonDown(0);
		int x = Mouse.getX();
		int y = Mouse.getY();
		if(isLeftDown) {
			if(selection == null) {
				selection = new Point(x, y);
				System.out.println(x + " " + y);
			}
			for(Pickable pickable : mainScene.getPickables()) {
				pickable.downLight();
			}
			Map<Pickable, Integer> entities = mainViewPort.handlePicking(selection.getX(), selection.getY(), x, y);
			
			Pickable closestPickable = null;
			int depth = 0;
			for(Pickable pickable : entities.keySet()) {
				pickable.highLight();
				if(depth == 0 || depth > entities.get(pickable)) {
					closestPickable = pickable;
					depth = entities.get(pickable);
				}
			}
			if(closestPickable != null) {
				secondaryViewPort.setCameraPosition(((Entity)closestPickable).getPosition());
			}
		} else {
			selection = null;
			mainViewPort.clearPicking();
			int wheel = Mouse.getDWheel();
			if(wheel != 0) {
				Vector3f recul = mainViewPort.getCameraReculOffset();
				recul.y += wheel / -60;
				if(recul.y < 0) recul.y = 0;
				if(recul.y > 75) recul.y = 75;
			}
			int width = Display.getDisplayMode().getWidth();
			int height = Display.getDisplayMode().getHeight();
			Vector3f position = mainViewPort.getCameraPosition();
			if(x < 5) {
				position.x -= 1;
				if(position.x < -100)
					position.x = -100;
			} else if(x > width - 5) {
				position.x += 1;
				if(position.x > 100)
					position.x = 100;
			}
			if(y < 5) {
				position.z += 1;
				if(position.z < -100)
					position.z = -100;
			} else if(y > height - 5) {
				position.z -= 1;
				if(position.z > 100)
					position.z = 100;
			}
		}
		mainScene.update();
	}

	@Override
	public void _render() {
		mainViewPort.render();
		secondaryViewPort.render();
	}

	@Override
	public void leave() {
		mainScene.leave();
	}

	@Override
	public boolean isCloseRequested() {
		return false;
	}
}
