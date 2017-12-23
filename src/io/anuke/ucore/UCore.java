package io.anuke.ucore;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

public class UCore{
	
	public static void log(Object...objects){
		int i = 0;
		for(Object o : objects){
			System.out.print(o);
			if(i++ != objects.length-1)
				System.out.print(", ");
		}
		System.out.println();
	}
	
	public static Object getPrivate(Object object, String name){
		try{
			Field field = ClassReflection.getDeclaredField(object.getClass(), name);
			field.setAccessible(true);
			return field.get(object);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
