package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;


public abstract class Module extends InputAdapter{
	public static Vector2 vector = new Vector2();
	public static Vector2 screen = new Vector2();
	
	public void update(){}
	public void init(){}
	public void preInit(){}
	public void pause(){}
	public void resume(){}
	public void dispose(){}
	public void resize(int width, int height){resize();}
	public void resize(){}
	
	public void clearScreen(){
		clearScreen(Color.BLACK);
	}
	
	public void clearScreen(Color color){
		Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	public void clearScreen(float r, float g, float b){
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	public void log(Object message){
		System.out.println(message);
	}
	
	/*
	public int gwidth(){
		return Gdx.graphics.getWidth();
	}
	
	public int gheight(){
		return Gdx.graphics.getHeight();
	}
	*/
	public float delta(){
		return Gdx.graphics.getDeltaTime()*60f;
	}
}
