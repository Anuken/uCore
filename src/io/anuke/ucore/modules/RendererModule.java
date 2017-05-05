package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.core.Settings;
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
	
	public void clampCamera(float minx, float miny, float maxx, float maxy){
		float vw = camera.viewportWidth/2f;
		float vh = camera.viewportHeight/2f;
		Vector3 pos = camera.position;
		
		if(pos.x - vw < minx)
			pos.x = vw + minx;
		
		if(pos.y - vh < miny)
			pos.y = vh + miny;
		
		if(pos.x + vw > maxx)
			pos.x = maxx-vw;
		
		if(pos.y + vh > maxy)
			pos.y = maxy-vh;
	}
	
	public void updateShake(){
		if(shaketime > 0){
			float intensity = shakeIntensity*(Settings.getInt("screenshake", 4)/4f);
			camera.position.add(Mathf.range(intensity), Mathf.range(intensity), 0);
			shakeIntensity -= 0.25f;
			shaketime -= delta();
		}else{
			shakeIntensity = 0f;
		}
	}
	
	/**Updates the camera, clears the screen, begins the batch and calls draw().*/
	public void drawDefault(){
		camera.update();
		
		if(pixelate) beginPixel();
		clearScreen(clearColor);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		draw();
		batch.end();
		
		if(pixelate) endPixel();
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
		drawFull("pixel");
	}
	
	public void drawFull(String buffername){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.begin();
		batch.draw(buffers.texture(buffername), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		batch.end();
	}
	
	@Override
	public void preInit(){
		DrawContext.set(batch, camera, atlas, font);
	}
	
	@Override
	public void resize(int width, int height){
		camera.setToOrtho(false, width/cameraScale, height/cameraScale);
		
		if(pixelate){
			buffers.remove("pixel");
			buffers.add("pixel", (int)(Gdx.graphics.getWidth()/cameraScale), 
					(int)(Gdx.graphics.getHeight()/cameraScale));
		}
		
		resize();
	}
}
