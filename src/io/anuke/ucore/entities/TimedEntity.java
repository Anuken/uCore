package io.anuke.ucore.entities;

import io.anuke.ucore.core.Timers;
import io.anuke.ucore.util.Mathf;

public abstract class TimedEntity extends Entity{
	public float lifetime;
	public float time;
	
	@Override
	public void update(){
		time += Timers.delta();

		time = Mathf.clamp(time, 0, lifetime);
		
		if(time >= lifetime) remove();
	}
	
	public float fract(){
		return 1f-time/lifetime;
	}
	
	public float ifract(){
		return time/lifetime;
	}
	
	public float sfract(){
		return (0.5f-Math.abs(time/lifetime-0.5f))*2f;
	}
}
