package io.anuke.ucore.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.anuke.ucore.core.DrawContext;
import io.anuke.ucore.core.UInput;
import io.anuke.ucore.scene.Element;
import io.anuke.ucore.scene.Scene;
import io.anuke.ucore.scene.style.Drawable;
import io.anuke.ucore.scene.style.Styles;
import io.anuke.ucore.scene.ui.layout.Table;

public class SceneModule<T extends ModuleController<T>> extends Module<T>{
	public Scene scene;
	public Styles styles;
	
	public SceneModule(){
		scene = new Scene(new ScreenViewport(), DrawContext.batch);
		UInput.addProcessor(scene);
		
		styles = new Styles(Gdx.files.internal("ui/uiskin.json"));
		loadStyles(styles);
	}
	
	protected void loadStyles(Styles styles){
		this.styles = styles;
		Styles.load(styles);
	}
	
	/**Updates and draws the stage.*/
	public void act(){
		scene.act();
		scene.draw();
	}
	
	/**Gets a drawable by name*/
	public Drawable tex(String name){
		return Styles.styles.getDrawable(name);
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
