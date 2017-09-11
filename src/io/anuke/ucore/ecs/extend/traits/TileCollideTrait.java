package io.anuke.ucore.ecs.extend.traits;

import com.badlogic.gdx.math.GridPoint2;

import io.anuke.ucore.ecs.Require;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.Trait;
import io.anuke.ucore.ecs.extend.Events.TileCollision;
import io.anuke.ucore.ecs.extend.processors.TileCollisionProcessor;

//TODO
@Require({PosTrait.class})
public class TileCollideTrait extends Trait{
	public float width = 4, height = 4, offsetx, offsety;
	public boolean trigger = false;
	
	public TileCollideTrait(){
		
	}
	
	public TileCollideTrait(float offsetx, float offsety, float w, float h){
		this.offsetx = offsetx;
		this.offsety = offsety;
		this.width = w;
		this.height = h;
	}
	
	public TileCollideTrait(float offsetx, float offsety, float w, float h, boolean trigger){
		this(offsetx, offsety, w, h);
		this.trigger = trigger;
	}
	
	@Override
	public void update(Spark spark){
		TileCollisionProcessor p = spark.getBasis().getProcessor(TileCollisionProcessor.class);
		if(p == null) throw new IllegalArgumentException("No TileCollisionProcessor in basis. Add one before using a TileCollideTrait.");
		
		GridPoint2 point = p.collides(spark, this);
		
		if(point != null){
			spark.getType().callEvent(TileCollision.class, spark, point.x, point.y);
		}
	}
	
	public void move(Spark spark, float x, float y){
		TileCollisionProcessor p = spark.getBasis().getProcessor(TileCollisionProcessor.class);
		if(p == null) throw new IllegalArgumentException("No TileCollisionProcessor in basis. Add one before using a TileCollideTrait.");
		
		if(!trigger){
			p.move(spark, this, x, y);
		}else{
			spark.pos().x += x;
			spark.pos().y += y;
		}
	}
}
