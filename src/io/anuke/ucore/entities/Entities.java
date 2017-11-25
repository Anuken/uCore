package io.anuke.ucore.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.function.*;
import io.anuke.ucore.util.*;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public class Entities{
	private static ObjectMap<Class<? extends Entity>, EntityGroup<?>> groups = new ObjectMap<>();
	private static Array<EntityGroup<?>> groupArray = new Array<>();

	private static RectQuadTree rtree;
	private static boolean physics = false;
	private static TileCollider collider;
	private static TileHitboxProvider tileHitbox;
	private static float tilesize;

	private static Vector2 vector = new Vector2();
	private static IntSet collided = new IntSet();
	private static Array<SolidEntity> array = new Array<>();
	private static Array<Rectangle> rectarray = new Array<>();
	private static Array<Rectangle> tmprects = new Array<>();
	private static Rectangle viewport = new Rectangle();
	
	public static int maxLeafObjects = 4;
	
	static{
		addGroup(Entity.class);
	}

	public static void setCollider(float atilesize, TileCollider acollider, TileHitboxProvider hitbox){
		tilesize = atilesize;
		collider = acollider;
		tileHitbox = hitbox;
	}

	public static void setCollider(float atilesize, TileCollider acollider){
		setCollider(atilesize, acollider, (x, y, out) -> {
			out.setSize(tilesize).setCenter(x * tilesize, y * tilesize);
		});
	}

	public static void initPhysics(float x, float y, float w, float h){
		for(EntityGroup group : groupArray){
			if(group.useTree)
				group.setTree(x, y, w, h);
		}
		rtree = new RectQuadTree(maxLeafObjects, new Rectangle(x, y, w, h));
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

	static void moveTiled(Entity e, Hitbox box, float dx, float dy){
		if(collider == null)
			throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

		Rectangle rect = Rectangle.tmp;

		float fx = box.offsetx, fy = box.offsety;

		rect.setSize(box.width, box.height);

		overlapTile(rect, e.x + fx, e.y + dy + fy);
		rect.getCenter(vector);
		overlapTile(rect, vector.x + dx + fx, vector.y + fy);
		rect.getCenter(vector);
		overlapTile(rect, e.x + fx, e.y + dy + fy);
		rect.getCenter(vector);
		overlapTile(rect, vector.x + dx + fx, vector.y + fy);

		e.x = rect.x + box.width / 2 + box.offsetx;
		e.y = rect.y + box.height / 2 + box.offsety;
	}

	static void overlapTile(Rectangle rect, float x, float y){
		int r = 1;
		rect.setCenter(x, y);

		float scl = 2f;

		//assumes tilesize is centered
		int tilex = Mathf.scl2(x, tilesize);
		int tiley = Mathf.scl2(y, tilesize);

		for(int dx = -r; dx <= r; dx++){
			for(int dy = -r; dy <= r; dy++){
				int wx = dx + tilex, wy = dy + tiley;
				if(collider.solid(wx, wy)){

					tileHitbox.getHitbox(wx, wy, Rectangle.tmp2);

					if(Rectangle.tmp2.overlaps(rect)){
						Vector2 out = Physics.overlap(rect, Rectangle.tmp2);
						rect.x += out.x * scl;
						rect.y += out.y * scl;
					}
				}
			}
		}
	}

	public static boolean overlapsTile(Rectangle rect){
		rect.getCenter(vector);
		int r = 1;

		//assumes tilesize is centered
		int tilex = Mathf.scl2(vector.x, tilesize);
		int tiley = Mathf.scl2(vector.y, tilesize);

		for(int dx = -r; dx <= r; dx++){
			for(int dy = -r; dy <= r; dy++){
				int wx = dx + tilex, wy = dy + tiley;
				if(collider.solid(wx, wy)){
					tileHitbox.getHitbox(wx, wy, Rectangle.tmp2);

					if(Rectangle.tmp2.overlaps(rect)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void getNearby(EntityGroup<?> group, Rectangle rect, Consumer<SolidEntity> out){
		if(!group.useTree) throw new RuntimeException("This group does not support quadtrees! Enable quadtrees when creating it.");
		group.tree().getIntersect(out, rect);
	}

	public static Array<SolidEntity> getNearby(EntityGroup<?> group, Rectangle rect){
		array.clear();
		getNearby(group, rect, entity -> array.add(entity));
		return array;
	}

	public static void getNearby(float x, float y, float size, Consumer<SolidEntity> out){
		getNearby(defaultGroup(), Rectangle.tmp.setSize(size).setCenter(x, y), out);
	}
	
	public static void getNearby(EntityGroup<?> group, float x, float y, float size, Consumer<SolidEntity> out){
		getNearby(group, Rectangle.tmp.setSize(size).setCenter(x, y), out);
	}
	
	public static Array<SolidEntity> getNearby(float x, float y, float size){
		return getNearby(defaultGroup(), Rectangle.tmp.setSize(size).setCenter(x, y));
	}

	public static Array<SolidEntity> getNearby(EntityGroup<?> group, float x, float y, float size){
		return getNearby(group, Rectangle.tmp.setSize(size).setCenter(x, y));
	}

	public static SolidEntity getClosest(EntityGroup<?> group, float x, float y, float range, Predicate<Entity> pred){
		SolidEntity closest = null;
		float cdist = 0f;
		for(SolidEntity e : getNearby(group, x, y, range * 2f)){
			if(!pred.test(e))
				continue;

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
		for(EntityGroup group : groupArray){
			group.clear();
		}
	}
	
	public static Iterable<Entity> get(){
		return get(Entity.class);
	}

	public static <T extends Entity> Iterable<T> get(Class<T> type){
		return getGroup(type).all();
	}

	public static <T extends Entity> T get(Class<T> type, int id){
		return getGroup(type).get(id);
	}
	
	public static Entity get(int id){
		return get(Entity.class, id);
	}
	
	public static EntityGroup<Entity> defaultGroup(){
		return (EntityGroup<Entity>) groups.get(Entity.class);
	}
	
	public static <T extends Entity> EntityGroup<T> getGroup(Class<T> type){
		return (EntityGroup<T>) groups.get(type);
	}
	
	public static <T extends Entity> EntityGroup<T> addGroup(Class<T> type){
		return addGroup(type, true);
	}
	
	public static <T extends Entity> EntityGroup<T> addGroup(Class<T> type, boolean useTree){
		EntityGroup<T> group = new EntityGroup<>(useTree);
		groups.put(type, group);
		groupArray.add(group);
		return group;
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
		Rectangle.tmp.setSize(hitwidth, hitheight).setCenter(e.x + offsetx + dx, e.y + offsety + dy);

		Rectangle.tmp2.setSize(hitwidth * 2 + Math.abs(offsetx), hitheight * 2 + Math.abs(offsety));
		Rectangle.tmp2.setCenter(e.x + offsetx, e.y + offsety);

		tmprects.clear();

		rtree.getIntersect(tmprects, Rectangle.tmp2);

		for(Rectangle rect : tmprects){
			if(rect.overlaps(Rectangle.tmp)){
				Vector2 vector = Physics.overlap(Rectangle.tmp, rect);
				Rectangle.tmp.x += vector.x;
				Rectangle.tmp.y += vector.y;
			}
		}

		float movex = Rectangle.tmp.x + hitwidth / 2 - e.x - offsetx, movey = Rectangle.tmp.y + hitheight / 2 - e.y - offsety;

		e.x += movex;
		e.y += movey;
	}
	
	public static void debugColliders(EntityGroup<?> group){
		Draw.color(Color.YELLOW);
		for(Entity e : group.all()){
			if(e instanceof SolidEntity){
				SolidEntity s = (SolidEntity) e;
				s.getBoundingBox(Rectangle.tmp);
				Draw.linerect(Rectangle.tmp.x, Rectangle.tmp.y, Rectangle.tmp.width, Rectangle.tmp.height);
			}
		}
		Draw.color();
	}

	private static void updatePhysics(EntityGroup<?> group){
		collided.clear();
		
		QuadTree<SolidEntity> tree = group.tree();
		
		tree.clear();

		for(Entity entity : group.all()){
			if(entity instanceof SolidEntity){
				tree.insert((SolidEntity) entity);
			}
		}
	}

	private static boolean checkCollide(Entity entity, Entity other){
		SolidEntity a = (SolidEntity) entity;
		SolidEntity b = (SolidEntity) other;
		
		Rectangle r1 = a.hitbox.getRect(Rectangle.tmp, a.x, a.y);
		Rectangle r2 = b.hitbox.getRect(Rectangle.tmp2, b.x, b.y);

		if(a != b && a.collides(b) && b.collides(a) && r1.overlaps(r2)){
			a.collision(b);
			b.collision(a);
			return true;
		}

		return false;
	}
	
	public static void collideGroups(EntityGroup<?> groupa, EntityGroup<?> groupb){
		collided.clear();
		
		for(Entity entity : groupa.all()){
			if(!(entity instanceof SolidEntity))
				continue;
			if(collided.contains(entity.id))
				continue;

			((QuadTreeObject) entity).getBoundingBox(Rectangle.tmp2);

			groupb.tree().getIntersect(c -> {
				if(!collided.contains((int) c.id))
					checkCollide(entity, c);
			}, Rectangle.tmp2);

			collided.add((int) entity.id);
		}
	}
	
	public static void draw(){
		draw(getGroup(Entity.class));
	}

	public static void draw(EntityGroup<?> group){
		OrthographicCamera cam = Core.camera;
		viewport.set(cam.position.x - cam.viewportWidth / 2 * cam.zoom, cam.position.y - cam.viewportHeight / 2 * cam.zoom, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);

		for(Entity e : group.all()){
			Rectangle.tmp2.setSize(e.drawSize()).setCenter(e.x, e.y);

			if(Rectangle.tmp2.overlaps(viewport))
				e.draw();
		}

		for(Entity e : group.all()){
			Rectangle.tmp2.setSize(e.drawSize()).setCenter(e.x, e.y);

			if(Rectangle.tmp2.overlaps(viewport))
				e.drawOver();
		}
	}
	
	public static void update(){
		update(defaultGroup());
		collideGroups(defaultGroup(), defaultGroup());
	}

	public static void update(EntityGroup<?> group){
		if(group.useTree){
			updatePhysics(group);
		}
		
		for(Entity e : group.all()){
			e.update();
		}
		
		group.updateRemovals();
	}
}
