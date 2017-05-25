package opengl.framework.opengl.loader;

import java.util.Comparator;

import org.lwjgl.opengl.DisplayMode;

public class DisplayModeComparator implements Comparator<DisplayMode> {

	@Override
	public int compare(DisplayMode o1, DisplayMode o2) {
		int result = new Integer(o1.getWidth()).compareTo(o2.getWidth());
		if(result != 0) return result;
		result = new Integer(o1.getHeight()).compareTo(o2.getHeight());
		if(result != 0) return result;
		result = new Integer(o1.getBitsPerPixel()).compareTo(o2.getBitsPerPixel());
		if(result != 0) return result;
		result = new Integer(o1.getFrequency()).compareTo(o2.getFrequency());
		return result;
	}

}
