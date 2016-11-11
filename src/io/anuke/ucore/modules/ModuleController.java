package io.anuke.ucore.modules;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public abstract class ModuleController<T extends ModuleController<T>> extends ApplicationAdapter{
	
	protected ObjectMap<Class<? extends Module<T>>, Module<T>> modules = new ObjectMap<Class<? extends Module<T>>, Module<T>>();
	protected Array<Module<T>> modulearray = new Array<Module<T>>();
	
	@SuppressWarnings("unchecked")
	public void addModule(Class<? extends Module<T>> c){
		try{
			Module<T> m = ClassReflection.newInstance(c);
			m.t = (T)this;
			modules.put(c, m);
			modulearray.add(m);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <N> N getModule(Class<N> c){
		return (N)(modules.get((Class<? extends Module<T>>)c));
	}
	
	abstract public void init();
	
	@Override
	public void resize(int width, int height){
		for(Module<T> module : modulearray){
			module.resize(width, height);
		}
	}
	
	@Override
	public final void create(){
		init();
		for(Module<T> module : modulearray){
			module.init();
		}
	}
	
	@Override
	public void render(){
		for(Module<T> module : modulearray){
			module.update();
		}
	}
	
	@Override
	public void pause(){
		for(Module<T> module : modulearray)
			module.pause();
	}
	
	@Override
	public void resume(){
		for(Module<T> module : modulearray)
			module.resume();
	}
	
	@Override
	public void dispose(){
		for(Module<T> module : modulearray)
			module.dispose();
	}
}
