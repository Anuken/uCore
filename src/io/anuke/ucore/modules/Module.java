package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;


public abstract class Module<T extends ModuleController<T>> extends InputAdapter{
	public T t;
	public abstract void update();
	public void init(){}
	public void pause(){}
	public void resume(){}
	public void dispose(){}
	
	@SuppressWarnings("unchecked")
	public <N> N getModule(Class<N> c){
		return (N)(t.getModule((Class<? extends Module<T>>)c));
	}
	
	public void resize(int width, int height){
		
	}
	
	public int gwidth(){
		return Gdx.graphics.getWidth();
	}
	
	public int gheight(){
		return Gdx.graphics.getHeight();
	}
}
