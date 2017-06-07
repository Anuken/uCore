package io.anuke.ucore.cui;

import com.badlogic.gdx.utils.ObjectMap;

public class Style{
	protected ObjectMap<String, Object> map = new ObjectMap<>();
	
	protected void apply(ObjectMap<String, Object> top){
		for(String key : top.keys()){
			map.put(key, top.get(key));
		}
	}
	
	public boolean has(String name){
		return map.containsKey(name);
	}
}
