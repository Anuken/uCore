package io.anuke.ucore.util;

import com.badlogic.gdx.utils.ObjectMap;

public class Timers{
	private static float time;
	private static ObjectMap<String, Float> timers = new ObjectMap<String, Float>();
	
	public static boolean get(float frames){
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		return get(e.getClassName() + e.getLineNumber(), frames);
	}
	
	public static boolean get(Object object, float frames){
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		return get(object.hashCode() + e.getClassName() + e.getLineNumber(), frames);
	}
	
	public static boolean get(String name, float frames){
		if(timers.containsKey(name)){
			float out = timers.get(name);
			if(time - out > frames){
				timers.put(name, time);
				return true;
			}else{
				return false;
			}
		}else{
			timers.put(name, time);
			return true;
		}
	}
	
	public static float time(){
		return time;
	}
	
	public static void update(float delta){
		time += delta;
	}
}
