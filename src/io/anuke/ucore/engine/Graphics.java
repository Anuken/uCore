package io.anuke.ucore.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Graphics{
	private static Vector3 vec3 = new Vector3();
	private static Vector2 mouse = new Vector2();
	private static Vector2 size = new Vector2();
	
	public static Vector2 getMouse(){
		mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
		return mouse;
	}
	
	public static Vector2 getMouseWorld(){
		Main.getViewport().unproject(vec3.set(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY(), 0));
		return mouse.set(vec3.x, vec3.y);
	}
	
	public static Vector2 getSize(){
		return size.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
}
