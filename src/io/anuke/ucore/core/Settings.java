package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ObjectMap;

public class Settings{
	private static Preferences prefs;
	private static ObjectMap<String, Object> defaults = new ObjectMap<>();
	
	public static void load(String name){
		prefs = Gdx.app.getPreferences(name);
	}
	
	public static Object getDefault(String name){
		return defaults.get(name);
	}
	
	public static String getString(String name){
		return prefs.getString(name, (String)defaults.get(name));
	}
	
	public static float getFloat(String name){
		return prefs.getFloat(name, (Float)defaults.get(name));
	}
	
	public static int getInt(String name){
		return prefs.getInteger(name, (Integer)defaults.get(name));
	}
	
	public static boolean getBool(String name){
		return prefs.getBoolean(name, (Boolean)defaults.get(name));
	}
	
	/**Sets a default value up.
	*  This is REQUIRED for every pref value.*/
	public static void defaults(String name, Object object){
		defaults.put(name, object);
	}
}
