package io.anuke.ucore.aabb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ColliderPartition{
	private HashMap<Long, ArrayList<Collider>> map = new HashMap<Long, ArrayList<Collider>>();
	
	public ArrayList<Collider> get(int x, int y){
		return map.get(getHash(x,y));
	}
	
	public boolean hasCell(int x, int y){
		return map.containsKey(getHash(x,y));
	}
	
	public void putCell(int x, int y, ArrayList<Collider> t){
		map.put(getHash(x,y), t);
	}
	
	public Collection<ArrayList<Collider>> cells(){
		return map.values();
	}
	
	public void clear(){
		map.clear();
	}
	
	public int size(){
		return map.size();
	}
	
	private long getHash(int x, int y){
		return (((long)x) << 32) | (y & 0xffffffffL);
	}
}
