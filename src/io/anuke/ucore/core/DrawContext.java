package io.anuke.ucore.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.graphics.RegionProvider;

/**Provides context for drawing things. Values are usually set in a RendererModule.*/
public class DrawContext{
	public static Camera camera;
	public static SpriteBatch batch;
	public static RegionProvider provider;
	public static BitmapFont font;
	
	public static void set(SpriteBatch abatch, Camera acamera, RegionProvider aprovider, BitmapFont afont){
		camera = acamera;
		batch = abatch;
		provider = aprovider;
		font = afont;
	}
}
