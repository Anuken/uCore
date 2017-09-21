package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Pool.Poolable;

import io.anuke.ucore.function.Callable;
import io.anuke.ucore.function.Supplier;

public class Timers{
	private static float time;
	private static ObjectMap<String, Float> timers = new ObjectMap<String, Float>();
	private static DelayedRemovalArray<DelayRun> runs = new  DelayedRemovalArray<>();
	private static long lastMark = 0;
	private static Supplier<Float> deltaimpl = ()->Math.min(Gdx.graphics.getDeltaTime()*60f, 4f);
	
	public static void run(float delay, Callable r){
		DelayRun run = Pools.obtain(DelayRun.class);
		run.finish = r;
		run.delay = delay;
		runs.add(run);
	}
	
	public static void runFor(float duration, Callable r){
		DelayRun run = Pools.obtain(DelayRun.class);
		run.run = r;
		run.delay = duration;
		runs.add(run);
	}
	
	public static void runFor(float duration, Callable r, Callable finish){
		DelayRun run = Pools.obtain(DelayRun.class);
		run.run = r;
		run.delay = duration;
		run.finish = finish;
		runs.add(run);
	}
	
	public static void reset(Object o, String label, float duration){
		timers.put(o.hashCode() + label, time - duration);
	}
	
	public static void clear(){
		runs.clear();
		timers.clear();
	}
	
	public static float getTime(Object object, String label){
		return time() - timers.get(object.hashCode() + label, 0f);
	}
	
	public static boolean get(Object object, float frames){
		return get(object.hashCode() +"", frames);
	}
	
	public static boolean get(Object object, String label, float frames){
		return get(object.hashCode() +label, frames);
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
	
	public static void mark(){
		lastMark = TimeUtils.millis();
	}
	
	public static long elapsed(){
		return TimeUtils.timeSinceMillis(lastMark);
	}
	
	/**Use normal delta time (e. g. gdx delta * 60)*/
	public static void update(){
		float delta = delta();
		time += delta;
		
		runs.begin();
		
		for(DelayRun run : runs){
			run.delay -= delta;
			
			if(run.run != null)
				run.run.run();
			
			if(run.delay <= 0){
				if(run.finish != null)
					run.finish.run();
				runs.removeValue(run, true);
				Pools.free(run);
			}
		}
		
		runs.end();
	}
	
	static class DelayRun implements Poolable{
		float delay;
		Callable run;
		Callable finish;
		
		@Override
		public void reset(){
			delay = 0;
			run = finish = null;
		}
	}

	public static float delta(){
		return deltaimpl.get();
	}

	public static void setDeltaProvider(Supplier<Float> impl){
		deltaimpl = impl;
	}
}
