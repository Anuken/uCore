package io.anuke.ucore.ecs.extend;

import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.SparkEvent;

public class Events{
	
	/**Collision event.*/
	public interface Collision extends SparkEvent{
		public void handle(Spark spark, Spark other);
	}
	
	/**Collision filter. Returns true if two sparks should collide.*/
	public interface CollisionFilter extends SparkEvent{
		public boolean handle(Spark spark, Spark other);
	}
	
	public interface Damaged extends SparkEvent{
		public void handle(Spark spark, Spark source, int damage);
	}
	
	public interface Death extends SparkEvent{
		public void handle(Spark spark);
	}
}
