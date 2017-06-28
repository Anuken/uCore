package io.anuke.ucore.cui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;

import io.anuke.ucore.UCore;

public class Stylesheet{
	private static final Json json = new Json();
	/**???*/
	private ObjectMap<String, ObjectMap<Class<?>, Object>> objects = new ObjectMap<>();
	/**Map of style names to actual styles (e.g. "background" -> drawable)*/
	private ObjectMap<String, ObjectMap<String, Object>> styles = new ObjectMap<>();
	
	public Stylesheet(FileHandle file){
		String text = file.readString();
		
		JsonValue value = json.fromJson(null, text);
		
		UCore.log(value.toString());
	}
	
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
