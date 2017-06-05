package io.anuke.ucore.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Timers{
	private static float time;
	private static ObjectMap<String, Float> timers = new ObjectMap<String, Float>();
	private static Array<DelayRun> runs = new Array<>();
	private static Array<DelayRun> remove = new Array<DelayRun>();
	
	public static void run(float delay, Runnable r){
		DelayRun run = new DelayRun();
		run.run = r;
		run.delay = delay;
		runs.add(run);
	}
	/*
	public static boolean get(float frames){
		StackTraceElement e = Thread.currentThread().getStackTrace()[2];
		return get(e.getClassName() + e.getLineNumber(), frames);
	}
	*/
	
	public static void clear(){
		runs.clear();
		remove.clear();
		timers.clear();
	}
	
	public static boolean get(Object object, float frames){
		return get(object.hashCode() +"", frames);
	}
	
	public static boolean get(String name, float frames){
		if(timers.containsKey(name)){
			float out = timers.get(name);
			if(time - out > frames){
				timers.put(name, time);
				return true;
			}else{
				return false;
			}
		}else{
			timers.put(name, time);
			return true;
		}
	}
	
	public static float time(){
		return time;
	}
	
	public static void update(){
		update(Gdx.graphics.getDeltaTime()*60f);
	}
	
	/**Use normal delta time (e. g. gdx delta * 60)*/
	public static void update(float delta){
		time += delta;
		
		remove.clear();
		for(DelayRun run : runs){
			run.delay -= delta;
			if(run.delay <= 0){
				run.run.run();
				remove.add(run);
			}
		}
		
		runs.removeAll(remove, true);
	}
	
	static class DelayRun{
		float delay;
		Runnable run;
	}
}
