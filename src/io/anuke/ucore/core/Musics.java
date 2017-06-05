package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.util.Mathf;

public class Musics{
	private static Array<Music> music = new Array<>();
	private static ObjectMap<String, Music> map = new ObjectMap<>();
	private static Music playing;
	private static float volume = 1f;
	private static boolean shuffling = false;

	/** Requires file extensions (e.g "something.mp3") */
	public static void load(String... names){
		Settings.defaults("musicvol", 10);
		
		for(String s : names){
			music.add(Gdx.audio.newMusic(Gdx.files.internal("music/" + s)));
			map.put(s.split("\\.")[0], music.peek());
			
			music.peek().setOnCompletionListener(new OnCompletionListener(){
				public void onCompletion(Music other){
					if(!shuffling)
						return;
					
					float vol = Settings.getInt("musicvol")/10f;
					while(playing == other){
						playing = music.get(Mathf.random(music.size - 1));
					}
					
					playing.setVolume(volume*vol);
					playing.play();
				}
			});
		}
	}

	public static void setVolume(float vol){
		volume = vol;
	}

	public static Music getPlaying(){
		return playing;
	}

	public static void loop(String name){
		float vol = Settings.getInt("musicvol")/10f;
		get(name).setLooping(true);
		get(name).setVolume(volume*vol);
		playing = get(name);
	}
	
	public static void updateVolume(){
		if(playing == null) return;
		float vol = Settings.getInt("musicvol")/10f*volume;
		playing.setVolume(vol);
	}

	public static void shuffleAll(){
		float vol = Settings.getInt("musicvol")/10f;
		shuffling = true;
		playing = music.get(Mathf.random(music.size - 1));
		playing.play();
		playing.setVolume(volume*vol);
	}

	public static Music get(String name){
		return map.get(name);
	}
}
