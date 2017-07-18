package io.anuke.ucore.ecs.extend.traits;

import io.anuke.ucore.ecs.Prototype;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.ecs.extend.Events.Damaged;
import io.anuke.ucore.ecs.extend.Events.Death;
import io.anuke.ucore.util.Mathf;

public class HealthTrait extends Trait{
	public int health, maxhealth;
	public boolean dead = false;
	
	public HealthTrait(){
		this(100);
	}
	
	public HealthTrait(int maxhealth){
		health = maxhealth;
		this.maxhealth = maxhealth;
	}
	
	@Override
	public void registerEvents(Prototype type){
		type.traitEvent(Damaged.class, (spark, source, damage)->{
			HealthTrait htrait = spark.get(HealthTrait.class);
			
			htrait.health -= damage;
			
			if(htrait.health < 0 && !dead){
				type.callEvent(Death.class, spark);
				dead = true;
			}
		});
	}
	
	public void heal(){
		health = maxhealth;
	}
	
	public void clampHealth(){
		health = Mathf.clamp(health, 0, maxhealth);
	}
	
	/**Returns health/maxhealth.*/
	public float healthfrac(){
		return (float)health/maxhealth;
	}
}
