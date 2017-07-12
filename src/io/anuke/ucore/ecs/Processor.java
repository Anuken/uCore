package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;

public abstract class Processor{
	private boolean enabled = true;
	
	public abstract void update(Array<Spark> sparks);
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
