package io.anuke.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import io.anuke.ucore.entities.trait.Entity;
import io.anuke.ucore.function.Predicate;
import io.anuke.ucore.util.QuadTree;

import java.util.Iterator;

public class EntityGroup<T extends Entity>{
	private static int lastid;
	private final int id;

	private IntMap<T> map;
	private EntityContainer<T> entityArray = new ArrayContainer<>();
	private Array<T> entitiesToRemove = new Array<>();
	private Array<T> entitiesToAdd = new Array<>();
	private QuadTree<T> tree;
	private Class<T> type;
	
	public  final boolean useTree;
	
	public EntityGroup(Class<T> type, boolean useTree){
		this.useTree = useTree;
		this.id = lastid ++;
		this.type = type;
	}

	public EntityGroup<T> enableMapping(){
		map = new IntMap<>();
		return this;
	}

	public Class<T> getType(){
		return type;
	}

	public int getID(){
		return id;
	}

	public synchronized void updateEvents(){
		for(T e : entitiesToAdd){
			if(e == null)
				continue;
			entityArray.add(e);
			e.added();

			if(map != null){
				map.put(e.getID(), e);
			}
		}

		entitiesToAdd.clear();

		for(T e : entitiesToRemove){
			entityArray.remove(e);
			if(map != null){
				map.remove(e.getID());
			}
			e.removed();
		}

		entitiesToRemove.clear();
	}

	public T getByID(int id){
		if(map == null) throw new RuntimeException("Mapping is not enabled for this group!");
		return map.get(id);
	}

	public synchronized void remap(T entity, int newID){
		map.remove(entity.getID());
		entity.resetID(newID);
		map.put(newID, entity);
	}
	
	public QuadTree tree(){
		return tree;
	}
	
	public void setTree(float x, float y, float w, float h){
		tree = new QuadTree(Entities.maxLeafObjects, new Rectangle(x, y, w, h));
	}

	public boolean isEmpty(){
		return entityArray.size() == 0;
	}

	public int size(){
		return entityArray.size();
	}

	public int count(Predicate<T> pred){
		int count = 0;
		for(T t : entityArray){
			if(pred.test(t)) count ++;
		}
		return count;
	}
	
	public synchronized void add(T type){
		if(type == null) throw new RuntimeException("Cannot add a null entity!");
		if(type.getGroup() != null) return; //throw new RuntimeException("Entities cannot be added twice!");
		type.setGroup(this);
		entitiesToAdd.add(type);
	}
	
	public synchronized void remove(T type){
		if(type == null) throw new RuntimeException("Cannot remove a null entity!");
		type.setGroup(null);
		entitiesToRemove.add(type);
	}
	
	public synchronized void clear(){
		for(T entity : entityArray)
			entity.setGroup(null);
		
		for(T entity : entitiesToAdd)
			entity.setGroup(null);
		
		for(T entity : entitiesToRemove)
			entity.setGroup(null);
		
		entitiesToAdd.clear();
		entitiesToRemove.clear();
		entityArray.clear();
		if(map != null)
			map.clear();
	}
	
	public synchronized EntityContainer<T> all(){
		return entityArray;
	}

	public synchronized void setContainer(EntityContainer<T> container){
		container.clear();

		for(int i = 0; i < entityArray.size(); i ++){
			container.add(entityArray.get(i));
		}

		entityArray = container;
	}

	public interface EntityContainer<T> extends Iterable<T>{
		int size();
		void add(T item);
		void clear();
		void remove(T item);
		T get(int index);
	}

	public static class ArrayContainer<T> implements EntityContainer<T>{
		private Array<T> array = new Array<>();

		@Override
		public int size() {
			return array.size;
		}

		@Override
		public void add(T item) {
			array.add(item);
		}

		@Override
		public void clear() {
			array.clear();
		}

		@Override
		public void remove(T item) {
			array.removeValue(item, true);
		}

		@Override
		public T get(int index) {
			return array.get(index);
		}

		@Override
		public Iterator<T> iterator() {
			return array.iterator();
		}
	}
}
