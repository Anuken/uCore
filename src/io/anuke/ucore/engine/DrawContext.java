package io.anuke.ucore.engine;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawContext{
	public static Camera camera;
	public static SpriteBatch batch;
	public static RegionProvider provider;
	
	public static void set(SpriteBatch abatch, Camera acamera, RegionProvider aprovider){
		camera = acamera;
		batch = abatch;
		provider = aprovider;
	}
}
