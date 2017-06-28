package io.anuke.ucore.cui;

import com.badlogic.gdx.utils.ObjectMap;

public class Style{
	protected ObjectMap<String, Object> map = new ObjectMap<>();
	
	protected void apply(ObjectMap<String, Object> top){
		for(String key : top.keys()){
			map.put(key, top.get(key));
		}
	}
	
	public <T> T get(String name){
		if(!has(name)){
			throw new IllegalArgumentException("The specified resource \"" + name + "\" does not exist");
		}
		
		return (T)map.get(name);
	}
	
	public boolean has(String name){
		return map.containsKey(name);
	}
}
