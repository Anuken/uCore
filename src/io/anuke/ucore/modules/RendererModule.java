package io.anuke.ucore.modules;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class RendererModule<T extends ModuleController<T>> extends Module<T>{
	public OrthographicCamera camera = new OrthographicCamera();
	public SpriteBatch batch = new SpriteBatch();
	public float cameraScale = 1f;
	
	public void resize(int width, int height){
		camera.setToOrtho(false, width/cameraScale, height/cameraScale);
	}
}
