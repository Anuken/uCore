package io.anuke.ucore.ecs.extend;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;

import io.anuke.ucore.ecs.Processor;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.extend.Events.Collision;
import io.anuke.ucore.ecs.extend.Events.CollisionFilter;
import io.anuke.ucore.function.BoundingBoxProvider;
import io.anuke.ucore.util.QuadTree;

public class CollisionProcessor extends Processor{
	private QuadTree<Spark> tree;
	private IntSet collided = new IntSet();
	private BoundingBoxProvider<Spark> bounds = (obj, out)->{
		ColliderTrait col = obj.get(ColliderTrait.class);
		out.setSize(col.width, col.height);
		out.setCenter(obj.pos().x, obj.pos().y);
	};
	
	public CollisionProcessor(){
		resizeTree(0, 0, 10, 10);
	}
	
	@Override
	public void update(Array<Spark> sparks){
		collided.clear();
		tree.clear();
		
		for(int i = 0; i < sparks.size; i ++){
			if(sparks.get(i).has(ColliderTrait.class))
				tree.insert(sparks.get(i));
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
				
				bounds.getBoundingBox(other, Rectangle.tmp2);
				if(Rectangle.tmp.overlaps(Rectangle.tmp2) &&
						other.getType().callEvent(true, CollisionFilter.class, other, spark) &&
						spark.getType().callEvent(true, CollisionFilter.class, spark, other)){
					spark.getType().callEvent(Collision.class, spark, other);
					other.getType().callEvent(Collision.class, other, spark);
				}
			}, Rectangle.tmp);
			
			collided.add(spark.getID());
		}
	}
	
	
	public void resizeTree(float x, float y, float w, float h){
		tree = new QuadTree<Spark>(5, new Rectangle(x, y, w, h));
		
		tree.setBoundingBoxProvider((obj, out)->{
			ColliderTrait col = obj.get(ColliderTrait.class);
			out.setSize(col.width, col.height);
			out.setCenter(obj.pos().x, obj.pos().y);
		});
	}
}
