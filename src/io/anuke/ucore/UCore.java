package io.anuke.ucore;

import java.lang.reflect.Field;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

public class UCore{
	/**The screen pixel density scale. Yes, this is a one letter variable, it's intended to be used a lot.*/
	public static final float s = (Gdx.app == null ? 0 : (Gdx.app.getType() == ApplicationType.Desktop ? 1f : Gdx.graphics.getDensity() / 1.5f));
	
	public static String parseException(Exception e){
		StringBuilder build = new StringBuilder();
		
		build.append(e.getClass().getName() + ": " +e.getMessage());
		
		for(StackTraceElement s : e.getStackTrace()){
			build.append("\n"+s.toString());
		}
		return build.toString();
	}
	
	public static Object getPrivate(Object object, String name){
		try{
			Field field = object.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(object);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
