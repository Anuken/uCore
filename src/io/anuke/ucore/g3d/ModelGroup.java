package io.anuke.ucore.g3d;

import com.badlogic.gdx.utils.ObjectMap;

public class ModelGroup{
	public ObjectMap<String, ModelRenderable> models = new ObjectMap<String, ModelRenderable>();
	
	public void add(String name, ModelTransform t){
		t.add();
		models.put(name, t);
	}
	
	public ModelRenderable get(String name){
		return models.get(name);
	}
	
	public void free(){
		ModelHandler.instance().remove(models.values());
	}
}
