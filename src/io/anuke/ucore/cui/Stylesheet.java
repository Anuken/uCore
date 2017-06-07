package io.anuke.ucore.cui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Stylesheet{
	private ObjectMap<String, ObjectMap<Class<?>, Object>> objects = new ObjectMap<>();
	private ObjectMap<String, ObjectMap<String, Object>> styles = new ObjectMap<>();
	
	public void getStyle(Style style, String basename, Array<String> extraStyles){
		ObjectMap<String, Object> base = styles.get(basename);
		style.map.clear();
		if(base != null){
			style.apply(base);
		}
		
		for(String extra : extraStyles){
			ObjectMap<String, Object> get = styles.get(extra);
			if(get == null){
				throw new RuntimeException("The specified style \"" + extra + "\" does not exist.");
			}
			
			style.apply(get);
		}
	}
}
