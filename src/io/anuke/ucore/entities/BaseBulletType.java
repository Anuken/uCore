package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;

/**Presumably you would extends BulletType and put in the default bullet entity type.*/
public abstract class BaseBulletType<T extends BulletEntity>{
	public float lifetime = 100;
	public float speed = 1f;
	public float damage = 1;
	public float hitsize = 4;
	public float drawSize = 20f;
	public float drag = 0f;
	public boolean pierce;
	public Effect hiteffect = null, despawneffect = null;
	
	protected Vector2 vector = new Vector2();

	public BaseBulletType(){

	}
	
	public void draw(T b){}
	public void init(T b){}
	public void update(T b){}

	public void hit(T b, float hitx, float hity){
		if(hiteffect != null)
			Effects.effect(hiteffect, b.x, b.y, b.angle());
	}

	public void hit(T b){
		hit(b, b.x, b.y);
	}

	public void despawned(T b){
		if(despawneffect != null)
			Effects.effect(despawneffect, b.x, b.y, b.angle());
	}
}
