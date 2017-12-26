package io.anuke.ucore.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.scene.Scene;
import io.anuke.ucore.scene.Skin;

public class Core{
	public static OrthographicCamera camera = new OrthographicCamera();
	public static Batch batch = new SpriteBatch(2048);
	public static Atlas atlas;
	public static BitmapFont font;
	public static int cameraScale = 1;

	public static I18NBundle bundle;
	public static Scene scene;
	public static Skin skin;
	
	static{
		
		for(String s : new ObjectMap.Keys<String>(Colors.getColors())){
			if(s != null)
			Colors.put(s.toLowerCase().replace("_", ""), Colors.get(s));
		}
		
		Colors.put("crimson", Color.SCARLET);
		Colors.put("scarlet", Color.SCARLET);
	}
	
	public static void setScene(Scene ascene, Skin askin){
		if(ascene != null) scene = ascene;
		if(askin != null) skin = askin;
	}

	/* Disposes of all resources, as well as internal resources and skin.*/
	public static void dispose(){
		Draw.dispose();
		Graphics.dispose();
		Inputs.dispose();
		Sounds.dispose();
		Musics.dispose();
		Timers.dispose();
		
		if(batch != null){
			batch.dispose();
			batch = null;
		}
		
		if(scene != null){
			scene.dispose();
			scene = null;
		}
	
		if(atlas != null){
			atlas.dispose();
			atlas = null;
		}
	
		if(skin != null){
			skin.dispose();
			skin = null;
		}
		
		if(font != null){
			font.dispose();
			font = null;
		}
	}
}
