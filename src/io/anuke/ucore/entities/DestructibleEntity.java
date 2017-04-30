package io.anuke.ucore.entities;

public abstract class DestructibleEntity extends SolidEntity{
	public int health;
	public int maxhealth;
	
	public void onHit(SolidEntity entity){}
	public void onDeath(){}
	
	@Override
	public boolean collides(SolidEntity other){
		return other instanceof Damager;
	}
	
	@Override
	public void collision(SolidEntity other){
		if(other instanceof Damager){
			health -= ((Damager)other).getDamage();
			onHit(other);
			
			if(health <= 0)
				onDeath();
		}
	}
	
	public void heal(){
		health = maxhealth;
	}
}
