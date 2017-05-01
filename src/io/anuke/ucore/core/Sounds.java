package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class Sounds{
	private static ObjectMap<String, Sound> map = new ObjectMap<>();
	private static float volume = 1f;
	
	public static void load(String... names){
		for(String s : names){
			map.put(s.split("\\.")[0], Gdx.audio.newSound(Gdx.files.internal("sounds/"+s)));
		}
	}
	
	public static Sound get(String name){
		return map.get(name);
	}
	
	public static void play(String name){
		if(map.containsKey(name)) throw new IllegalArgumentException("Sound \""+name+"\" does not exist!");
		long id = get(name).play();
		float vol = Settings.getInt("sfxvol", 10)/10f;
		get(name).setVolume(id, volume*vol);
	}
	
	public static void setVolume(float vol){
		volume = vol;
	}
}
