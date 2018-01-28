package io.anuke.ucore;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class UCore{
	private static Logger logger;
	public static final boolean isAssets =
			Gdx.app != null && Gdx.app.getType() != ApplicationType.WebGL
					&& getProperty("user.name").equals("anuke")
					&& getAbsolute(Gdx.files.local("na").parent()).endsWith("assets");

	public static String getProperty(String name){
		try{
			Method method = ClassReflection.getMethod(System.class, "getProperty", String.class);
			return (String)method.invoke(null, name);
		}catch(Exception e){
			return null;
		}
	}

	public static String getPropertyNotNull(String name){
		String s = getProperty(name);
		return s == null ? "" : s;
	}

	public static String getAbsolute(FileHandle file){
		try{
			Method method = ClassReflection.getMethod(file.getClass(), "file");
			Object object = method.invoke(file);
			Method fm = ClassReflection.getMethod(object.getClass(), "getAbsolutePath");
			return (String)fm.invoke(object);
		}catch(ReflectionException e){
			throw new RuntimeException(e);
		}
	}
	
	public static Object getPrivate(Object object, String name){
		try{
			Field field = ClassReflection.getDeclaredField(object.getClass(), name);
			field.setAccessible(true);
			return field.get(object);
		}catch(ReflectionException e){
            throw new RuntimeException(e);
		}
	}

	public static void setPrivate(Object object, String name, Object value){
		try{
			Field field = ClassReflection.getDeclaredField(object.getClass(), name);
			field.setAccessible(true);
			field.set(object, value);
		}catch(ReflectionException e){
			throw new RuntimeException(e);
		}
	}
}
