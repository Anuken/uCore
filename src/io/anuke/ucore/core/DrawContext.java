package io.anuke.ucore.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.graphics.Atlas;

/**Provides context for drawing things. Values are usually set in a RendererModule.*/
public class DrawContext{
	public static OrthographicCamera camera;
	public static SpriteBatch batch;
	public static Atlas atlas;
	public static BitmapFont font;
	
	public static void set(SpriteBatch abatch, OrthographicCamera acamera, Atlas aprovider, BitmapFont afont){
		camera = acamera;
		batch = abatch;
		atlas = aprovider;
		font = afont;
	}
}
