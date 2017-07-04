package io.anuke.ucore.modules;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.core.Settings;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.util.Mathf;

public abstract class RendererModule<T extends ModuleController<T>> extends Module<T>{
	private static Vector3 pan = new Vector3();
	
	public OrthographicCamera camera = new OrthographicCamera();
	public SpriteBatch batch = new SpriteBatch();
	public Atlas atlas;
	public BitmapFont font;
	
	public int cameraScale = 1;
	public Color clearColor = Color.BLACK;
	public float shakeIntensity, shaketime;
	
	protected boolean pixelate;
	
	public RendererModule(){
		Settings.defaults("screenshake", 4);
	}
	
	public void shake(float intensity, float duration){
		shakeIntensity = Math.max(intensity, shakeIntensity);
		shaketime = Math.max(shaketime, duration);
	}
	
	public void setCamera(float x, float y){
		camera.position.set(x, y, 0);
	}
	
	public void smoothCamera(float x, float y, float alpha){
		camera.position.interpolate(pan.set(x, y, 0), alpha*delta(), Interpolation.linear);
	}
	
	public void roundCamera(){
		camera.position.x = (int)camera.position.x;
		camera.position.y = (int)camera.position.y;
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
			float intensity = shakeIntensity*(Settings.getInt("screenshake")/4f);
			camera.position.add(Mathf.range(intensity), Mathf.range(intensity), 0);
			shakeIntensity -= 0.25f*delta();
			shaketime -= delta();
			shakeIntensity = Mathf.clamp(shakeIntensity, 0f, 100f);
		}else{
			shakeIntensity = 0f;
		}
	}
	
	/**Updates the camera, clears the screen, begins the batch and calls draw().*/
	public void drawDefault(){
		camera.update();
		
		Draw.beginCam();
		
		if(pixelate) 
			beginPixel();
		
		clearScreen(clearColor);
		
		draw();
		
		postDraw();
		
		if(pixelate) 
			endPixel();
		
		Draw.end();
	}
	
	public void postDraw(){}
	
	/**override this*/
	public void draw(){}
	
	public void pixelate(){
		pixelate(-1);
	}
	
	public void pixelate(int scl){
		Draw.addSurface("pixel", scl == -1 ? cameraScale : scl);
		pixelate = true;
	}
	
	public void beginPixel(){
		Draw.surface("pixel");
	}
	
	public void endPixel(){
		Draw.flushSurface();
	}
	
	@Override
	public void dispose(){
		Draw.dispose();
	}
	
	@Override
	public void preInit(){
		DrawContext.set(batch, camera, atlas, font);
	}
	
	@Override
	public void resize(int width, int height){
		camera.setToOrtho(false, width/cameraScale, height/cameraScale);
		
		resize();
	}
}
