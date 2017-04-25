package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class USound{
	private static ObjectMap<String, Sound> map = new ObjectMap<>();
	private static float volume = 1f;
	
	public static void load(String... names){
		for(String s : names){
			map.put(s.split(".")[0], Gdx.audio.newSound(Gdx.files.internal("sounds/"+s)));
		}
	}
	
	public static Sound get(String name){
		return map.get(name);
	}
	
	public static void play(String name){
		long id = get(name).play();
		get(name).setVolume(id, volume);
	}
	
	public static void setVolume(float vol){
		volume = vol;
	}
}
