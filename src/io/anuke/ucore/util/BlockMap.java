package io.anuke.ucore.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class BlockMap<T>{
	protected HashMap<Long, T> map = new HashMap<Long, T>();
	
	public T get(int x, int y){
		return map.get(getHash(x,y));
	}
	
	public void put(int x, int y, T t){
		map.put(getHash(x,y), t);
	}
	
	public Collection<T> values(){
		return map.values();
	}
	
	public Set<Long> keys(){
		return map.keySet();
	}
	
	private long getHash(int x, int y){
		return (((long)x) << 32) | (y & 0xffffffffL);
	}
}
