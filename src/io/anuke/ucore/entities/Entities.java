package io.anuke.ucore.entities;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.Predicate;
import io.anuke.ucore.function.TileCollider;
import io.anuke.ucore.util.*;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public class Entities{
	private static HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
	protected static ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	protected static ObjectSet<Entity> entitiesToAdd = new ObjectSet<Entity>();
	
	public static QuadTree<SolidEntity> tree;
	public static RectQuadTree rtree;
	public static boolean physics = false;
	public static TileCollider collider;
	public static float tilesize;
	
	private static Vector2 vector = new Vector2();
	private static IntSet collided = new IntSet();
	private static Array<SolidEntity> array = new Array<>();
	private static Array<Rectangle> rectarray = new Array<>();
	private static Array<Rectangle> tmprects = new Array<>();
	private static Rectangle viewport = new Rectangle();
	private static final int maxObjects = 4;
	
	public static void setCollider(float atilesize, TileCollider acollider){
		tilesize = atilesize;
		collider = acollider;
	}
	
	public static void initPhysics(float x, float y, float w, float h){
		tree = new QuadTree(maxObjects, new Rectangle(x, y, w, h));
		rtree = new RectQuadTree(maxObjects, new Rectangle(x, y, w, h));
		physics = true;
		
		updateRects();
	}
	
	public static void initPhysics(){
		initPhysics(0, 0, 0, 0);
	}
	
	public static void resizeTree(float x, float y, float w, float h){
		initPhysics(x, y, w, h);
	}
	
	public static void resizeTree(float w, float h){
		initPhysics(0, 0, w, h);
	}
	
	static void moveTiled(Entity e, float hitw, float hith, float dx, float dy){
		if(collider == null) throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");
		
		Rectangle rect = Rectangle.tmp;
		
		rect.setSize(hitw, hith);

		overlapTile(rect, e.x , e.y + dy);
		rect.getCenter(vector);
		overlapTile(rect, vector.x + dx, vector.y);
		
		e.x = rect.x+hitw/2;
		e.y = rect.y+hith/2;
	}
	
	static void overlapTile(Rectangle rect, float x, float y){
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
					
					Vector2 out = Physics.overlap(rect, Rectangle.tmp2);
					rect.x += out.x*3f;
					rect.y += out.y*3f;
				}
			}
		}
	}
	
	static boolean overlapsTile(Rectangle rect){
		rect.getCenter(vector);
		int r = 1;
		
		//assumes tilesize is centered
		int tilex = Mathf.scl2(vector.x, tilesize);
		int tiley = Mathf.scl2(vector.y, tilesize);
		
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
		tree.getIntersect(out, rect);
	}
	
	public static void getNearby(float x, float y, float size, Consumer<SolidEntity> out){
		tree.getIntersect(out, Rectangle.tmp.setSize(size).setCenter(x, y));
	}
	
	public static Array<SolidEntity> getNearby(float x, float y, float size){
		array.clear();
		tree.getIntersect(array, Rectangle.tmp2.setSize(size).setCenter(x, y));
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
	
	public static int amount(){
		return entities.size();
	}
	
	public static Entity get(long id){
		return entities.get(id);
	}
	
	public static Array<Rectangle> getRects(){
		return rectarray;
	}
	
	public static void addRect(Rectangle rect){
		rectarray.add(rect);
		rtree.insert(rect);
	}
	
	public static void removeRect(Rectangle rect){
		rectarray.removeValue(rect, true);
		
		updateRects();
	}
	
	public static void updateRects(){
		rtree.clear();
		
		for(Rectangle rect : rectarray){
			rtree.insert(rect);
		}
	}
	
	public static void moveRect(Entity e, float hitwidth, float hitheight, float offsetx, float offsety, float dx, float dy){
		Rectangle.tmp.setSize(hitwidth, hitheight).setCenter(e.x+offsetx+dx, e.y+offsety+dy);

		Rectangle.tmp2.setSize(hitwidth*2+Math.abs(offsetx), hitheight*2+Math.abs(offsety));
		Rectangle.tmp2.setCenter(e.x+offsetx, e.y+offsety);
		
		tmprects.clear();
		
		rtree.getIntersect(tmprects, Rectangle.tmp2);
		
		for(Rectangle rect : tmprects){
			if(rect.overlaps(Rectangle.tmp)){
				Vector2 vector = Physics.overlap(Rectangle.tmp, rect);
				Rectangle.tmp.x += vector.x;
				Rectangle.tmp.y += vector.y;
			}
		}
		
		float movex = Rectangle.tmp.x+hitwidth/2-e.x-offsetx, movey = Rectangle.tmp.y+hitheight/2-e.y-offsety;
		
		e.x += movex;
		e.y += movey;
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
			
			tree.getIntersect(c->{
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
		viewport.set(cam.position.x - cam.viewportWidth/2*cam.zoom, cam.position.y - cam.viewportHeight/2*cam.zoom, cam.viewportWidth*cam.zoom, cam.viewportHeight*cam.zoom);
		
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
	
	public static void debugColliders(){
		Draw.color(Color.YELLOW);
		for(Entity e : entities.values()){
			if(e instanceof SolidEntity){
				SolidEntity s = (SolidEntity)e;
				s.getBoundingBox(Rectangle.tmp);
				Draw.linerect(Rectangle.tmp.x, Rectangle.tmp.y, Rectangle.tmp.width, Rectangle.tmp.height);
			}
		}
		Draw.color();
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
