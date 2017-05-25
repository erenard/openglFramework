package opengl.framework.opengl.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.lwjgl.LWJGLUtil;

public class Loader {
	public static void appendNativePathToUserPath(String nativeLibraryPath) throws IOException {
		String s = nativeLibraryPath + File.separatorChar + LWJGLUtil.getPlatformName();
		try {
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String [] paths = (String []) field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (s.equals(paths[i])) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = s;
			field.set(null, tmp);
		} catch (IllegalAccessException e) {
			throw new IOException("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new IOException("Failed to get field handle to set library path");
		}
	}
}
