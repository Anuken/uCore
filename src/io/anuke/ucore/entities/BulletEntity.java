package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class BulletEntity extends SolidEntity implements Damager{
	public BaseBulletType type;
	public Entity owner;
	public Vector2 velocity = new Vector2();
	public float time = 0f;
	public float drag = 0f;
	
	public BulletEntity(){}
	
	public BulletEntity(BaseBulletType type, Entity owner, float angle){
		this.type = type;
		this.owner = owner;
		
		velocity.set(0, type.speed).setAngle(angle);
		hitsize = 4;
	}
	
	@Override
	public void update(){
		type.update(this);
		
		x += velocity.x*delta;
		y += velocity.y*delta;
		
		velocity.scl(1f - drag * delta);
		
		time += delta;
		
		
		if(time >= type.lifetime){
			remove();
			//TODO change this
			//type.removed(this);
		}
	}
	
	@Override
	public int getDamage(){
		return type.damage;
	}
	
	@Override
	public void draw(){
		type.draw(this);
	}
	
	@Override
	public boolean collides(SolidEntity other){
		return other != owner && !(other instanceof Damager);
	}
	
	@Override
	public void collision(SolidEntity other){
		remove();
		type.removed(this);
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
