package io.anuke.ucore.core;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.core.Inputs.DeviceType;
import io.anuke.ucore.core.Inputs.InputDevice;

public class KeyBinds{
	private static ObjectMap<String, ObjectMap<String, ObjectMap<DeviceType, Integer>>> map = new ObjectMap<>();
	/** Holds default keybind values. */
	private static ObjectMap<String, ObjectMap<String, ObjectMap<DeviceType, Integer>>> defaults = new ObjectMap<>();
	private static Array<String> sections = new Array<>();
	private static ObjectMap<String, Array<String>> keys = new ObjectMap<>();
	private static ObjectMap<String, InputDevice> sectionDevices = new ObjectMap<>();

	public static Iterable<String> getBinds(){
		return getBinds("default");
	}

	public static DeviceType getType(String section){
		return sectionDevices.get(section).type;
	}

	public static Iterable<String> getBinds(String section){
		if(!map.containsKey(section))
			throw new IllegalArgumentException("Section " + section + " not registered!");
		return keys.get(section);
	}

	public static void setDevice(String section, InputDevice dev){
		sectionDevices.put(section, dev);
	}

	public static Array<String> getSections(){
		return sections;
	}

	public static int get(String name){
		return get("default", getType("default"), name);
	}
	
	public static int get(String section, String name){
		return get(section, getType(section), name);
	}

	public static int get(String section, DeviceType type, String name){
		if(!map.containsKey(section))
			throw new IllegalArgumentException("Section " + section + " not registered!");
		
		if(!map.get(section).containsKey(name))
			throw new IllegalArgumentException("Key \"" + name + "\" not registered!");
		
		return map.get(section).get(name).get(type);
	}

	public static int getDefault(String section, String name){
		if(!defaults.containsKey(section))
			throw new IllegalArgumentException("Section \"" + section + "\" not registered!");
		if(!defaults.get(section).containsKey(name))
			throw new IllegalArgumentException("Key \"" + name + "\" not registered!");
		
		return defaults.get(section).get(name).get(getType(section));
	}

	public static void rebindKey(String name, int code){
		put("default", name, getType("default"),  code);
	}

	public static void rebindKey(String section, String name, int code){
		put(section, name, getType(section), code);
	}

	public static void rebindKey(String section, DeviceType type, String name, int code){
		put(section, name, type,  code);
	}

	/** Resets a key binding to default. */
	public static void resetKey(String name){
		resetKey("default", name);
	}

	/** Resets a key binding to default. */
	public static void resetKey(String section, String name){
		put(section, name, getType(section), getDefault(section, name));
	}

	/** Resets a key binding to default. */
	public static void resetKey(String section, DeviceType type, String name){
		put(section, name, type, getDefault(section, name));
	}

	/** Load the keybinds. Call after Settings.load() */
	public static void load(){
		for(String section : defaults.keys()){
			for(String key : defaults.get(section).keys()){
				for(DeviceType type : defaults.get(section).get(key).keys()){
					put(section, key, type, Settings.getIntKey("keybind-" + section + "-" + key + '-' + type.name(), defaults.get(section).get(key).get(type)));
				}
			}
		}
	}

	/** Save keybindings. */
	public static void saveBindings(){
		for(String section : defaults.keys()){
			for(String key : defaults.get(section).keys()){
				for(DeviceType type : defaults.get(section).get(key).keys()){
					Settings.putInt("keybind-" + section + "-" + key+ '-' + type.name(), get(section, type, key));
				}
			}
		}
		Settings.save();
	}

	/** Sets up key defaults. Format: name, keycode, name2, keycode2, etc */
	public static void defaults(Object... keys){
		defaultSection("default", DeviceType.keyboard, keys);
	}
	
	/**
	 * Sets up key defaults for a specific section. Format: section name, name,
	 * keycode, name2, keycode2, etc
	 */
	public static void defaultSection(String sec, Object... keys){
		defaultSection(sec, DeviceType.keyboard, keys);
	}

	/**
	 * Sets up key defaults for a specific section. Format: section name, name,
	 * keycode, name2, keycode2, etc
	 */
	public static void defaultSection(String sec, DeviceType type, Object... keys){
		addSection(sec);
		
		boolean add = KeyBinds.keys.get(sec).size == 0;
		
		for(int i = 0; i < keys.length; i += 2){
			put(sec, (String) keys[i], type,  (Integer) keys[i + 1]);
			putDefaults(sec, (String) keys[i], type, (Integer) keys[i + 1]);
			if(add)
			KeyBinds.keys.get(sec).add((String) keys[i]);
		}
		
		//check for missing device keys
		for(DeviceType d : DeviceType.values()){
			for(int i = 0; i < keys.length; i += 2){
				String name = (String)keys[i];
				if(!defaults.get(sec).get(name).containsKey(d)){
					//-2 is the 'unset' key
					put(sec, name, d, -2);
					putDefaults(sec, name, d, -2);
				}
			}
		}
	}

	private static void addSection(String name){
		if(keys.containsKey(name)) return;
		
		map.put(name, new ObjectMap<>());
		defaults.put(name, new ObjectMap<>());
		sections.add(name);
		keys.put(name, new Array<>());
		sectionDevices.put(name, Inputs.getDevices().get(0));
	}

	private static void put(String section, String name, DeviceType type,  int key){
		ObjectMap<DeviceType, Integer> sec = map.get(section).get(name);

		if(sec == null){
			sec = new ObjectMap<>();
			map.get(section).put(name, sec);
		}

		sec.put(type, key);
	}

	private static void putDefaults(String section, String name, DeviceType type, int key){
		ObjectMap<DeviceType, Integer> sec = defaults.get(section).get(name);

		if(sec == null){
			sec = new ObjectMap<>();
			defaults.get(section).put(name, sec);
		}

		sec.put(type, key);
	}

	//TODO
	public static String toString(String section, int keycode){
		if(!sectionDevices.containsKey(section))
			throw new IllegalArgumentException("Section \"" + section + "\" not registered!");
		
		if(keycode == -2)
			return "Unset";

		InputDevice d = sectionDevices.get(section);

		if(d.type == DeviceType.keyboard){
			return Keys.toString(keycode);
		}else{
			try{
				return (String)Inputs.invokeControl("io.anuke.ucontrol.Xkeys", "toString", keycode);
			}catch (Throwable e){}
			
			return "nil";
		}
	}
}
