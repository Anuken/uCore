package io.anuke.ucore.entities.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.entities.component.DamageTrait;
import io.anuke.ucore.entities.component.DrawTrait;
import io.anuke.ucore.entities.component.Entity;
import io.anuke.ucore.entities.component.VelocityTrait;
import io.anuke.ucore.entities.component.SolidTrait;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Scalable;

public abstract class BulletEntity<T extends BaseBulletType> extends SolidEntity implements DamageTrait, Scalable, Poolable, DrawTrait, VelocityTrait {
	protected T type;
	protected Entity owner;
	protected Vector2 velocity = new Vector2();
	protected float time = 0f;
	
	public BulletEntity(){}
	
	public BulletEntity(T type, Entity owner, float angle){
		this.type = type;
		this.owner = owner;
		
		velocity.set(0, type.speed).setAngle(angle);
		hitbox.setSize(type.hitsize);
	}

	@Override
	public void update(){
		type.update(this);
		
		x += velocity.x*Timers.delta();
		y += velocity.y*Timers.delta();
		
		velocity.scl(1f - type.drag * Timers.delta());
		
		time += Timers.delta();

		time = Mathf.clamp(time, 0, type.lifetime);
		
		if(time >= type.lifetime){
			type.despawned(this);
			remove();
		}
	}

	@Override
	public float drawSize() {
		return type.drawSize;
	}

	@Override
	public void added() {
		type.init(this);
	}

	@Override
	public float getDamage(){
		return type.damage;
	}
	
	@Override
	public void draw(){
		type.draw(this);
	}
	
	@Override
	public boolean collides(SolidTrait other){
		return other != owner && !(other instanceof DamageTrait);
	}
	
	@Override
	public void collision(SolidTrait other, float x, float y){
		if(!type.pierce) remove();
		type.hit(this, x, y);
	}

	@Override
	public float fin() {
		return time/type.lifetime;
	}

	@Override
	public Vector2 getVelocity() {
		return velocity;
	}

	@Override
	public void reset() {
		type = null;
		owner = null;
		velocity.setZero();
		time = 0f;
		lastPosition().set(Float.NaN, Float.NaN);
	}

	public void setVelocity(float speed, float angle){
		velocity.set(0, speed).setAngle(angle);
	}
	
	public void limit(float f){
		velocity.limit(f);
	}
	
	public void setAngle(float angle){
		velocity.setAngle(angle);
	}
	
	public float angle(){
		return velocity.angle();
	}
}
