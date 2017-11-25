package io.anuke.ucore.entities;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.ucore.util.QuadTree;

public class EntityGroup<T extends Entity>{
	private HashMap<Integer, T> entities = new HashMap<>();
	private Array<T> entityArray = new Array<>();
	private ObjectSet<T> entitiesToRemove = new ObjectSet<>();
	private ObjectSet<T> entitiesToAdd = new ObjectSet<>();
	private QuadTree<SolidEntity> tree;
	
	public  final boolean useTree;
	
	public EntityGroup(boolean useTree){
		this.useTree = useTree;
	}
	
	public void updateRemovals(){

		for(T e : entitiesToRemove){
			entities.remove(e.id);
			entityArray.removeValue(e, true);
		}
		
		entitiesToRemove.clear();

		for(T e : entitiesToAdd){
			if(e == null)
				continue;
			entities.put(e.id, e);
			entityArray.add(e);
			e.added();
		}
		entitiesToAdd.clear();
	}
	
	public QuadTree<SolidEntity> tree(){
		return tree;
	}
	
	public void setTree(float x, float y, float w, float h){
		tree = new QuadTree(Entities.maxLeafObjects, new Rectangle(x, y, w, h));
	}
	
	public int amount(){
		return entityArray.size;
	}
	
	public T get(int id){
		return entities.get(id);
	}
	
	public void add(T type){
		entitiesToAdd.add(type);
	}
	
	public void remove(T type){
		entitiesToRemove.add(type);
	}
	
	public void clear(){
		entitiesToAdd.clear();
		entities.clear();
		entitiesToRemove.clear();
		entityArray.clear();
	}
	
	public Iterable<T> all(){
		return entityArray;
	}
}
