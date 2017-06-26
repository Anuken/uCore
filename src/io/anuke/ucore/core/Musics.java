package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.UCore;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Timers;

public class Musics{
	private static String[] mnames;
	private static Array<Music> music = new Array<>();
	private static ObjectMap<String, Music> map = new ObjectMap<>();
	private static Music playing;
	private static float volume = 1f;
	private static String[] shufflenames = null;
	private static boolean shuffling = false;

	/** Requires file extensions (e.g "something.mp3") */
	public static void load(String... names){
		Settings.defaults("musicvol", 10);
		mnames = new String[names.length];
		
		int i = 0;
		
		for(String s : names){
			String name = s.split("\\.")[0];
			music.add(Gdx.audio.newMusic(Gdx.files.internal("music/" + s)));
			map.put(name, music.peek());
			
			mnames[i++] = name;
			
			music.peek().setOnCompletionListener(new OnCompletionListener(){
				public void onCompletion(Music other){
					if(!shuffling)
						return;
					
					float vol = Settings.getInt("musicvol")/10f;
					String[] names = shufflenames == null ? mnames : shufflenames;
					while(playing == other){
						playing = get(names[Mathf.random(names.length - 1)]);
					}
					
					playing.setVolume(volume*vol);
					playing.play();
				}
			});
		}
	}
	
	public static void fadeIn(String name){
		shuffling = false;
		Music m = get(name);
		
		if(m == playing) return;
		
		float vol = Settings.getInt("musicvol")/10f*volume;
		
		m.setLooping(true);
		m.setVolume(0f);
		m.play();
		
		float time = 100f;
		
		if(playing != null){
			Timers.runFor(time, ()->{
				UCore.log(m.getVolume());
				m.setVolume(m.getVolume()+Mathf.delta()/100f*vol);
				playing.setVolume(m.getVolume()-Mathf.delta()/100f*vol);
			}, ()->{
				playing.stop();
				playing = m;
			});
		}else{
			m.setVolume(vol);
			playing = m;
		}
	}
	
	public static void setVolume(float vol){
		volume = vol;
	}

	public static Music getPlaying(){
		return playing;
	}

	public static void loop(String name){
		if(playing == get(name)) return;
		
		if(playing != null){
			playing.stop();
		}
		
		float vol = Settings.getInt("musicvol")/10f;
		get(name).setLooping(true);
		get(name).setVolume(volume*vol);
		get(name).play();
		playing = get(name);
	}
	
	public static void updateVolume(){
		if(playing == null) return;
		float vol = Settings.getInt("musicvol")/10f*volume;
		playing.setVolume(vol);
	}
	
	public static void shuffle(String... names){
		if(shuffling) return;
		
		if(playing != null){
			playing.stop();
			playing = null;
		}
		
		//UCore.log("shuffling " + Arrays.toString(names), Mathf.random(names.length - 1));
		
		float vol = Settings.getInt("musicvol")/10f;
		shufflenames = names;
		shuffling = true;
		playing = get(names[Mathf.random(names.length - 1)]);
		playing.play();
		playing.setVolume(volume*vol);
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
