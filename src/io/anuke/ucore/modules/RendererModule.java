package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.graphics.FrameBufferMap;

public abstract class RendererModule<T extends ModuleController<T>> extends Module<T>{
	public OrthographicCamera camera = new OrthographicCamera();
	public SpriteBatch batch = new SpriteBatch();
	public FrameBufferMap buffers = new FrameBufferMap();
	public Atlas atlas;
	public BitmapFont font;
	public float cameraScale = 1f;
	private boolean pixelate;
	
	public RendererModule(){
		
	}
	
	public void setPixelation(){
		buffers.add("pixel", (int)(Gdx.graphics.getWidth()/cameraScale), 
				(int)(Gdx.graphics.getHeight()/cameraScale));
		pixelate = true;
	}
	
	public void beginPixel(){
		buffers.begin("pixel");
		clearScreen(Color.CLEAR);
	}
	
	public void endPixel(){
		buffers.end("pixel");
		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.begin();
		batch.draw(buffers.texture("pixel"), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		batch.end();
	}
	
	public void preInit(){
		DrawContext.set(batch, camera, atlas, font);
	}
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/cameraScale, height/cameraScale);
		
		if(pixelate){
			buffers.remove("pixel");
			buffers.add("pixel", (int)(Gdx.graphics.getWidth()/cameraScale), 
					(int)(Gdx.graphics.getHeight()/cameraScale));
		}
	}
}
