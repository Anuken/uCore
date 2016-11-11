package io.anuke.ucore.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.ObjectMap;

public class Models{
	private static ObjLoader loader = new ObjLoader();
	private static ObjectMap<String, Model> models = new ObjectMap<String, Model>();
	private static ObjectMap<String, ModelInstance> modelsi = new ObjectMap<String, ModelInstance>();
	
	public static Model get(String name){
		if(!models.containsKey(name)){
			Model model = loader.loadModel(Gdx.files.internal("models/" + name + ".obj"));
			models.put(name, model);
			return model;
		}
		return models.get(name);
	}
	
	public static ModelInstance geti(String name){
		if(!modelsi.containsKey(name)){
			ModelInstance i = new ModelInstance(get(name));
			modelsi.put(name, i);
			return i;
		}
		return modelsi.get(name);
	}
	
	public static ModelTransform gett(String name){
		return new ModelTransform(geti(name));
	}
	
	public static void dispose(){
		for(Model model : models.values())
			model.dispose();
	}
}
