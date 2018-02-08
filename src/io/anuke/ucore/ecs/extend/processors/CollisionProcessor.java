package io.anuke.ucore.ecs.extend.processors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;

import io.anuke.ucore.ecs.Processor;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.extend.Events.Collision;
import io.anuke.ucore.ecs.extend.Events.CollisionFilter;
import io.anuke.ucore.ecs.extend.traits.ColliderTrait;
import io.anuke.ucore.function.BoundingBoxProvider;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.Predicate;
import io.anuke.ucore.util.QuadTree;

public class CollisionProcessor extends Processor{
	private QuadTree<Spark> tree;
	private Array<Spark> array = new Array<>();
	private IntSet collided = new IntSet();
	private Rectangle tmp2 = new Rectangle();
	private BoundingBoxProvider<Spark> bounds = (obj, out)->{
		ColliderTrait col = obj.get(ColliderTrait.class);
		out.setSize(col.width, col.height);
		out.setCenter(obj.pos().x + col.offsetx, obj.pos().y + col.offsety);
	};
	
	public CollisionProcessor(){
		//resizeTree(0, 0, 10, 10);
	}
	
	@Override
	public void update(Array<Spark> sparks){
		collided.clear();
		tree.clear();
		
		for(int i = 0; i < sparks.size; i ++){
			if(sparks.get(i).has(ColliderTrait.class)){
				tree.insert(sparks.get(i));
			}
		}
		
		for(int i = 0; i < sparks.size; i ++){
			Spark spark = sparks.get(i);
			
			//prevent duplicate collisions
			if(!spark.has(ColliderTrait.class) || collided.contains(spark.getID())){
				continue;
			}
			
			bounds.getBoundingBox(spark, Rectangle.tmp);
			
			tree.getIntersect(other -> {
				if(!other.has(ColliderTrait.class) || other == spark)
					return;
				
				bounds.getBoundingBox(other, tmp2);
				
				if(Rectangle.tmp.overlaps(tmp2) &&
						other.getType().callEvent(true, CollisionFilter.class, other, spark) &&
						spark.getType().callEvent(true, CollisionFilter.class, spark, other)){
					spark.getType().callEvent(Collision.class, spark, other);
					other.getType().callEvent(Collision.class, other, spark);
				}
			}, Rectangle.tmp);
			
			collided.add(spark.getID());
		}
	}
	
	public void getNearby(Rectangle rect, Consumer<Spark> out){
		tree.getIntersect(out, rect);
	}
	
	public Array<Spark> getNearby(Rectangle rect){
		array.clear();
		tree.getIntersect(array, rect);
		return array;
	}
	
	public void getNearby(float x, float y, float size, Consumer<Spark> out){
		tree.getIntersect(out, Rectangle.tmp.setSize(size).setCenter(x, y));
	}
	
	public Array<Spark> getNearby(float x, float y, float size){
		array.clear();
		tree.getIntersect(array, tmp2.setSize(size).setCenter(x, y));
		return array;
	}
	
	public Spark getClosest(float x, float y, float range, Predicate<Spark> pred){
		Spark closest = null;
		float cdist = 0f;
		for(Spark e : getNearby(x, y, range*2f)){
			if(!pred.test(e)) continue;
			
			float dist = Vector2.dst(e.pos().x, e.pos().y, x, y);
			if(dist < range)
			if(closest == null || dist < cdist){
				closest = e;
				cdist = dist;
			}
		}
		
		return closest;
	}
	
	public void resizeTree(float x, float y, float w, float h){
		tree = new QuadTree<Spark>(5, new Rectangle(x, y, w, h));
		
		tree.setBoundingBoxProvider(bounds);
	}
}
