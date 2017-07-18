package io.anuke.ucore.ecs.extend.traits;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.core.Effects;
import io.anuke.ucore.ecs.*;
import io.anuke.ucore.ecs.extend.Events.Collision;

@Require({VelocityTrait.class, PosTrait.class, ColliderTrait.class, ContactDamageTrait.class, LifetimeTrait.class})
public class ProjectileTrait extends Trait{
	public ProjectileType type;
	public Spark source;
	public boolean customDamage = false;
	
	@Override
	public void registerEvents(Prototype type){
		type.traitEvent(Collision.class, (a, b)->{
			a.remove();
			a.get(ProjectileTrait.class).type.removed(a);
		});
	}
	
	@Override
	public void added(Spark spark){
		if(type == null){
			throw new IllegalArgumentException("Projectile type is null! Set a value before adding the Spark to anything.");
		}
		if(!customDamage)
			spark.get(ContactDamageTrait.class).damage = type.damage;
		spark.get(VelocityTrait.class).vector.setLength(type.speed);
		spark.get(ColliderTrait.class).setSize(type.hitsize);
		spark.get(LifetimeTrait.class).lifetime = type.lifetime;
	}
	
	@Override
	public void update(Spark spark){
		type.update(spark);
	}
	
	@Override
	public void removed(Spark spark){
		type.despawned(spark);
	}
	
	public static abstract class ProjectileType{
		public float lifetime = 100;
		public float speed = 1f;
		public int damage = 1;
		public float hitsize = 4;
		public String hiteffect = null, despawneffect = null;
		protected Vector2 vector = new Vector2();
		
		public abstract void draw(Spark spark);
		
		//TODO create particle effect on death
		public void update(Spark spark){}
		
		public void removed(Spark spark){
			if(hiteffect != null)
				Effects.effect(hiteffect, spark);
		}
		
		public void despawned(Spark spark){
			if(despawneffect != null)
				Effects.effect(despawneffect, spark);
		}
	}
}
