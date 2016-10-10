package io.anuke.ucore.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class EntityController{
	private static EntityController instance;
	public ObjectMap<Long, Entity> entities = new ObjectMap<Long, Entity>();
	public ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	public ObjectSet<Entity> entitiesToAdd = new ObjectSet<Entity>();
	
	private EntityController(){
		instance = this;
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
	
	public static EntityController instance(){
		if(instance == null) instance = new EntityController();
		return instance;
	}
}
