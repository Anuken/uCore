package io.anuke.ucore.core;

import static io.anuke.ucore.core.Core.batch;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.graphics.CustomSurface;
import io.anuke.ucore.graphics.Shader;
import io.anuke.ucore.graphics.Surface;

public class Graphics{
	private static Vector3 vec3 = new Vector3();
	private static Vector2 mouse = new Vector2();
	private static Vector2 size = new Vector2();
	
	private static TextureRegion tempregion = new TextureRegion();
	
	private static Stack<Batch> batches = new Stack<Batch>();
	
	//TODO stop mapping things to strings!
	//private static ObjectMap<String, Surface> surfaces = new ObjectMap<>();
	private static Array<Surface> surfaceArray = new Array<>();
	private static Stack<Surface> surfaceStack = new Stack<>();
	
	private static Surface effects1, effects2;
	
	private static Shader[] currentShaders;
	
	/**Mouse coords.*/
	public static Vector2 mouse(){
		mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
		return mouse;
	}
	
	/**World coordinates for current mouse coords.*/
	public static Vector2 mouseWorld(){
		Core.camera.unproject(vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		return mouse.set(vec3.x, vec3.y);
	}
	
	/**World coordinates for supplied screen coords.*/
	public static Vector2 world(float screenx, float screeny){
		Core.camera.unproject(vec3.set(screenx, screeny, 0));
		return mouse.set(vec3.x, vec3.y);
	}
	
	/**Screen coordinates for supplied world coords.*/
	public static Vector2 screen(float worldx, float worldy){
		Core.camera.project(vec3.set(worldx, worldy, 0));
		return mouse.set(vec3.x, vec3.y);
	}
	
	/**Screen size.*/
	public static Vector2 size(){
		return size.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public static void clear(Color color){
		Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void clear(float r, float g, float b){
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	public static void useBatch(Batch batch){
		if(batches.isEmpty())
			batches.push(Core.batch);
		batches.push(batch);
		Core.batch = batch;
	}

	public static void popBatch(){
		batches.pop();
		Core.batch = batches.peek();
	}
	
	public static Array<Surface> getSurfaces(){
		return surfaceArray;
	}

	public static Surface currentSurface(){
		return surfaceStack.isEmpty() ? null : surfaceStack.peek();
	}

	/** Adds a custom surface that handles events. */
	public static CustomSurface createSurface(CustomSurface surface){
		surfaceArray.add(surface);
		return surface;
	}

	/** Creates a surface, sized to the screen */
	public static Surface createSurface(){
		return createSurface(-1, 0);
	}

	public static Surface createSurface(int scale){
		return createSurface(scale, 0);
	}

	/**Creates a surface, scale times smaller than the screen. Useful for
	 * pixelated things.*/
	public static Surface createSurface(int scale, int bind){
		Surface s = new Surface(scale, bind);
		surfaceArray.add(s);
		return s;
	}
	
	/** Begins drawing on a surface, clearing it. */
	public static void surface(Surface surface){
		surface(surface, true);
	}

	/** Begins drawing on a surface. */
	public static void surface(Surface surface, boolean clear){
		if(!surfaceStack.isEmpty()){
			end();
			surfaceStack.peek().end(false);
		}
	
		surfaceStack.push(surface);
	
		if(drawing())
			end();
		
		surface.begin(clear);
		
		begin();
	}

	public static void surface(){
		Graphics.surface(false);
	}

	/** Ends drawing on the current surface. */
	public static void surface(boolean end){
		Graphics.checkSurface();
	
		Surface surface = surfaceStack.pop();
	
		end();
		surface.end(true);
	
		if(!end){
			Surface current = surfaceStack.empty() ? null : surfaceStack.peek();
	
			if(current != null)
				current.begin(false);
			begin();
		}
	}

	/** Ends the current surface and draws its contents onto the screen. */
	public static void flushSurface(){
		Graphics.flushSurface(null);
	}

	/** Ends the current surface and draws its contents onto the specified surface. */
	public static void flushSurface(Surface dest){
		Graphics.checkSurface();
	
		Surface surface = surfaceStack.pop();
	
		end();
		surface.end(true);
	
		Surface current = surfaceStack.empty() ? null : surfaceStack.peek();
	
		if(current != null)
			current.begin(false);
		
		beginCam();
		//Graphics.setScreen();
		
		if(dest != null)
			surface(dest);
		
		batch.draw(surface.texture(),
				Core.camera.position.x - Core.camera.viewportWidth/2 * Core.camera.zoom, 
				Core.camera.position.y + Core.camera.viewportHeight/2 * Core.camera.zoom, 
				Core.camera.viewportWidth * Core.camera.zoom, -Core.camera.viewportHeight * Core.camera.zoom);
		
		if(dest != null)
			surface();
	}

	/** Sets the batch projection matrix to the screen, without the camera. */
	public static void setScreen(){
		boolean drawing = batch.isDrawing();
	
		if(drawing)
			end();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		begin();
	}

	static void checkSurface(){
		if(surfaceStack.isEmpty())
			throw new RuntimeException("Surface stack is empty! Set a surface first.");
	}

	/**Begin the postprocessing shaders.
	 * FIXME does not work with multiple shaders*/
	public static void beginShaders(Shader... types){
		currentShaders = types;
		
		batch.flush();
		
		surface(effects1);
	}

	//FIXME does not work with multiple shaders
	/**End the postprocessing shader.*/
	public static void endShaders(){
		
		//batch.flush();
		
		if(currentShaders.length == 1){
			Shader shader = currentShaders[0];
			tempregion.setRegion(currentSurface().texture());
			
			Graphics.shader(shader);
			shader.program().begin();
			shader.region = tempregion;
			shader.apply();
			shader.program().end();
			flushSurface();
			Graphics.shader();
		}else{
			
			int i = 0;
			boolean index = true;
			
			for(Shader shader : currentShaders){
				boolean ending = i == currentShaders.length - 1;
				
				tempregion.setRegion(currentSurface().texture());
				
				Graphics.shader(shader);
				shader.program().begin();
				shader.region = tempregion;
				shader.apply();
				shader.program().end();
				flushSurface(ending ? null : (index ? effects2 : effects1));
				Graphics.shader();
				
				if(!ending){
					surface((index ? effects2 : effects1));
				}
				
				index = !index;
				
				i ++;
			}
		}
	}
	
	public static void flush(){
		Core.batch.flush();
	}

	/** Set the shader, applying immediately.*/
	public static void shader(Shader shader){
		shader(shader, true);
	}
	
	public static void shader(Shader shader, boolean applyOnce){
	
		batch.setShader(shader.program());
		
		if(applyOnce){
			shader.program().begin();
			shader.apply();
			shader.program().end();
		}
	}

	/** Revert to the default shader. */
	public static void shader(){
		
	
		batch.setShader(null);
	}

	public static void resize(){
		if(effects1 == null){
			effects1 = Graphics.createSurface(Core.cameraScale);
			effects2 = Graphics.createSurface(Core.cameraScale);
		}
		
		for(Surface surface : surfaceArray){
			surface.resize();
		}
		
	}
	
	public static void setCameraScale(int scale){
		Core.cameraScale = scale;
		Core.camera.viewportWidth = Gdx.graphics.getWidth() / scale;
		Core.camera.viewportHeight = Gdx.graphics.getHeight() / scale;
		for(Surface surface : surfaceArray){
			surface.resize();
		}
		Core.camera.update();
	}

	/** Begins the batch and sets the camera projection matrix. */
	public static void beginCam(){
		batch.setProjectionMatrix(Core.camera.combined);
		batch.begin();
	}

	/** Begins the batch. */
	public static void begin(){
		batch.begin();
	}

	/** Ends the batch */
	public static void end(){
		batch.end();
	}

	public static boolean drawing(){
		return batch.isDrawing();
	}
	
	static void dispose(){
		for(Surface surface : surfaceArray){
			surface.dispose();
		}
		surfaceArray.clear();
	}
}
