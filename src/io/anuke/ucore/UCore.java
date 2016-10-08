package io.anuke.ucore;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;

public class UCore {
	
	public static <T>boolean inBounds(int x, int y, T[][] array){
		return x >= 0 && y >= 0 && x < array.length && y < array[0].length;
	}

	public static <T>boolean inBounds(int x, int y, int z, T[][][] array){
		return x >= 0 && y >= 0 && z >= 0 && x < array.length && y < array[0].length && z < array[0][0].length;
	}

	public static <T>boolean inBounds(int x, int y, int z, int[][][] array){
		return x >= 0 && y >= 0 && z >= 0 && x < array.length && y < array[0].length && z < array[0][0].length;
	}

	public static boolean inBounds(int x, int y, int z, int size, int padding){
		return x >= padding && y >= padding && z >= padding && x < size - padding && y < size - padding && z < size - padding;
	}

	public static <T>boolean inBounds(int x, int y, int width, int height){
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	public static float clamp(float i, float min, float max){
		if(i < min) i = min;
		if(i > max) i = max;
		return i;
	}

	public static int clamp(int i, int min, int max){
		if(i < min) i = min;
		if(i > max) i = max;
		return i;
	}
	
	/** Requires LWJGL3. */
	public static void maximizeWindow() {
		try {
			Class.forName("org.lwjgl.glfw.GLFW").getMethod("glfwMaximizeWindow", long.class).invoke(null, getWindowHandle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Requires LWJGL3. */
	public static long getWindowHandle() {
		try {
			Object o = Gdx.graphics;
			Object window = o.getClass().getMethod("getWindow").invoke(o);
			Class<?> wc = window.getClass();

			Field f = wc.getDeclaredField("windowHandle");
			f.setAccessible(true);
			long l = f.getLong(window);
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
