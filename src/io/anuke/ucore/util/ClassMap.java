package io.anuke.ucore.util;

import java.util.Iterator;

import com.badlogic.gdx.utils.ObjectMap;

public class ClassMap<T> implements Iterable<T>{
	private ObjectMap<Class<? extends T>, T> map = new ObjectMap<>();
	
	public void add(T type){
		map.put((Class<? extends T>) type.getClass(), type);
	}
	
	public <N extends T> N get(Class<N> type){
		return (N)map.get(type);
	}
	
	public void clear(){
		map.clear();
	}
	
	public int size(){
		return map.size;
	}

	@Override
	public Iterator<T> iterator(){
		return map.values();
	}
}
