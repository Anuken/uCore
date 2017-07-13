package io.anuke.ucore.ecs.extend;

import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.SparkEvent;

public class Events{
	
	/**Collision event.*/
	public interface Collision extends SparkEvent{
		public void handle(Spark a, Spark b);
	}
	
	/**Collision filter. Should two sparks collide?*/
	public interface CollisionFilter extends SparkEvent{
		public boolean handle(Spark a, Spark b);
	}
}
