package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class BulletEntity extends SolidEntity implements Damager{
	public Entity owner;
	public Vector2 velocity = new Vector2();
	public float drag = 0f;
	
	public BulletEntity(){}
	
	public BulletEntity(Entity owner, float speed, float angle){
		velocity.set(0, speed).setAngle(angle);
		this.owner = owner;
	}
	
	@Override
	public void update(){
		x += velocity.x*delta;
		y += velocity.y*delta;
		
		velocity.scl(1f - drag * delta);
	}
	
	@Override
	public boolean collides(SolidEntity other){
		return other != owner && !(other instanceof Damager);
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
