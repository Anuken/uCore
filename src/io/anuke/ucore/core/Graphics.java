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
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.graphics.CustomSurface;
import io.anuke.ucore.graphics.Shader;
import io.anuke.ucore.graphics.Surface;

public class Graphics{
	private static Vector3 vec3 = new Vector3();
	private static Vector2 mouse = new Vector2();
	private static Vector2 size = new Vector2();
	
	private static TextureRegion tempregion = new TextureRegion();
	
	private static Stack<Batch> batches = new Stack<Batch>();
	
	private static ObjectMap<String, Surface> surfaces = new ObjectMap<>();
	private static Stack<Surface> surfaceStack = new Stack<>();
	
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

	public static Surface currentSurface(){
		return surfaceStack.isEmpty() ? null : surfaceStack.peek();
	}

	/** Adds a custom surface that handles events. */
	public static void addSurface(CustomSurface surface){
		surfaces.put(surface.name(), surface);
	}

	/** Creates a surface, sized to the screen */
	public static void addSurface(String name){
		Graphics.addSurface(name, 1, 0);
	}

	public static void addSurface(String name, int scale){
		Graphics.addSurface(name, scale, 0);
	}

	/**
	 * Creates a surface, scale times smaller than the screen. Useful for
	 * pixelated things.
	 */
	public static void addSurface(String name, int scale, int bind){
		surfaces.put(name, new Surface(name, scale, bind));
	}

	public static Surface getSurface(String name){
		if(!surfaces.containsKey(name))
			throw new IllegalArgumentException("The surface \"" + name + "\" does not exist!");
	
		return surfaces.get(name);
	}

	/** Begins drawing on a surface. */
	public static void surface(String name){
		if(!surfaceStack.isEmpty()){
			end();
			surfaceStack.peek().end(false);
		}
	
		Surface surface = getSurface(name);
	
		surfaceStack.push(surface);
	
		if(drawing())
			end();
		
		surface.begin();
		
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
	public static void flushSurface(String name){
		Graphics.checkSurface();
	
		Surface surface = surfaceStack.pop();
	
		end();
		surface.end(true);
	
		Surface current = surfaceStack.empty() ? null : surfaceStack.peek();
	
		if(current != null)
			current.begin(false);
	
		Graphics.setScreen();
		
		if(name != null)
			surface(name);
		
		batch.draw(surface.texture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		
		if(name != null)
			surface();
		
		end();
	
		beginCam();
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

	/**Begin the postprocessing shaders.*/
	public static void beginShaders(Shader... types){
		currentShaders = types;
		
		batch.flush();
		
		surface("effects1");
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
			int index = 2;
			
			for(Shader shader : currentShaders){
				boolean ending = i == currentShaders.length - 1;
				
				tempregion.setRegion(currentSurface().texture());
				
				Graphics.shader(shader);
				shader.program().begin();
				shader.region = tempregion;
				shader.apply();
				shader.program().end();
				flushSurface(ending ? null : ("effects" + index));
				Graphics.shader();
				
				if(!ending){
					surface("effects" + index);
				}
				
				index = (index == 2 ? 1 : 2);
				
				i ++;
			}
		}
	}

	/** Set the shader by class and returns the reference.*/
	public static void shader(Shader shader){
		boolean rendering = batch.isDrawing();
	
		if(rendering)
			batch.end();
	
		batch.setShader(shader.program());
		
		shader.program().begin();
		shader.apply();
		shader.program().end();
	
		if(rendering)
			batch.begin();
	}

	/** Revert to the default shader. */
	public static void shader(){
		boolean rendering = batch.isDrawing();
	
		if(rendering)
			batch.end();
	
		batch.setShader(null);
	
		if(rendering)
			batch.begin();
	}

	public static void resize(){
		if(!surfaces.containsKey("effects1")){
			addSurface("effects1", Core.cameraScale);
			addSurface("effects2", Core.cameraScale);
		}
		
		for(Surface surface : surfaces.values()){
			surface.resize();
		}
		
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
		for(Surface surface : surfaces.values()){
			surface.dispose();
		}
	}
}
