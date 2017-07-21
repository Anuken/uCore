package io.anuke.ucore.ecs.extend.traits;

import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.ecs.Require;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.util.Mathf;

@Require(PosTrait.class)
public class VelocityTrait extends Trait{
	public Vector2 vector = new Vector2();
	public float drag;
	
	public VelocityTrait(){}
	
	public VelocityTrait(float drag){
		this.drag = drag;
	}
	
	@Override
	public void update(Spark spark){
		PosTrait pos = spark.pos();
		pos.x += vector.x*Mathf.delta();
		pos.y += vector.y*Mathf.delta();
		vector.scl(1f-drag*Mathf.delta());
	}
	
	public float angle(){
		return vector.angle();
	}
}
