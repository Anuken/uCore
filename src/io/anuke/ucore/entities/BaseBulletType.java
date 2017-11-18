package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;

/**presumably you would extends BulletType and put in default bullet entity type.*/
public abstract class BaseBulletType<T extends BulletEntity>{
	public float lifetime = 100;
	public float speed = 1f;
	public int damage = 1;
	public float hitsize = 4;
	public Effect hiteffect = null, despawneffect = null;
	
	protected Vector2 vector = new Vector2();
	
	public abstract void draw(T b);
	public void update(T b){}
	public void removed(T b){
		if(hiteffect != null)
			Effects.effect(hiteffect, b);
	}
	public void despawned(T b){
		if(despawneffect != null)
			Effects.effect(despawneffect, b);
	}
}
