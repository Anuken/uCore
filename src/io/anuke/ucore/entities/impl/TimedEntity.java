package io.anuke.ucore.entities.impl;

import com.badlogic.gdx.utils.Pool.Poolable;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Scalable;

public abstract class TimedEntity extends BaseEntity implements Scalable, Poolable{
	public float lifetime;
	public float time;
	
	@Override
	public void update(){
		time += Timers.delta();

		time = Mathf.clamp(time, 0, lifetime);
		
		if(time >= lifetime) remove();
	}

	@Override
	public float fin() {
		return time/lifetime;
	}

	@Override
	public void reset() {
		time = 0f;
		lifetime = 0f;
	}
}
