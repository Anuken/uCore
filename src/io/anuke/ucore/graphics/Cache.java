package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

import io.anuke.ucore.core.Draw;

public class Cache{
	private static final int maxDraws = 2000;
	
	protected Array<SpriteCache> caches = new Array<>();
	protected IntArray cacheIDs = new IntArray();
	private SpriteCache current;
	private int draws = 0;
	
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
			current = new SpriteCache(2000, true);
			caches.add(current);
			current.beginCache();
		}else{
			if(draws > maxDraws){
				
				cacheIDs.add(current.endCache());
				
				current = null;
				draws = 0;
				checkCache();
			}
		}
	}
}
