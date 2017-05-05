package io.anuke.ucore.entities;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.QuadTree;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public class Entities{
	private static HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
	protected static ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	protected static ObjectSet<Entity> entitiesToAdd = new ObjectSet<Entity>();
	
	public static QuadTree<SolidEntity> tree;
	public static boolean physics = false;
	public static TileCollider collider;
	public static float tilesize;
	
	private static IntSet collided = new IntSet();
	private static Array<SolidEntity> array = new Array<>();
	private static Rectangle viewport = new Rectangle();
	private static final int maxObjects = 4;
	
	public static void setCollider(float atilesize, TileCollider acollider){
		tilesize = atilesize;
		collider = acollider;
	}
	
	public static void initPhysics(float x, float y, float w, float h){
		tree = new QuadTree(maxObjects, new Rectangle(x, y, w, h));
		physics = true;
	}
	
	public static void initPhysics(){
		tree = new QuadTree(maxObjects, new Rectangle(0, 0, 0, 0));
		physics = true;
	}
	
	public static void resizeTree(float x, float y, float w, float h){
		initPhysics(x, y, w, h);
	}
	
	public static void resizeTree(float w, float h){
		initPhysics(0, 0, w, h);
	}
	
	static void move(Entity e, float hitsize, float dx, float dy){
		if(collider == null) throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");
		
		Rectangle.tmp.setSize(hitsize).setCenter(e.x, e.y);

		if(!overlapsTile(Rectangle.tmp, e.x + dx, e.y)){
			e.x += dx;
		}

		if(!overlapsTile(Rectangle.tmp, e.x, e.y + dy)){
			e.y += dy;
		}
	}
	
	static boolean overlapsTile(Rectangle rect, float x, float y){
		int r = 1;
		rect.setCenter(x, y);
		//assumes tilesize is centered
		int tilex = Mathf.scl2(x, tilesize);
		int tiley = Mathf.scl2(y, tilesize);

		for(int dx = -r; dx <= r; dx++){
			for(int dy = -r; dy <= r; dy++){
				int wx = dx+tilex, wy = dy+tiley;
				if(collider.solid(wx, wy) &&
						Rectangle.tmp2.setSize(tilesize)
						.setCenter(wx*tilesize, wy*tilesize).overlaps(rect)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static void getNearby(Rectangle rect, Consumer<SolidEntity> out){
		tree.getMaybeIntersecting(out, rect);
	}
	
	public static void getNearby(float x, float y, float size, Consumer<SolidEntity> out){
		tree.getMaybeIntersecting(out, Rectangle.tmp.setSize(size).setCenter(x, y));
	}
	
	public static Array<SolidEntity> getNearby(float x, float y, float size){
		array.clear();
		tree.getMaybeIntersecting(array, Rectangle.tmp2.setSize(size).setCenter(x, y));
		return array;
	}
	
	public static SolidEntity getClosest(float x, float y, float range, Predicate<Entity> pred){
		SolidEntity closest = null;
		float cdist = 0f;
		for(SolidEntity e : getNearby(x, y, range*2f)){
			if(!pred.test(e)) continue;
			
			float dist = Vector2.dst(e.x, e.y, x, y);
			if(dist < range)
			if(closest == null || dist < cdist){
				closest = e;
				cdist = dist;
			}
		}
		
		return closest;
	}
	
	public static void clear(){
		entitiesToAdd.clear();
		entities.clear();
		entitiesToRemove.clear();
	}
	
	public static Iterable<Entity> all(){
		return entities.values();
	}
	
	public static Entity get(long id){
		return entities.get(id);
	}
	
	private static void updatePhysics(){
		collided.clear();
		
		tree.clear();
		
		for(Entity entity : all()){
			if(entity instanceof SolidEntity)
			tree.insert((SolidEntity)entity);
		}
		
		for(Entity entity : all()){
			if(!(entity instanceof SolidEntity)) continue;
			if(collided.contains((int)entity.id)) continue;
				
			((QuadTreeObject)entity).getBoundingBox(Rectangle.tmp2);
			
			tree.getMaybeIntersecting(c->{
				if(!collided.contains((int)c.id))
						checkCollide(entity, c);
			}, Rectangle.tmp2);
			
			collided.add((int)entity.id);
		}
	}
	
	private static boolean checkCollide(Entity entity, Entity other){
		SolidEntity a = (SolidEntity) entity;
		SolidEntity b = (SolidEntity) other;
		
		if(a != b && a.collides(b) 
				&& b.collides(a)
				 && Mathf.intersect(entity.x, entity.y, a.hitsize/2, other.x, other.y, b.hitsize/2)){
			a.collision(b);
			b.collision(a);
			return true;
		}
		
		return false;
	}
	
	public static void updateAll(){
		update();
		draw();
	}
	
	public static void update(){
		update(true);
	}
	
	public static void draw(){
		OrthographicCamera cam = DrawContext.camera;
		viewport.set(cam.position.x - cam.viewportWidth/2, cam.position.y - cam.viewportHeight/2, cam.viewportWidth, cam.viewportHeight);
		
		for(Entity e : entities.values()){
			Rectangle.tmp2.setSize(e.drawSize()).setCenter(e.x, e.y);
			
			if(Rectangle.tmp2.overlaps(viewport))
				e.draw();
		}
		
		for(Entity e : entities.values()){
			Rectangle.tmp2.setSize(e.drawSize()).setCenter(e.x, e.y);
			
			if(Rectangle.tmp2.overlaps(viewport))
				e.drawOver();
		}
	}
	
	public static void update(boolean callupdate){
		Entity.delta = Gdx.graphics.getDeltaTime() * 60f;
		
		if(physics)
			updatePhysics();
		
		if(callupdate)
		for(Entity e : entities.values()){
			e.update();
		}

		for(Long l : entitiesToRemove){
			entities.remove(l);
			
		}
		entitiesToRemove.clear();

		for(Entity e : entitiesToAdd){
			entities.put(e.id, e);
			e.added();
		}
		entitiesToAdd.clear();
	}
}
