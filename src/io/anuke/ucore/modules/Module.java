package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import io.anuke.ucore.UCore;


public abstract class Module<T extends ModuleController<T>> extends InputAdapter{
	public T t;
	public void update(){}
	public void init(){}
	public void pause(){}
	public void resume(){}
	public void dispose(){}
	public void resize(int width, int height){}
	
	@SuppressWarnings("unchecked")
	public <N> N getModule(Class<N> c){
		return (N)(t.getModule((Class<? extends Module<T>>)c));
	}
	
	public void maximize(){
		UCore.maximizeWindow();
	}
	
	public void clearScreen(){
		clearScreen(Color.BLACK);
	}
	
	public void clearScreen(Color color){
		UCore.clearScreen(color);
	}
	
	public void clearScreen(float r, float g, float b){
		Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	public int gwidth(){
		return Gdx.graphics.getWidth();
	}
	
	public int gheight(){
		return Gdx.graphics.getHeight();
	}
}
