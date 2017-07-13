package io.anuke.ucore.ecs;

import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public abstract class Prototype{
	private static int lastid;
	private static Array<Prototype> types = new Array<>();
	
	private static ObjectMap<Class<? extends SparkEvent>, Method> methodCache = new ObjectMap<>();
	
	private final int id;
	/**Current events. When overriding an already existing Prototype class, the new handler gets added onto the array.*/
	private ObjectMap<Class<? extends SparkEvent>, Array<SparkEvent>> events = new ObjectMap<>();
	public abstract TraitList traits();
	
	public Prototype(){
		id = lastid++;
		types.add(this);
	}
	
	public int getTypeID(){
		return id;
	}
	
	/**Registers an event listener. Do this in the constructor.*/
	protected <T extends SparkEvent> void event(Class<T> type, T event){
		if(!events.containsKey(type))
			events.put(type, new Array<>());
		
		events.get(type).add(event);
	}
	
	/**Calls an event, with an optional value used when null is returned otherwise.*/
	public <T extends SparkEvent, N> N callEvent(N defValue, Class<T> type, Object... arguments){
		Object out = callEvent(type, 0, arguments);
		return out == null ? defValue : (N)out;
	}
	
	/**Calls an event. This does not provide a return object.*/
	public <T extends SparkEvent> void callEvent(Class<T> type, Object... arguments){
		callEvent(type, 0, arguments);
	}
	
	/**Calls an event on the last inherited prototype that implemented it. If there's nothing to call, nothing happens.*/
	public <T extends SparkEvent> Object callSuper(Class<T> type, Object... arguments){
		return callEvent(type, 1, arguments);
	}
	
	/**Calls an event with an optional number of "super-overrides." May return null.*/
	private <T extends SparkEvent> Object callEvent(Class<T> type, int supers, Object... arguments){
		
		Array<SparkEvent> list = events.get(type);
		
		//no handlers found for this event type
		if(list == null || list.size == 0 || list.size <= supers)
			return null;
		
		SparkEvent handler = list.get(list.size-1-supers);
		
		Method method = getMethod(type, arguments);
		
		try{
			return method.invoke(handler, arguments);
		}catch (ReflectionException e){
			throw new RuntimeException(e);
		}
	}
	
	private static Method getMethod(Class<? extends SparkEvent> type, Object... args){
		if(methodCache.containsKey(type)){
			return methodCache.get(type);
		}else{
			Class[] classes = new Class[args.length];
			for(int i = 0; i < classes.length; i ++){
				classes[i] = args[i].getClass();
			}
			
			try{
			
				Method method = ClassReflection.getMethod(type, "handle", classes);
				
				methodCache.put(type, method);
				return method;
			}catch (ReflectionException e){
				throw new IllegalArgumentException("Unable to find method \"handle\" for class \"" 
						+ ClassReflection.getSimpleName(type) + "\" and argument type(s) " 
						+ Arrays.toString(classes) + ". Make sure you have a handle method declared, and that the argument "
								+ "types are correct.", e);
			}
		}
	}
	
	public static Array<Prototype> getAllTypes(){
		return types;
	}
}
