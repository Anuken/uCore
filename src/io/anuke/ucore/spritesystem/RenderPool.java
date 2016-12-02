package io.anuke.ucore.spritesystem;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pools;

public class RenderPool{
	
	public static SpriteRenderable sprite(TextureRegion region){
		return Pools.obtain(SpriteRenderable.class).setRegion(region);
	}
	
	public static TextRenderable text(){
		return Pools.obtain(TextRenderable.class);
	}

	public static SpriteRenderable light(){
		return Pools.obtain(SpriteRenderable.class);
	}
	
	public static ParticleRenderable particle(){
		return Pools.obtain(ParticleRenderable.class);
	}
	
	public static void free(Renderable r){
		if(r.getClass().isAnonymousClass()) return;
		
		Pools.free(r);
	}
}
