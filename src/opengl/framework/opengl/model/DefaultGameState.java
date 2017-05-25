package opengl.framework.opengl.model;

import opengl.framework.opengl.ViewPort;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class DefaultGameState extends GameState {

	private ViewPort mainViewPort;
	private final Scene defaultScene;
	
	public DefaultGameState(Scene defaultScene) {
		this.defaultScene = defaultScene;
	}
	
	@Override
	public void enter() {
		DisplayMode displayMode = Display.getDisplayMode();
		mainViewPort = new ViewPort(0, 0, displayMode.getWidth(), displayMode.getHeight());
		mainViewPort.setScene(defaultScene);
		defaultScene.enter();
	}

	@Override
	public void leave() {
		defaultScene.leave();
	}

	@Override
	public void _render() {
		mainViewPort.render();
	}

	@Override
	public void update() {
		defaultScene.update();
	}

	@Override
	public boolean isCloseRequested() {
		return false;
	}
}
