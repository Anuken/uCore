package io.anuke.ucore.entities;

public abstract class TimedEntity extends Entity{
	public float lifetime;
	public float time;
	
	@Override
	public void update(){
		time += delta;
		
		if(time >= lifetime) remove();
	}
	
	public float fract(){
		return 1f-time/lifetime;
	}
}
