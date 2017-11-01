package io.anuke.ucore.cui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Json.Serializer;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import io.anuke.ucore.cui.drawables.NinePatchDrawable;
import io.anuke.ucore.cui.drawables.TextureRegionDrawable;
import io.anuke.ucore.cui.style.Style;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.util.Strings;
import io.anuke.ucore.util.Tmp;

public class Stylesheet{
	public static final char typeSeperator = '.';
	public static final char stateSeperator = ':';
	
	private final Json json = new Json();
	private ObjectMap<String, Drawable> drawables = new ObjectMap<>();
	/** Map of style names to actual styles (e.g. "background" -> drawable) */
	private ObjectMap<String, Style> styles = new ObjectMap<>();
	private Array<Style> styleArray = new Array<>();
	private Atlas atlas;

	private ObjectMap<String, Field> styleFieldMap = new ObjectMap<>();
	private Field[] styleFields;
	private ObjectMap<String, StyleProperty> styleProperties = new ObjectMap<>();
	private Style blankStyle = new Style();
	private ObjectMap<Class<?>, Object> defaultPrimitives = new ObjectMap(){{
		put(Integer.class, 0);
		put(Float.class, 0f);
		put(Boolean.class, false);
	}};
	

	public Stylesheet(FileHandle atlasFile, FileHandle file) {
		styleFields = ClassReflection.getFields(Style.class);
		for(Field field : styleFields){
			styleFieldMap.put(field.getName(), field);
		}

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
		styleProperties.put("pad", (value, style)->{
			float pad = value.asFloat();
			style.padLeft = style.padTop = style.padRight = style.padBottom = pad;
		});
		
		styleProperties.put("space", (value, style)->{
			float space = value.asFloat();
			style.spaceLeft = style.spaceTop = style.spaceRight = style.spaceBottom = space;
		});
		
		styleProperties.put("border", (value, style)->{
			String[] out = value.asString().split(" ");
			if(out.length == 2){
				style.borderColor = parseColor(out[0]);
				style.borderThickness = Float.parseFloat(out[1]);
			}else if(out.length == 3){
				style.borderColor = parseColor(out[0]);
				style.borderThickness = Float.parseFloat(out[1]);
				style.borderRadius = Float.parseFloat(out[2]);
			}else{
				throw new RuntimeException("Invalid border property: \"" + value.asString() + "\"");
			}
		});
		
		json.setSerializer(Drawable.class, new Serializer<Drawable>(){
			@Override
			public void write(Json json, Drawable object, Class knownType){}

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
			public void write(Json json, Color object, Class knownType){}

			@Override
			public Color read(Json json, JsonValue jsonData, Class type){
				String name = jsonData.asString();

				return parseColor(name);
			}
		});

		json.setSerializer(Style.class, new Serializer<Style>(){

			@Override
			public void write(Json json, Style object, Class knownType){}

			@Override
			public Style read(Json json, JsonValue jsonData, Class type){
				try{
					Style style = new Style();
					JsonValue child = jsonData.child;
					while(child != null){
						String fieldname = child.name;
						
						if(fieldname.indexOf('-') != -1){
							fieldname = Strings.kebabToCamel(fieldname);
						}
						
						if(!styleProperties.containsKey(fieldname)){
							Field field = styleFieldMap.get(fieldname);
							if(field == null){
								throw new RuntimeException("No field found with name \"" + fieldname + "\"!");
							}
							Object obj = json.fromJson(field.getType(), child.toString().substring(child.name.length() + 1));
							field.set(style, obj);
						}else{
							styleProperties.get(fieldname).parseProperty(child, style);
						}
						child = child.next;
					}
					return style;
				}catch(ReflectionException e){
					throw new RuntimeException(e);
				}
			}

		});
	}
	
	private Color parseColor(String name){
		if(name.startsWith("#")){
			return Color.valueOf(name);
		}else{
			if(!Colors.getColors().containsKey(name))
				throw new RuntimeException("Color not found: \"" + name + "\"!");
			return Colors.get(name).cpy();
		}
	}

