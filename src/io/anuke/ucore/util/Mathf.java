package io.anuke.ucore.util;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Mathf{
	private static Random seedr = new Random();
	
	/**X/Y flipped, returns degrees*/
	public static float atan2(float x, float y){
		return (float)Math.atan2(x, y) * MathUtils.radDeg;
	}
	
	public static float sin(float in, float scl, float mag){
		return MathUtils.sin(in/scl)*mag;
	}
	
	public static float absin(float in, float scl, float mag){
		return Math.abs(sin(in, scl*2f, mag));
	}
	
	public static float pow(float a, float b){
		return (float)Math.pow(a, b);
	}
	
	public static <T> T choose(T... items){
		return items[random(0, items.length-1)];
	}
	
	public static boolean near(float a, float b, float range){
		return Math.abs(a-b) < range;
	}
	
	public static boolean near2d(float x, float y, float x2, float y2, float range){
		return near(x, x2, range) && near(y, y2, range);
	}
	
	public static boolean angNear(float a, float b, float range){
		return Math.abs(a-b) < range || Math.abs((360f-a)-b) < range;
	}
	
	public static float slerp(float angle, float target, float alpha){
		return MathUtils.lerpAngleDeg(angle, target, alpha);
	}
	
	public static float lerp(float from, float to, float alpha){
		return MathUtils.lerp(from, to, alpha);
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
	
	public static int randomSeed(int seed, int min, int max){
		seedr.setSeed(seed);
		return seedr.nextInt(max-min+1)+min;
	}
	
	public static float range(float range){
		return random(-range, range);
	}
	
	public static int range(int range){
		return random(-range, range);
	}
	
	public static float range(float min, float max){
		if(chance(0.5)){
			return random(min, max);
		}else{
			return -random(min, max);
		}
	}
	
	public static boolean chance(double d){
		return Math.random() < d;
	}
	
	public static int roundi(float a, int b){
		return (int)(a/b)*b;
	}
	
	public static float round(float a, float b){
		return (int)(a/b)*b;
	}
	
	public static float round2(float a, float b){
		return Math.round(a/b)*b;
	}
	
	public static int scl(float a, float b){
		return (int)(a/b);
	}
	
	public static int scl2(float a, float b){
		return Math.round(a/b);
	}
	
	public static float delta(){
		return Gdx.graphics.getDeltaTime()*60f;
	}
	
	public static boolean inRect(float x, float y, float bx, float by, float tx, float ty){
		return x > bx && y > by && x < tx && y < ty;
	}
	
	
	public static boolean intersect(float x1, float y1, float s1, float x2, float y2, float s2){
		return MathUtils.isEqual(x1, x2, s1+s2) && MathUtils.isEqual(y1, y2, s1+s2);
	}
	
	public static int sign(float f){
		return(f < 0 ? -1 : 1);
	}
	
	public static int sign(boolean b){
		return b ? 1 : -1;
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
