package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.core.Timers;

public abstract class BulletEntity extends SolidEntity implements Damager{
	public BaseBulletType type;
	public Entity owner;
	public Vector2 velocity = new Vector2();
	public float time = 0f;
	/**-1 to use type's damage.*/
	public int damage = -1;
	
	public BulletEntity(){}
	
	public BulletEntity(BaseBulletType type, Entity owner, float angle){
		this.type = type;
		this.owner = owner;
		
		velocity.set(0, type.speed).setAngle(angle);
		hitbox.setSize(4f);
	}
	
	@Override
	public void update(){
		type.update(this);
		
		x += velocity.x*Timers.delta();
		y += velocity.y*Timers.delta();
		
		velocity.scl(1f - type.drag * Timers.delta());
		
		time += Timers.delta();
		
		
		if(time >= type.lifetime){
			remove();
			type.despawned(this);
			//TODO change this
			//type.removed(this);
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
	public int getDamage(){
		return damage == -1 ? type.damage : damage;
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
	
	public float fract(){
		return 1f-time/type.lifetime;
	}
	
	public float ifract(){
		return time/type.lifetime;
	}
	
	public float sfract(){
		return (0.5f-Math.abs(time/type.lifetime-0.5f))*2f;
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
