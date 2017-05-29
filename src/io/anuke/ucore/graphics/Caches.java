package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.core.Draw;

public class Caches{
	private static Array<Cache> caches = new Array<>();
	private static Cache current;
	
	public static void render(Cache cache){
		Gdx.gl.glEnable(GL20.GL_BLEND);
		for(int i = 0; i < cache.caches.size; i ++){
			cache.caches.get(i).setProjectionMatrix(Draw.batch().getProjectionMatrix());
			
			cache.caches.get(i).begin();
			cache.caches.get(i).draw(cache.cacheIDs.get(i));
			cache.caches.get(i).end();
		}
	}
	
	public static void draw(TextureRegion region, float x, float y, float w, float h){
		checkCache();
		current.draw(region, x, y, w, h);
	}
	
	public static void draw(String region, float x, float y){
		checkCache();
		current.draw(region, x, y);
	}
	
	/**Calls begin() with 2000 sprites per cache.*/
	public static void begin(){
		begin(2000);
	}
	
	public static void begin(int size){
		if(current != null)
			throw new RuntimeException("Call end() before begin()!");
		
		current = new Cache(size);
		current.begin();
		caches.add(current);
	}
	
	public static Cache end(){
		if(current == null)
			throw new RuntimeException("Call begin() before end()!");
		
		current.end();
		Cache c = current;
		current = null;
		
		return c;
	}
	
	private static void checkCache(){
		if(current == null)
			throw new RuntimeException("Call begin() before drawing!");
	}
}
