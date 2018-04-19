package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;


public abstract class Module extends InputAdapter{
	public boolean doUpdate = true;
	
	public void update(){}
	public void init(){}
	public void preInit(){}
	public void pause(){}
	public void resume(){}
	public void dispose(){}
	public void resize(int width, int height){resize();}
	public void resize(){}

	public float delta(){
		return Gdx.graphics.getDeltaTime()*60f;
	}
}
