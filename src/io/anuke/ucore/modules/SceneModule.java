package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.core.Inputs;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.Scene;
import io.anuke.ucore.scene.Skin;
import io.anuke.ucore.scene.style.Drawable;
import io.anuke.ucore.scene.ui.layout.Table;

public class SceneModule<T extends ModuleController<T>> extends Module<T>{
	private static String[] colorTypes = {"accent", "title"};
	
	public Scene scene;
	public Skin skin;
	
	public SceneModule(){
		scene = new Scene(DrawContext.batch);
		Inputs.addProcessor(scene);
		
		if(Gdx.files.internal("ui/uiskin.json").exists()){
			skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
			skin.font().setUseIntegerPositions(false);
		}else{
			Gdx.app.error("UI", "ERROR: No skin file found in ui/uiskin.json. UI features are disabled.");
		}
		
		loadContext();

	}
	
	protected void loadContext(){
		DrawContext.setScene(scene, skin);
		
		if(DrawContext.font == null && skin != null)
			DrawContext.font = skin.font();
		
		for(String s : colorTypes)
			if(Colors.get(s) == null)
				Colors.put(s, Color.WHITE);
		
	}
	
	public boolean hasMouse(){
		return scene.hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), true) != null;
	}
	
	/**Updates and draws the stage.*/
	public void act(){
		scene.act();
		scene.draw();
	}
	
	/**Gets a drawable by name*/
	public Drawable tex(String name){
		return DrawContext.skin.getDrawable(name);
	}
	
	/**Find an element by name, or by class if prefixed by #.*/
	public <N> N $(String name){
		if(name.startsWith("#")){
			for(Element a : scene.getElements()){
				if(a.getClass().getSimpleName().toLowerCase().equals(name.substring(0, 1))){
					return (N)a;
				}
			}
			return null;
		}
		return (N)scene.find(name);
	}
	
	public <N> Array<N> $list(Class<N> type){
		Array<N> arr = new Array<N>();
		for(Element actor : scene.getElements()){
			if(actor.getClass() == type){
				arr.add((N)actor);
			}
		}
		
		return arr;
	}
	
	@Override
	public void update(){
		act();
	}
	
	/**Creates and adds a new layout to fill the stage.*/
	public Table fill(){
		return scene.table();
	}
	
	@Override
	public void resize(int width, int height){
		scene.resize(width, height);
	}
}
