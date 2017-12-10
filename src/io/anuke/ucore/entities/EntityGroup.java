package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.util.QuadTree;

public class EntityGroup<T extends Entity>{
	private Array<T> entityArray = new Array<>();
	private Array<T> entitiesToRemove = new Array<>();
	private Array<T> entitiesToAdd = new Array<>();
	private QuadTree<SolidEntity> tree;
	
	public  final boolean useTree;
	
	public EntityGroup(boolean useTree){
		this.useTree = useTree;
	}
	
	public void updateRemovals(){

		for(T e : entitiesToRemove){
			entityArray.removeValue(e, true);
		}
		
		entitiesToRemove.clear();

		for(T e : entitiesToAdd){
			if(e == null)
				continue;
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
	
	public void add(T type){
		if(type == null) throw new RuntimeException("Cannot add a null entity!");
		if(type.group != null) return; //throw new RuntimeException("Entities cannot be added twice!");
		type.group = this;
		entitiesToAdd.add(type);
	}
	
	public void remove(T type){
		if(type == null) throw new RuntimeException("Cannot remove a null entity!");
		type.group = null;
		entitiesToRemove.add(type);
	}
	
	public void clear(){
		for(Entity entity : entityArray)
			entity.group = null;
		
		for(Entity entity : entitiesToAdd)
			entity.group = null;
		
		for(Entity entity : entitiesToRemove)
			entity.group = null;
		
		entitiesToAdd.clear();
		entitiesToRemove.clear();
		entityArray.clear();
	}
	
	public Array<T> all(){
		return entityArray;
	}
}
