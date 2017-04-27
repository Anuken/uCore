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
	
	/**Loads keybinds as well as prefs.*/
	public static void loadAll(String name){
		load(name);
		KeyBinds.load();
	}
	
	public static Object getDefault(String name){
		return defaults.get(name);
	}
	
	public static void putString(String name, String val){
		prefs.putString(name, val);
	}
	
	public static void putFloat(String name, float val){
		prefs.putFloat(name, val);
	}
	
	public static void putInt(String name, int val){
		prefs.putInteger(name, val);
	}
	
	public static void putBool(String name, boolean val){
		prefs.putBoolean(name, val);
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
	
	public static int getIntKey(String name, int def){
		return prefs.getInteger(name, def);
	}
	
	public static void save(){
		prefs.flush();
	}
	
	/**Sets a default value up.
	*  This is REQUIRED for every pref value.*/
	public static void defaults(String name, Object object){
		defaults.put(name, object);
	}
}