	public void getStyle(Section section, Style style, Style finalStyle, Array<String> extraStyles){

		try{
			clearStyle(style);
			
			Enum<?>[] states = section instanceof Stateful ? ((Stateful)section).stateValues() : null;
			
			//check for possible matches before going to the actual style classes
			for(Style checkStyle : styleArray){
				if(checkApplyStyle(section, checkStyle, null)){
					applyStyle(style, checkStyle);
				}
			}
			
			//if there's a state, setup state stuff
			if(section instanceof Stateful){
				//setup state style map if it's null
				if(section.stateStyles == null){
					section.stateStyles = new ObjectMap<>();
					for(Enum<?> state : states){
						section.stateStyles.put(state, new Style());
					}
				}
				
				//go through each state
				for(Enum<?> state : states){
					
					Style stateStyle = section.stateStyles.get(state);
					//clear state style
					clearStyle(stateStyle);
					//for every style, check if it applies, and put it on top
					for(Style current : styleArray){
						if(checkApplyStyle(section, current, state)){
							applyStyle(stateStyle, current);
						}
					}
					
					//make sure there's no nulls
					fixPrimitives(stateStyle);
				}
			}
			
			//apply the section specific styles
			for(String currentName : extraStyles){
				Style current = styles.get(currentName);

				if(current == null){
					throw new RuntimeException("No style found with name: \"" + currentName + "\"");
				}

				applyStyle(style, current);

			}
			
			clearStyle(finalStyle);
			applyStyle(finalStyle, style);
		
			fixPrimitives(finalStyle);
		}catch(ReflectionException e){
			throw new RuntimeException(e);
		}
	}
	
	public void getStateStyle(Section section, Style from, Style finalStyle){
		try{
			//nothing to interpolate here, we're done
			if(section.targetState == null){
				Style to = section.stateStyles.get(section.state);
				applyStyle(finalStyle, to);
			}else{ //else, time to interpolate the values
				Style to = section.stateStyles.get(section.targetState);
				//UCore.log(section.targetState);
				float alpha = section.stateTime / to.transition;
				if(Float.isNaN(alpha)){
					alpha = 1f;
				}
				for(Field field : styleFields){
					if(field.getType() == Color.class){
						Color fromC = (Color)field.get(from);
						Color toC = (Color)field.get(to);
						Color value = (Color)field.get(finalStyle);
						
						if(fromC == null && toC == null){
							continue;
						}
						
						if(value == null){
							field.set(finalStyle, value = new Color());
						}
						
						if(fromC == null){
							fromC = Tmp.c1.set(toC);
							fromC.a = 0f;
						}
						
						if(toC == null){
							toC = Tmp.c1.set(fromC);
							toC.a = 0f;
						}
						
						value.set(fromC).mul(1f-alpha).add(toC.r * alpha, toC.g * alpha, toC.b * alpha, toC.a * alpha);
						
					}else if(field.getType() == Float.class){
						Float fromF = (Float)field.get(from);
						Float toF = (Float)field.get(to);
						
						if(fromF == null || toF == null){
							continue;
						}
						
						field.set(finalStyle, MathUtils.lerp(fromF, toF, alpha));
					}
				}
			}
		
		}catch(ReflectionException e){
			throw new RuntimeException(e);
		}
	}
	
	//set all primitive values to 0/false
	private void fixPrimitives(Style style) throws ReflectionException{
		for(Field field : styleFields){
			if(defaultPrimitives.containsKey(field.getType()) &&
					field.get(style) == null){
				field.set(style, defaultPrimitives.get(field.getType()));
			}
		}
	}
	
	private void clearStyle(Style style) throws ReflectionException{
		for(Field field : styleFields){
			Object value = field.get(blankStyle);
			
			if(!(value instanceof Color)){
				field.set(style, value);
			}else{
				((Color)field.get(style)).set((Color)value);
			}
		}
	}

	private void applyStyle(Style style, Style topStyle) throws ReflectionException{
		for(Field field : styleFields){
			Object value = field.get(topStyle);
			
			if(value != null){
				if(!(value instanceof Color)){
					field.set(style, value);
				}else{
					((Color)field.get(style)).set((Color)value);
				}
			}
		}
	}

	private boolean checkApplyStyle(Section section, Style style, Enum<?> state){
		Section current = section;
		
		if(state != null && !state.name().equals(style.stateName)){
			return false;
		}else if(state == null && style.stateName != null){
			return false;
		}
		
		for(int i = style.typeNames.length - 1; i >= 0; i--){
			if(current == null){
				return false;
			}
			if(!current.getTypeName().equals(style.typeNames[i])){
				return false;
			}
			current = section.parent;
		}
		return true;
	}

	private void processStyle(JsonValue value){
		String name = value.name;
		String str = value.toString().substring(name.length() + 1);
		try{
			Style style = json.fromJson(Style.class, str);
			String tname = null;
			
			if(name.indexOf(stateSeperator) != -1){
				String[] split = name.split(stateSeperator + "");
				style.name = name;
				tname = split[0];
				style.stateName = split[1];
			}else{
				style.name = name;
				tname = name;
			}
			
			style.typeNames = tname.indexOf(typeSeperator) != -1 ? new String[] { tname } : tname.split("\\" + typeSeperator);
			
			styles.put(style.name, style);
			styleArray.add(style);
		}catch(RuntimeException e){
			throw new RuntimeException("Error parsing style \"" + name + "\"!", e);
		}
	}
	
	interface StyleProperty{
		void parseProperty(JsonValue value, Style style);
	}
}
