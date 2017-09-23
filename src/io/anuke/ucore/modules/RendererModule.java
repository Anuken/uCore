package io.anuke.ucore.modules;

import static io.anuke.ucore.core.Core.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;

import io.anuke.ucore.core.*;
import io.anuke.ucore.util.Mathf;

public abstract class RendererModule<T extends ModuleCore<T>> extends Module<T>{
	private static Vector3 pan = new Vector3();
	public Color clearColor = Color.BLACK;
	public float shakeIntensity, shaketime;
	
	protected boolean pixelate;
	protected Object recorder;
	protected Class<?> recorderClass;
	
	public RendererModule(){
		Settings.defaults("screenshake", 4);
		
		Effects.setScreenShakeProvider((intensity, duration)->{
			shake(intensity, duration);
		});
		
		//setup recorder if possible
		try{
			recorderClass = ClassReflection.forName("io.anuke.gif.GifRecorder");
			recorder = ClassReflection.getConstructor(recorderClass, Batch.class).newInstance(batch);
		}catch (Exception e){}
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
		float vw = camera.viewportWidth/2f * camera.zoom;
		float vh = camera.viewportHeight/2f * camera.zoom;
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
		updateShake(1f);
	}
	
	public void updateShake(float scale){
		if(shaketime > 0){
			float intensity = shakeIntensity*(Settings.getInt("screenshake")/4f)*scale;
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
	
	/**Updates the gif recorder. Does nothing on GWT.*/
	public void record(){
		if(recorder == null) return;
		
		try{
			Method method = ClassReflection.getMethod(recorderClass, "update");
			method.invoke(recorder);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void pixelate(){
		pixelate(-1);
	}
	
	public void pixelate(int scl){
		Draw.addSurface("pixel", scl == -1 ? Core.cameraScale : scl);
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
	public void resize(int width, int height){
		camera.setToOrtho(false, width/Core.cameraScale, height/Core.cameraScale);
		
		resize();
	}
}
