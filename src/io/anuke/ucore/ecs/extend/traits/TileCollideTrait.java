package io.anuke.ucore.ecs.extend.traits;

import io.anuke.ucore.ecs.Require;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.ecs.extend.Events.TileCollision;
import io.anuke.ucore.ecs.extend.processors.TileCollisionProcessor;

//TODO
@Require({PosTrait.class})
public class TileCollideTrait extends Trait{
	public float width = 4, height = 4, offsetx, offsety;
	
	public TileCollideTrait(){
		
	}
	
	public TileCollideTrait(float offsetx, float offsety, float w, float h){
		this.offsetx = offsetx;
		this.offsety = offsety;
		this.width = w;
		this.height = h;
	}
	
	@Override
	public void update(Spark spark){
		TileCollisionProcessor p = spark.getBasis().getProcessor(TileCollisionProcessor.class);
		if(p.collides(spark, this)){
			spark.getType().callEvent(TileCollision.class, spark);
		}
	}
	
	public void move(Spark spark, float x, float y){
		TileCollisionProcessor p = spark.getBasis().getProcessor(TileCollisionProcessor.class);
		p.move(spark, this, x, y);
	}
}
