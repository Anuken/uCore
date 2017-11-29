package io.anuke.ucore.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.TimeUtils;

import io.anuke.ucore.core.Timers;

public class Profiler{
	private static String[] displayColors = {"red", "orange", "yellow", "lime", "olive", "green", "forest", "cyan", "royal", "blue"};
	private static OrderedMap<String, Long> times = new OrderedMap<>();
	private static ObjectMap<String, Long> lastTimes = new ObjectMap<>();
	private static int updateSpacing = 30;
	private static long lastFrame = 0;
	private static boolean update = true;
	
	public static void begin(String section){
		lastTimes.put(section, TimeUtils.nanoTime());
		if(times.get(section) == null)
			times.put(section, TimeUtils.nanoTime());
	}
	
	public static void end(String section){
		if(Gdx.graphics.getFrameId() != lastFrame){
			update = Timers.get("profile", updateSpacing);
			lastFrame = Gdx.graphics.getFrameId();
		}
		
		if(lastTimes.get(section) == null) throw new RuntimeException("Call begin() before end()!");
		if(update) times.put(section, TimeUtils.timeSinceNanos(lastTimes.get(section)));
		lastTimes.put(section, null);
	}
	
	public static OrderedMap<String, Long> getTimes(){
		return times;
	}
	
	public static boolean updating(){
		return update;
	}
	
	public static String formatDisplayTimes(){
		return formatDisplayTimes(true);
	}
	
	public static String formatDisplayTimes(boolean useColor){
		if(!times.containsKey("total")) 
			throw new RuntimeException("The profiler must include a 'total' section in order for display time formatting to work!");
		
		long total = times.get("total");
		
		int index = 0;
		StringBuilder builder = new StringBuilder();
		for(String section : times.keys()){
			if(useColor) builder.append("["+displayColors[index]+"]");
			builder.append(section);
			builder.append(": ");
			builder.append(Strings.toFixed((float)times.get(section) / total * 100, 1));
			builder.append("% - ");
			builder.append(times.get(section));
			if(index != times.size - 1)
				builder.append("\n");
			
			index ++;
			if(index >= displayColors.length) index = displayColors.length-1;
		}
		
		return builder.toString();
	}
}
