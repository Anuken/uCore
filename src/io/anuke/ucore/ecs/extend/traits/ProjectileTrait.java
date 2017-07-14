package io.anuke.ucore.ecs.extend.traits;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.ecs.*;
import io.anuke.ucore.ecs.extend.Events.Collision;

@Require({VelocityTrait.class, PosTrait.class, ColliderTrait.class, ContactDamageTrait.class})
public class ProjectileTrait extends Trait{
	public ProjectileType type;
	public float life;
	
	@Override
	public void registerEvents(Prototype type){
		type.traitEvent(Collision.class, (a, b)->{
			a.remove();
			a.get(ProjectileTrait.class).type.removed(a);
		});
	}
	
	@Override
	public void update(Spark spark){
		type.update(spark);
	}
	
	public static abstract class ProjectileType{
		public float lifetime = 100;
		public float speed = 1f;
		public int damage = 1;
		public float hitsize = 4;
		protected Vector2 vector = new Vector2();
		
		public abstract void draw(Spark spark);
		
		public void update(Spark spark){}
		public void removed(Spark spark){}
		public void despawned(Spark spark){}
	}
}
