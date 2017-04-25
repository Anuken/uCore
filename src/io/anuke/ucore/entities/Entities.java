package io.anuke.ucore.entities;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.QuadTree;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public class Entities{
	private static Entities instance;
	
	public HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
	public ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	public ObjectSet<Entity> entitiesToAdd = new ObjectSet<Entity>();
	
	public QuadTree<PhysicsEntity> tree;
	public boolean physics = false;
	
	private IntSet collided = new IntSet();
	
	
	private Entities(){
		instance = this;
	}
	
	public void initPhysics(float x, float y, float w, float h){
		tree = new QuadTree(4, new Rectangle(x, y, w, h));
		this.physics = true;
	}
	
	public void resizeTree(float x, float y, float w, float h){
		initPhysics(x, y, w, h);
	}
	
	public void getNearby(Rectangle rect, Consumer<PhysicsEntity> out){
		tree.getMaybeIntersecting(out, rect);
	}
	
	public void getNearby(float x, float y, float size, Consumer<PhysicsEntity> out){
		tree.getMaybeIntersecting(out, Rectangle.tmp.setSize(size).setCenter(x, y));
	}
	
	public void clear(){
		entitiesToAdd.clear();
		entities.clear();
		entitiesToRemove.clear();
	}
	
	public Iterable<Entity> all(){
		return entities.values();
	}
	
	private void updatePhysics(){
		collided.clear();
		
		tree.clear();

		for(Entity entity : all()){
			if(entity instanceof PhysicsEntity)
			tree.insert((PhysicsEntity)entity);
		}
		
		for(Entity entity : all()){
			if(!(entity instanceof PhysicsEntity)) continue;
			if(collided.contains((int)entity.id)) continue;
				
			((QuadTreeObject)entity).getBoundingBox(Rectangle.tmp);
			
			tree.getMaybeIntersecting(c->{
				if(!collided.contains((int)c.id))
						checkCollide(entity, c);
			}, Rectangle.tmp);
			
			collided.add((int)entity.id);
		}
	}
	
	private boolean checkCollide(Entity entity, Entity other){
		PhysicsEntity a = (PhysicsEntity) entity;
		PhysicsEntity b = (PhysicsEntity) other;
		
		if(a.collides(b) 
				&& b.collides(a)
				 && Mathf.intersect(entity.x, entity.y, a.hitsize/2, other.x, other.y, b.hitsize/2)){
			a.collision(b);
			b.collision(a);
			return true;
		}
		
		return false;
	}
	
	public void update(){
		update(true);
	}
	
	public void update(boolean update){
		Entity.delta = Gdx.graphics.getDeltaTime() * 60f;
		
		if(physics)
			updatePhysics();
		
		if(update)
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
	
	public static Entities get(){
		if(instance == null) instance = new Entities();
		return instance;
	}
}
