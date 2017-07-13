package io.anuke.ucore.ecs.extend;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.util.Mathf;

public class PosTrait extends Trait{
	public float x, y;
	
	public PosTrait set(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public PosTrait set(PosTrait other){
		return set(other.x, other.y);
	}
	
	public float dst(float x, float y){
		return Vector2.dst(this.x, this.y, x, y);
	}
	
	public float dst(PosTrait pos){
		return dst(pos.x, pos.y);
	}
	
	public float angleTo(float x, float y){
		return Mathf.atan2(this.x-x, this.y-y);
	}
	
	public void translate(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public void translate(Vector2 v){
		translate(v.x, v.y);
	}

}
