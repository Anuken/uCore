package io.anuke.ucore.entities;

public abstract class DestructibleEntity extends SolidEntity{
	public int health;
	public int maxhealth;
	
	void onHit(SolidEntity entity){}
	void onDeath(){}
	
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
