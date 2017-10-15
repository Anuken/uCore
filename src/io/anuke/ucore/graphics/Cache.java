package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;

import io.anuke.ucore.core.Draw;

public class Cache implements Disposable{
	protected Array<SpriteCache> caches = new Array<>();
	protected IntArray cacheIDs = new IntArray();
	
	private int size = 2000;
	private SpriteCache current;
	private int draws = 0;
	private boolean disposed = false;
	
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
	
	public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation){
		checkCache();
		
		current.add(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
		
		draws ++;
	}
	
	public void draw(String region, float x, float y){
		TextureRegion r = Draw.region(region);
		draw(r, x-r.getRegionWidth()/2, y-r.getRegionHeight()/2, r.getRegionWidth(), r.getRegionHeight());
	}
	
	public void draw(String region, float x, float y, float rotation){
		checkCache();
		
		TextureRegion r = Draw.region(region);
		current.add(r, x-r.getRegionWidth()/2, y-r.getRegionHeight()/2, r.getRegionWidth()/2, r.getRegionHeight()/2,
				r.getRegionWidth(), r.getRegionHeight(), 1, 1, rotation);
		
		draws ++;
	}
	
	public SpriteCache getCurrent(){
		return current;
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
		
		current.setColor(Caches.getColor());
	}

	@Override
	public void dispose(){
		if(disposed) return;
		
		for(SpriteCache cache : caches){
			cache.dispose();
		}
		
		disposed = true;
	}
}
