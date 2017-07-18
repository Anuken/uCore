package io.anuke.ucore.ecs.extend.traits;

import io.anuke.ucore.ecs.Require;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
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
	
	public void move(Spark spark, float x, float y){
		TileCollisionProcessor p = spark.getBasis().getProcessor(TileCollisionProcessor.class);
		p.move(spark, this, x, y);
	}
}
