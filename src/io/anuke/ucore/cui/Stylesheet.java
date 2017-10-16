package io.anuke.ucore.cui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import io.anuke.ucore.cui.drawables.NinePatchDrawable;
import io.anuke.ucore.cui.drawables.TextureRegionDrawable;
import io.anuke.ucore.cui.style.Style;
import io.anuke.ucore.graphics.Atlas;

public class Stylesheet{
	private final Json json = new Json();
	/** ??? */
	private ObjectMap<String, Drawable> drawables = new ObjectMap<>();
	/** Map of style names to actual styles (e.g. "background" -> drawable) */
	private ObjectMap<String, Style> styles = new ObjectMap<>();
	private Atlas atlas;

	private Field[] styleFields;
	private Style blankStyle = new Style();

	public Stylesheet(FileHandle atlasFile, FileHandle file) {
		styleFields = ClassReflection.getFields(Style.class);

		atlas = new Atlas(atlasFile);

		for(AtlasRegion region : atlas.getRegions()){
			if(region.splits != null){
				drawables.put(region.name, new NinePatchDrawable(new NinePatch(region, region.splits[0], region.splits[1], region.splits[2], region.splits[3])));
			}else{
				drawables.put(region.name, new TextureRegionDrawable(region));
			}
		}
		
		addSerializers();

		String text = file.readString();

		JsonValue value = json.fromJson(null, text);
		JsonValue child = value.child();
		if(child != null){
			processStyle(child);
			while(child.next != null){
				processStyle(child.next);
				child = child.next;
			}
		}

	}
	
	private void addSerializers(){
		json.setSerializer(Drawable.class, new Serializer<Drawable>(){
			@Override
			public void write(Json json, Drawable object, Class knownType){
				//don't do this
			}

			@Override
			public Drawable read(Json json, JsonValue jsonData, Class type){
				String name = jsonData.asString();
				if(!drawables.containsKey(name)){
					throw new RuntimeException("No drawable found with name: \"" + name + "\"!");
				}
				return drawables.get(name);
			}
		});
		


		json.setSerializer(Color.class, new Serializer<Color>(){
			@Override
			public void write(Json json, Color object, Class knownType){
				//don't do this
			}

			@Override
			public Color read(Json json, JsonValue jsonData, Class type){
				String name = jsonData.asString();
				
				if(name.startsWith("#")){
					return Color.valueOf(name);
				}else{
					if(!Colors.getColors().containsKey(name)) throw new RuntimeException("Color not found: \"" + name + "\"!");
					return Colors.get(name);
				}
			}
		});
	}

	public void getStyle(Style style, String basename, Array<String> extraStyles){
		Style current = styles.get(basename);
		int index = 0;

		try{

			for(Field field : styleFields){
				field.set(style, field.get(blankStyle));
			}

			while(true){
				if(current != null){
					for(Field field : styleFields){
						Object value = field.get(current);
						if(value != null){
							field.set(style, value);
						}
					}
				}

				if(index >= extraStyles.size){
					break;
				}else{
					current = styles.get(extraStyles.get(index));
					if(current == null){
						throw new RuntimeException("Style cannot be null!");
					}
					index++;
				}
			}

		}catch(ReflectionException e){
			throw new RuntimeException(e);
		}
	}

	private void processStyle(JsonValue value){
		String name = value.name;
		String str = value.toString().substring(name.length() + 1);
		try{
			Style style = json.fromJson(Style.class, str);
			styles.put(name, style);
		}catch(RuntimeException e){
			throw new RuntimeException("Error parsing style \"" + name + "\"!", e);
		}
	}
}
