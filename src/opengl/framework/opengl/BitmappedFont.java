package opengl.framework.opengl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

public class BitmappedFont {

	private int fontSize = 24;

	private String fontName = "Courier New"; // Name of the font to use

	private int fontTexture;                 // Return Image Address In Memory

	private int base;                        // Storage For 256 Characters
	
	private final int[] charWidth = new int[256];
	
	//Creates the font Courier New to be used as the drawable font
	private void buildFont() {                          // Build Our Bitmap Font
		Font font;                                      // Font object
		BufferedImage fontImage;                        // image for creating the bitmap
		int bitmapSize = 512;                           // set the size for the bitmap texture
		boolean sizeFound = false;
		boolean directionSet = false;
		int fontSize = 24;
		int delta = 0;

		//Finds the size of the widest character (W)
		while(!sizeFound)
		{
			font = new Font(fontName, Font.PLAIN, fontSize);  // Font Name
			// use BufferedImage.TYPE_4BYTE_ABGR to allow alpha blending
			fontImage = new BufferedImage(bitmapSize, bitmapSize, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = (Graphics2D)fontImage.getGraphics();
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int width = fm.stringWidth("W");
			int height = fm.getHeight();
			int lineWidth = (width > height) ? width * 16 : height * 16;
			if(!directionSet)
			{
				if(lineWidth > bitmapSize)
					delta = -2;
				else
					delta = 2;
				directionSet = true;
			}
			if(delta > 0)
			{
				if(lineWidth < bitmapSize)
					fontSize += delta;
				else
				{
					sizeFound = true;
					fontSize -= delta;
				}
			}
			else if(delta < 0)
			{
				if(lineWidth > bitmapSize)
					fontSize += delta;
				else
				{
					sizeFound = true;
					fontSize -= delta;
				}
			}
		}

		/* Now that a font size has been determined, create the final image, set the font and draw the
		 * standard/extended ASCII character set for that font.
		 */
		font = new Font(fontName, Font.BOLD, fontSize);  // Font Name
		// use BufferedImage.TYPE_4BYTE_ABGR to allow alpha blending
		fontImage = new BufferedImage(bitmapSize, bitmapSize, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)fontImage.getGraphics();
		g.setFont(font);
		g.setColor(new Color(0xFFFFFFFF, true));
		g.setBackground(new Color(0x00000000, true));
		FontMetrics fm = g.getFontMetrics();
		for(int i = 0; i < 256; i++)
		{
			int x = i % 16;
			int y = i / 16;
			char ch[] = {(char)i};
			String temp = new String(ch);
			g.drawString(temp, (x * 32) + 1, (y * 32) + fm.getAscent());
			charWidth[i] = fm.charWidth(ch[0]);
		}

		// Flip Image
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -fontImage.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		fontImage = op.filter(fontImage, null);

		// Put Image In Memory
		ByteBuffer scratch = ByteBuffer.allocateDirect(4 * fontImage.getWidth() * fontImage.getHeight());

		byte data[] = (byte[])fontImage.getRaster().getDataElements(0,0,fontImage.getWidth(),fontImage.getHeight(),null);
		scratch.clear();
		scratch.put(data);
		scratch.rewind();

		// Create A IntBuffer For Image Address In Memory
		IntBuffer buf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
		GL11.glGenTextures(buf); // Create Texture In OpenGL

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));
		// Typical Texture Generation Using Data From The Image

		// Linear Filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		// Linear Filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		// Generate The Texture
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, fontImage.getWidth(), fontImage.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, scratch);


		fontTexture = buf.get(0);                           // Return Image Address In Memory

		base = GL11.glGenLists(256);                    // Storage For 256 Characters

		/* Generate the display lists.  One for each character in the standard/extended ASCII chart.
		 */
		float textureDelta = 1.0f / 16.0f;
		for(int i = 0; i < 256; i++)
		{
			float u = ((i % 16)) / 16.0f;
			float v = 1.f - (((i / 16)) / 16.0f);
			GL11.glNewList(base + i, GL11.GL_COMPILE);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(u, v);
			GL11.glVertex3f(-0.0450f, 0.0450f, 0.0f);
			GL11.glTexCoord2f((u + textureDelta), v);
			GL11.glVertex3f(0.0450f, 0.0450f, 0.0f);
			GL11.glTexCoord2f((u + textureDelta), v - textureDelta);
			GL11.glVertex3f(0.0450f, -0.0450f, 0.0f);
			GL11.glTexCoord2f(u, v - textureDelta);
			GL11.glVertex3f(-0.0450f, -0.0450f, 0.0f);
			GL11.glEnd();
			GL11.glEndList();
		}
	}

	public BitmappedFont(String fontName, int fontSize) {
		this.fontName = fontName;
		this.fontSize = fontSize;
		buildFont();
	}

	public BitmappedFont(String fontName) {
		this.fontName = fontName;
		buildFont();
	}

	public BitmappedFont(int fontSize) {
		this.fontSize = fontSize;
		buildFont();
	}

	public BitmappedFont() {
		buildFont();
	}

	public void glString(String str, double pointWidth, double pointHeight) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture);
        GL11.glTranslated(8.0 * pointWidth, 8.0 * pointHeight, 0);
        GL11.glScaled(fontSize / 24.0, fontSize / 24.0, 1.0);
		for(int i = 0; i < str.length(); i++) {
			GL11.glCallList(base + str.charAt(i));
			GL11.glTranslated(pointWidth * charWidth[str.charAt(i)], 0.0, 0.0);
		}
	}
}
