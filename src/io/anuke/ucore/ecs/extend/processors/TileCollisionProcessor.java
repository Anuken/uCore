package io.anuke.ucore.ecs.extend.processors;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.anuke.ucore.ecs.Processor;
import io.anuke.ucore.ecs.Spark;
import io.anuke.ucore.ecs.extend.traits.TileCollideTrait;
import io.anuke.ucore.function.TileCollider;
import io.anuke.ucore.function.TileHitboxProvider;
import io.anuke.ucore.jbump.JBCollision.CollisionFilter;
import io.anuke.ucore.jbump.JBResponse.Result;
import io.anuke.ucore.jbump.JBWorld;
import io.anuke.ucore.jbump.JBWorld.JBItem;
import io.anuke.ucore.util.Mathf;

public class TileCollisionProcessor extends Processor{
	private static final int r = 2;
	
	private JBWorld world = new JBWorld();
	
	private Array<JBItem> items = new Array<>();
	private Rectangle tmp = new Rectangle();
	private Rectangle tmp2 = new Rectangle();
	
	private float tilesize;
	private TileCollider collider;
	private TileHitboxProvider hitbox;
	private GridPoint2 point = new GridPoint2();
	private Vector2 v = new Vector2();
	
	public TileCollisionProcessor(float tilesize, TileCollider collider, TileHitboxProvider hitbox){
		this.tilesize = tilesize;
		this.collider = collider;
		this.hitbox = hitbox;
	}
	
	public TileCollisionProcessor(float tilesize, TileCollider collider){
		this(tilesize, collider, (x, y, out) -> {
			out.setSize(tilesize).setCenter(x*tilesize, y*tilesize);
		});
	}
	
	public void move(Spark spark, TileCollideTrait trait, float deltax, float deltay){
		items.clear();
		float x = spark.pos().x + trait.offsetx, y = spark.pos().y  + trait.offsety;
		
		//TODO offset x/y to be in bottom left corner if needed?
		
		JBItem is = new JBItem();
		items.add(is);
		world.add(is, x-trait.width/2, y-trait.height/2, trait.width, trait.height);
		
		int tilex = Mathf.scl2(x, tilesize), tiley = Mathf.scl2(y, tilesize);
		
		for(int dx = -r; dx <= r; dx++){
			for(int dy = -r; dy <= r; dy++){
				int wx = dx+tilex, wy = dy+tiley;
				if(collider.solid(wx, wy)){
					
					hitbox.getHitbox(wx, wy, tmp);
					JBItem tile = new JBItem();
					world.add(tile, tmp.x, tmp.y, tmp.width, tmp.height);
					items.add(tile);
				}
			}
		}
		
		Result result = world.move(is, x + deltax-trait.width/2, y + deltay-trait.height/2, CollisionFilter.defaultFilter);
		spark.pos().set(result.goalX - trait.offsetx+trait.width/2, result.goalY - trait.offsety+trait.height/2);
		
		for(JBItem item : items){
			world.remove(item);
		}
	}
	
	public GridPoint2 collides(Spark spark, TileCollideTrait trait){
		tmp.setSize(trait.width, trait.height);
		tmp.setCenter(spark.pos().x + trait.offsetx, spark.pos().y + trait.offsety);
		tmp.getCenter(v);
		
		//assumes tilesize is centered
		int tilex = Mathf.scl2(v.x, tilesize);
		int tiley = Mathf.scl2(v.y, tilesize);
		
		for(int dx = -r; dx <= r; dx++){
			for(int dy = -r; dy <= r; dy++){
				int wx = dx+tilex, wy = dy+tiley;
				if(collider.solid(wx, wy)){
					hitbox.getHitbox(wx, wy, tmp2);
					
					if(tmp2.overlaps(tmp)){
						return point.set(wx, wy);
					}
				}
			}
		}
		return null;
	}

	@Override
	public void update(Array<Spark> sparks){}

}
