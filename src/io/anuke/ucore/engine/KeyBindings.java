package io.anuke.ucore.engine;

import com.badlogic.gdx.utils.ObjectMap;

public class KeyBindings{
	private static ObjectMap<String, Integer> map = new ObjectMap<>();
	
	public static int getKey(String name){
		return map.get(name);
	}
	
	public static void bindKey(String name, int code){
		map.put(name, code);
	}
}
