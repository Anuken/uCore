package io.anuke.ucore.modules;

public abstract class Core extends ModuleController{
	
	public void add(Module module){
		addModule(module);
	}
	/*
	public T control;
	public N ui;
	
	{Hue.init();}
	
	public void add(T t){
		control = t;
		t.preInit();
	}
	
	public void add(N t){
		ui = t;
		ui.preInit();
	}
	
	void update(){
		Inputs.update();
		Timers.update(Gdx.graphics.getDeltaTime()*60f);
	}
	
	public abstract void init();
	
	@Override
	public void resize(int width, int height){
		Module.screen.set(width, height);
		control.resize(width, height);
		ui.resize(width, height);
	}
	
	@Override
	public final void create(){
		init();
		control.init();
		ui.init();
	}
	
	@Override
	public void render(){
		control.update();
		ui.update();
		update();
	}
	
	@Override
	public void pause(){
		control.pause();
		ui.pause();
	}
	
	@Override
	public void resume(){
		control.resume();
		ui.resume();
	}
	
	@Override
	public void dispose(){
		control.dispose();
		ui.dispose();
	}
	*/
}
