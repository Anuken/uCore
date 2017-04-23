package io.anuke.ucore.util;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;

public class Mathf{
	
	/**X/Y flipped, returns degrees*/
	public static float atan2(float x, float y){
		return (float)Math.atan2(x, y) * MathUtils.radDeg;
	}
	
	public static int random(int a, int b){
		return MathUtils.random(a, b);
	}
	
	public static int random(int range){
		return MathUtils.random(range);
	}
	
	public static float random(float a, float b){
		return MathUtils.random(a, b);
	}
	
	public static float random(float range){
		return MathUtils.random(range);
	}
	
	public static Random random(){
		return MathUtils.random;
	}
	
	public static float round(float a, float b){
		return (int)(a/b)*b;
	}
	
	public static int scl(float a, float b){
		return (int)(a/b);
	}
	
	public static boolean inRect(float x, float y, float bx, float by, float tx, float ty){
		return x > bx && y > by && x < tx && y < ty;
	}
	
	public static boolean intersect(float x1, float y1, float s1, float x2, float y2, float s2){
		return MathUtils.isEqual(x1, x2, s1+s2) && MathUtils.isEqual(y1, y2, s1+s2);
	}

	public static float clamp(float i, float min, float max){
		if(i < min)
			i = min;
		if(i > max)
			i = max;
		return i;
	}
	
	/**Clamps to [0,1]*/
	public static float clamp(double i){
		return clamp((float)i,0,1);
	}
	
	public static boolean between(float i, float min, float max){
		return i > min && i < max;
	}

	public static int clamp(int i, int min, int max){
		if(i < min)
			i = min;
		if(i > max)
			i = max;
		return i;
	}
	
	public static <T> boolean inBounds(int x, int y, T[][] array){
		return x >= 0 && y >= 0 && x < array.length && y < array[0].length;
	}

	public static <T> boolean inBounds(int x, int y, int z, T[][][] array){
		return x >= 0 && y >= 0 && z >= 0 && x < array.length && y < array[0].length && z < array[0][0].length;
	}

	public static <T> boolean inBounds(int x, int y, int z, int[][][] array){
		return x >= 0 && y >= 0 && z >= 0 && x < array.length && y < array[0].length && z < array[0][0].length;
	}

	public static boolean inBounds(int x, int y, int z, int size, int padding){
		return x >= padding && y >= padding && z >= padding && x < size - padding && y < size - padding
				&& z < size - padding;
	}

	public static <T> boolean inBounds(int x, int y, int width, int height){
		return x >= 0 && y >= 0 && x < width && y < height;
	}
}
