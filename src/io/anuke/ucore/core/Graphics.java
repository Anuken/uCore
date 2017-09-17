package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Graphics{
	private static Vector3 vec3 = new Vector3();
	private static Vector2 mouse = new Vector2();
	private static Vector2 size = new Vector2();
	
	public static Vector2 mouse(){
		mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
		return mouse;
	}
	
	public static Vector2 mouseWorld(){
		Core.camera.unproject(vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		return mouse.set(vec3.x, vec3.y);
	}
	
	public static Vector2 world(float screenx, float screeny){
		Core.camera.unproject(vec3.set(screenx, screeny, 0));
		return mouse.set(vec3.x, vec3.y);
	}
	
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
}
