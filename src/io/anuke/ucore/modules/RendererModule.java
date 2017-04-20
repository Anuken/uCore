package io.anuke.ucore.modules;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.graphics.Atlas;

public abstract class RendererModule<T extends ModuleController<T>> extends Module<T>{
	public OrthographicCamera camera = new OrthographicCamera();
	public SpriteBatch batch = new SpriteBatch();
	public Atlas atlas;
	public float cameraScale = 1f;
	
	public RendererModule(){
		
	}
	
	public void preInit(){
		DrawContext.set(batch, camera, atlas);
	}
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/cameraScale, height/cameraScale);
	}
}
