package io.anuke.ucore.core;

import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import io.anuke.ucore.function.Event;

public class Events{
	private static ObjectMap<Class<? extends Event>, Method> methodCache = new ObjectMap<>();
	private static ObjectMap<Class<?>, Class<?>> primitiveClassMap = new ObjectMap<Class<?>, Class<?>>(){{
		put(Integer.class, int.class);
		put(Float.class, float.class);
		put(Boolean.class, boolean.class);
		put(Double.class, double.class);
		put(Byte.class, byte.class);
		put(Short.class, short.class);
		put(Long.class, long.class);
	}};
	
	private static ObjectMap<Class<? extends Event>, Array<Event>> events = new ObjectMap<>();
	
	public static <T extends Event> void on(Class<T> type, T listener){
		if(events.get(type) == null)
			events.put(type, new Array<>());
		
		events.get(type).add(listener);
	}
	
	public static <T extends Event> void fire(Class<T> type, Object... args){
		if(events.get(type) == null)
			return;
		
		Method method = getMethod(type, args);
		
		for(Event event : events.get(type)){
			try{
				method.invoke(event, args);
			}catch (ReflectionException e){
				e.printStackTrace();
				throw new IllegalArgumentException("Exception occurred calling event: event exception, or wrong number or type of arguments!");
			}
		}
	}
	
	private static Method getMethod(Class<? extends Event> type, Object... args){
		if(methodCache.containsKey(type)){
			return methodCache.get(type);
		}else{
			Class[] classes = new Class[args.length];
			for(int i = 0; i < classes.length; i ++){
				classes[i] = primitiveClassMap.get(args[i].getClass(), args[i].getClass());
			}
			
			try{
			
				Method method = ClassReflection.getMethod(type, "handle", classes);
				
				methodCache.put(type, method);
				return method;
			}catch (ReflectionException e){
				throw new IllegalArgumentException("Unable to find method \"handle\" for class \"" 
						+ ClassReflection.getSimpleName(type) + "\" and argument type(s) " 
						+ Arrays.toString(classes) + ". Make sure you have a handle method declared, and that the argument "
								+ "types are correct.");
			}
		}
	}
	
}
