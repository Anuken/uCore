package io.anuke.ucore.ecs.extend;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.ecs.Processor;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.function.BoundingBoxProvider;
import io.anuke.ucore.util.QuadTree;

public class CollisionProcessor extends Processor{
	private QuadTree<Spark> tree;
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
		tree.clear();
		for(int i = 0; i < sparks.size; i ++){
			if(sparks.get(i).has(ColliderTrait.class))
				tree.insert(sparks.get(i));
		}
		
		for(int i = 0; i < sparks.size; i ++){
			Spark spark = sparks.get(i);
			
			if(!spark.has(ColliderTrait.class)){
				continue;
			}
			
			bounds.getBoundingBox(spark, Rectangle.tmp);
			
			tree.getIntersect(other -> {
				if(!other.has(ColliderTrait.class))
					return;
				
				bounds.getBoundingBox(other, Rectangle.tmp2);
				if(Rectangle.tmp.overlaps(Rectangle.tmp2)){
					//what now?
				}
			}, Rectangle.tmp);
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
