package opengl.framework.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import opengl.framework.opengl.model.Pickable;
import opengl.framework.opengl.model.Scene;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


public class ViewPort {
	
	private final Rectangle viewport = new Rectangle();
	
	private final Rectangle ortho = new Rectangle();
	
	private Rectangle picking;
	
	private final Vector3f cameraReculOffset = new Vector3f(0, 1, 8);
	
	private final Vector3f cameraPosition = new Vector3f(0, 0, 0);

	public ViewPort(int x, int y, int w, int h) {
		//Texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		//Color material
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		//Smooth Shading
		GL11.glShadeModel(GL11.GL_SMOOTH);
		//Fond Noir
		GL11.glClearColor(0f, 0f, 0f, 0f);
		//Profondeur
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		//Qualité du calcul de perspective
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

		viewport.setBounds(x, y, w, h);
		
	}
	
	private static boolean orthoModeState = false;
	
	public void setOrthoOn() {
		setOrthoOn(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
	}
	
	public void setOrthoOn(int viewportX, int viewportY, int viewportW, int viewportH) {
		if(orthoModeState)
			return;
		
		ortho.setBounds(viewportX, viewportY, viewportW, viewportH);
		
		// prepare to render in 2D
		GL11.glDisable(GL11.GL_DEPTH_TEST); // so text stays on top of scene
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix(); // Preserve the Modelview Matrix
		GL11.glLoadIdentity(); // clear the Modelview Matrix

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix(); // preserve perspective view
		GL11.glLoadIdentity(); // clear the perspective matrix

		GL11.glOrtho(viewportX, viewportX + viewportW, viewportY, viewportY + viewportH, -1, 1);
		orthoModeState = true;
	}

	public void setOrthoOff() {
		if(!orthoModeState)
			return;
		
		ortho.setBounds(viewport);

		// restore the original positions and views
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST); // turn Depth Testing back on
		orthoModeState = false;
	}

	public double getPixelWidth() {
		return (double) ortho.getWidth() / (double) viewport.getWidth();
	}

	public double getPixelHeight() {
		return (double) ortho.getHeight() / (double) viewport.getHeight();
	}

	public double getPointWidth() {
		return (double) ortho.getWidth() / 800;
	}

	public double getPointHeight() {
		return (double) ortho.getHeight() / 600;
	}
	
	private Scene scene = null;
	
	public void setScene(Scene scene) {
		if(this.scene != null) {
			this.scene.leave();
			this.scene = null;
		}
		this.scene = scene;
	}

	public void glString(BitmappedFont font, String str) {
		if(str != null) {
	        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(16);
	        GL11.glGetBoolean(GL11.GL_BLEND, byteBuffer);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        double pointWidth = getPointWidth();
	        double pointHeight = getPointHeight();
	        font.glString(str, pointWidth, pointHeight);
	        if(byteBuffer.get(0) == 0)
	        	GL11.glDisable(GL11.GL_BLEND);
		}
	}

	public FloatBuffer getOGLPos(int x, int y) {
		FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projMatrix);
		
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

		int winX = x;
		int winY = y;

		FloatBuffer readBuffer = BufferUtils.createFloatBuffer(1);
		GL11.glReadPixels(winX, winY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, readBuffer);
		float winZ = readBuffer.get(0);
		
		if(winZ != 0)
			System.out.println("winZ=" + winZ);
		
		FloatBuffer coords = BufferUtils.createFloatBuffer(4);
		
		GLU.gluUnProject(winX, winY, winZ, modelMatrix, projMatrix, viewport, coords);

		return coords;
	}

	private void preRender() {
		GL11.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float) viewport.getWidth()) / ((float) viewport.getHeight()), 0.1f, 100.0f);
		GLU.gluLookAt(cameraPosition.x + cameraReculOffset.x, cameraPosition.y + cameraReculOffset.y, cameraPosition.z + cameraReculOffset.z, cameraPosition.x, cameraPosition.y, cameraPosition.z, 0, 1, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	public void render() {
		if(scene != null) {
			preRender();
			//GL Scene
			scene.render();
			postRender();
		}
	}

	private void postRender() {
		if(picking != null) {
			setOrthoOn();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glColor4f(1, 0, 0, 0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(picking.getX(), picking.getY());
			GL11.glVertex2i(picking.getX() + picking.getWidth(), picking.getY());
			GL11.glVertex2i(picking.getX() + picking.getWidth(), picking.getY() + picking.getHeight());
			GL11.glVertex2i(picking.getX(), picking.getY() + picking.getHeight());
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			setOrthoOff();
		}
	}
	
	public Map<Pickable, Integer> handlePicking(int x1, int y1, int x2, int y2) {
		picking = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));

		int w = Math.abs(x1 - x2);
		int h = Math.abs(y1 - y2);
		
		int x = Math.min(x1, x2) + w / 2;
		int y = Math.min(y1, y2) + h / 2;
		
		w++;
		h++;
		
		Map<Pickable, Integer> result = new HashMap<Pickable, Integer>();
		if(scene != null) {
			/* Mode selection */
			IntBuffer selectBuffer = scene.glSelectMode();
			/* Rendu de selection */
			preRender();
			//Get viewport & projectionmatrix
			IntBuffer viewport = BufferUtils.createIntBuffer(16);
		    GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		    FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		    GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);

		    //Switch to projection transformation
		    GL11.glMatrixMode(GL11.GL_PROJECTION);
		    GL11.glPushMatrix();
		    GL11.glLoadIdentity();
		    
		    //Restrict region to pick object only in this region
		    GLU.gluPickMatrix(x, y, w, h, viewport);    //x, y, width, height is the picking area

		    //Load the projection matrix
		    GL11.glMultMatrix(projection);
		    
		    //Go back to modelview for rendering
		    GL11.glMatrixMode(GL11.GL_MODELVIEW);

		    GL11.glInitNames();
		    //Draw the objects to pick
			scene.renderPickable();
			
			//Switch back to render mode & get the number of hits/records in the select buffer
		    int nbRecords = GL11.glRenderMode(GL11.GL_RENDER);
		    if(nbRecords > 0) {

			    /*
			     * Select buffer
			     * -------------
			     * The select buffer is a list of nbRecords records.
			     * Each records is composed of :
			     * 1st int : depth of the name stack
			     * 2nd int : minimum depth value
			     * 3rd int : maximum depth value
			     * x int(s) : list of name (number is defined in the 1st integer))
			     */
			    int index = 0;
			    for(int i = 0; i < nbRecords; i++) {
		            //Current record is closer
		            int stackLength = selectBuffer.get(index++);
		            int minDepth = selectBuffer.get(index++);
		            /*int maxDepth = */selectBuffer.get(index++);

		            for(int j = 0; j < stackLength; j++) {
		            	result.put(scene.pick(selectBuffer.get(index++)), minDepth);
		            }
			    }
		    }

		    GL11.glMatrixMode(GL11.GL_PROJECTION);
		    GL11.glPopMatrix();

		}
		return result;
	}

	public void setCameraPosition(Vector3f position) {
		cameraPosition.set(position);
	}

	public Vector3f getCameraPosition() {
		return cameraPosition;
	}

	public void setCameraReculOffset(Vector3f recul) {
		cameraReculOffset.set(recul);
	}

	public Vector3f getCameraReculOffset() {
		return cameraReculOffset;
	}

	public void clearPicking() {
		picking = null;
	}
}
