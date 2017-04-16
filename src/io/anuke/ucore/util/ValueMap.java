package io.anuke.ucore.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

public class ValueMap{
	private ObjectMap<String, Object> map = new ObjectMap<String, Object>();
	
	public void put(String name, Object object){
		map.put(name, object);
	}
	
	public <T> T get(String name){
		return (T)map.get(name);
	}
	
	public boolean has(String name){
		return map.containsKey(name);
	}
	
	public int getInt(String name){
		if(!has(name)) return 0;
		return (int)(Integer)get(name);
	}
	
	public float getFloat(String name){
		if(!has(name)) return 0;
		return (float)(Float)get(name);
	}
	
	public boolean getBoolean(String name){
		if(!has(name)) return false;
		return (boolean)(Boolean)get(name);
	}
	
	public Color getColor(String name){
		return (Color)get(name);
	}
}
