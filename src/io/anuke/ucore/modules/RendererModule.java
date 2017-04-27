package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.graphics.FrameBufferMap;
import io.anuke.ucore.util.Mathf;

public abstract class RendererModule<T extends ModuleController<T>> extends Module<T>{
	public OrthographicCamera camera = new OrthographicCamera();
	public SpriteBatch batch = new SpriteBatch();
	public FrameBufferMap buffers = new FrameBufferMap();
	public Atlas atlas;
	public BitmapFont font;
	public float cameraScale = 1f;
	public Color clearColor = Color.BLACK;
	public float shakeIntensity, shaketime;
	
	private boolean pixelate;
	
	public RendererModule(){
		
	}
	
	public void shake(float intensity, float duration){
		shakeIntensity = Math.max(intensity, shakeIntensity);
		shaketime = Math.max(shaketime, duration);
	}
	
	public void setCamera(float x, float y){
		camera.position.set(x, y, 0);
	}
	
	public void updateShake(){
		if(shaketime > 0){
			camera.position.add(Mathf.range(shakeIntensity), Mathf.range(shakeIntensity), 0);
			shakeIntensity -= 0.5f;
			shaketime -= delta();
		}
	}
	
	/**Updates the camera, clears the screen, begins the batch and calls draw().*/
	public void drawDefault(){
		updateShake();
		camera.update();
		
		clearScreen(clearColor);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		draw();
		batch.end();
	}
	
	/**override this*/
	public void draw(){}
	
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
