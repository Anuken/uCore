package io.anuke.ucore.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class KeyBinds{
	private static ObjectMap<String, Integer> map = new ObjectMap<>();
	/**Holds default keybind values.*/
	private static ObjectMap<String, Integer> defaults = new ObjectMap<>();
	private static Array<String> keys = new Array<>();
	
	public static Iterable<String> getBinds(){
		return keys;
	}
	
	public static int get(String name){
		if(!map.containsKey(name)) throw new IllegalArgumentException("Key " + name + " not registered!");
		return map.get(name);
	}
	
	public static void rebindKey(String name, int code){
		map.put(name, code);
	}
	
	/**Resets a key binding to default.*/
	public static void resetKey(String name){
		map.put(name, defaults.get(name));
	}
	
	/**Load the keybinds. Call after Settings.load()*/
	public static void load(){
		for(String key : getBinds()){
			map.put(key, Settings.getIntKey("keybind-"+key, defaults.get(key)));
		}
	}
	
	/**Save keybindings.*/
	public static void saveBindings(){
		for(String key : getBinds()){
			Settings.putInt("keybind-"+key, get(key));
		}
		Settings.save();
	}
	
	/**Sets up key defaults. Format: name, keycode, name2, keycode2, etc*/
	public static void defaults(Object... keys){
		for(int i = 0; i < keys.length; i +=2){
			map.put((String)keys[i], (Integer)keys[i+1]);
			defaults.put((String)keys[i], (Integer)keys[i+1]);
			KeyBinds.keys.add((String)keys[i]);
		}
	}
}
