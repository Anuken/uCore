package io.anuke.ucore.g3d;

import com.badlogic.gdx.utils.ObjectMap;

public class ModelGroup{
	public ObjectMap<String, ModelTransform> models = new ObjectMap<String, ModelTransform>();
	
	public void add(String name, ModelTransform t){
		models.put(name, t);
	}
	
	public ModelTransform get(String name){
		return models.get(name);
	}
	
	public void free(){
		ModelHandler.instance().remove(models.values());
	}
}
