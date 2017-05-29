package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

import io.anuke.ucore.core.Draw;

public class Cache{
	protected Array<SpriteCache> caches = new Array<>();
	protected IntArray cacheIDs = new IntArray();
	private int size = 2000;
	private SpriteCache current;
	private int draws = 0;
	
	public Cache(int size){
		this.size = size;
	}
	
	public void render(){
		Caches.render(this);
	}
	
	public void draw(TextureRegion region, float x, float y, float w, float h){
		checkCache();
		
		current.add(region, x, y, w, h);
		
		draws ++;
	}
	
	public void draw(String region, float x, float y){
		TextureRegion r = Draw.region(region);
		draw(r, x-r.getRegionWidth()/2, y-r.getRegionHeight()/2, r.getRegionWidth(), r.getRegionHeight());
	}
	
	protected void begin(){
		checkCache();
	}
	
	protected void end(){
		cacheIDs.add(current.endCache());
	}
	
	private void checkCache(){
		if(current == null){
			current = new SpriteCache(size, true);
			caches.add(current);
			current.beginCache();
		}else{
			if(draws > size-10){
				cacheIDs.add(current.endCache());
				
				current = null;
				draws = 0;
				checkCache();
			}
		}
	}
}
