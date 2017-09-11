package io.anuke.ucore.util;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import io.anuke.ucore.core.Timers;

//TODO
public class Tween{
	private static ObjectMap<Class<?>, Field> fieldCache = new ObjectMap<>();
	
	public static void run(Object object, String field, float from, float to, float duration, Interpolation in){
		run(true, object, field, from, to, duration, in);
	}
	
	private static void run(boolean reset, Object object, String field, float from, float to, float duration, Interpolation in){
		Field cfield = getField(object.getClass(), field);
		float[] accumulator = {0};
		
		Timers.runFor(duration, ()->{
			try{
				accumulator[0] += 1f/duration*Timers.delta();
				cfield.set(object, in.apply(from, to, accumulator[0]));
			}catch (ReflectionException e){
				throw new RuntimeException(e);
			}
		});
	}
	
	private static Field getField(Class<?> type, String name){
		Field field = fieldCache.get(type);
		
		if(field == null){
			try{
				field = ClassReflection.getDeclaredField(type, name);
				fieldCache.put(type, field);
			}catch (ReflectionException e){
				throw new RuntimeException(e);
			}
		}
		
		return field;
	}
	
	private static class TweenContainer{
		private float offset;
		private Object object;
		
		public void run(String field, float from, float to, float duration, Interpolation in){
			Tween.run(false, object, field, from, to, duration, in);
		}
	}
}
