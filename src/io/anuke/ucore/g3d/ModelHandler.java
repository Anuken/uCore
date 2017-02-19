package io.anuke.ucore.g3d;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;

/**this class was a mistake*/
/**(probably)*/
public class ModelHandler{
	private static ModelHandler instance;
	
	private Array<ModelRenderable> models = new Array<ModelRenderable>();
	private Environment environment;
	private int peak;
	
	private ModelHandler(){}
	
	public static ModelHandler instance(){
		if(instance == null) instance = new ModelHandler();
		return instance;
	}
	
	public void setEnvironment(Environment env){
		this.environment = env;
	}
	
	public Environment environment(){
		return environment;
	}
	
	public int count(){
		return models.size;
	}
	
	public int peak(){
		return peak;
	}
	
	public void render(ModelBatch batch){
		peak = Math.max(peak, models.size);
		for(ModelRenderable model : models)
			model.render(batch);
	}
	
	public void add(ModelRenderable model){
		models.add(model);
	}
	
	public void remove(ModelRenderable model){
		models.removeValue(model, true);
	}
	
	public void remove(Iterable<? extends ModelRenderable> list){
		for(ModelRenderable r : list)
			remove(r);
	}
	
	public void clear(){
		models.clear();
	}
}
