package io.anuke.ucore.core;

import com.badlogic.gdx.utils.ObjectMap;

public class KeyBinds{
	private static ObjectMap<String, Integer> map = new ObjectMap<>();
	
	public static int get(String name){
		return map.get(name);
	}
	
	public static void bindKey(String name, int code){
		map.put(name, code);
	}
}
