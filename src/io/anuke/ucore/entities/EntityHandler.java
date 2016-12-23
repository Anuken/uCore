package io.anuke.ucore.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class EntityHandler{
	private static EntityHandler instance;
	public ObjectMap<Long, Entity> entities = new ObjectMap<Long, Entity>();
	public ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	public ObjectSet<Entity> entitiesToAdd = new ObjectSet<Entity>();
	
	private EntityHandler(){
		instance = this;
	}
	
	public Iterable<Entity> getEntities(){
		return entities.values();
	}
	
	public void update(){
		Entity.delta = Gdx.graphics.getDeltaTime() * 60f;

		for(Entity e : entities.values()){
			e.update();
		}

		for(Long l : entitiesToRemove){
			entities.remove(l);
			
		}
		entitiesToRemove.clear();

		for(Entity e : entitiesToAdd){
			entities.put(e.id, e);
		}
		entitiesToAdd.clear();
	}
	
	public static EntityHandler instance(){
		if(instance == null) instance = new EntityHandler();
		return instance;
	}
}
