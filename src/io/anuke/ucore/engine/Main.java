package io.anuke.ucore.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import io.anuke.ucore.modules.Module;
import io.anuke.ucore.modules.ModuleController;

public abstract class Main<T extends ModuleController<T>> extends ModuleController<T>{
	
	@Override
	public void addModule(Class<? extends Module<T>> c){
		try{
			Module<T> m = ClassReflection.newInstance(c);
			m.main = (T)this;
			modules.put(c, m);
			modulearray.add(m);
			
			if(m instanceof RendererModule)
				((RendererModule<T>)m).setContext();
			
		}catch (RuntimeException e){
			throw e;
		}catch (Exception e){
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	@Override
	public void update(){
		Input.update();
	}
}
