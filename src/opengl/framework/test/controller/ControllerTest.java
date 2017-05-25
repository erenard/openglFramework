package opengl.framework.test.controller;

import java.io.IOException;

import opengl.framework.opengl.loader.Loader;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;


public class ControllerTest {
	public static void main(String[] args) throws IOException, LWJGLException {
		Loader.appendNativePathToUserPath("native");
		Controllers.create();
		int controllerCount = Controllers.getControllerCount();
		System.out.println(controllerCount + " controllers.");
		for(int i = 0; i < controllerCount; i++) {
			Controller controller = Controllers.getController(i);
			System.out.println(i + ":" + controller.getName() + ", " + controller.getAxisCount() + " Axis, " + controller.getButtonCount() + " Buttons.");
		}
		Controller controller = Controllers.getController(0);
		while(true) {
			controller.poll();
			System.out.println(controller.getXAxisValue());
		}
	}
}
