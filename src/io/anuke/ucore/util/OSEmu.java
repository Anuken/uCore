package io.anuke.ucore.util;

import com.badlogic.gdx.utils.reflect.ClassReflection;

/**Class for getting system properties. Do not use directly.
 * Only for GWT emulation.*/
class OSEmu {

    public static String getProperty(String name){
        try{
            return (String) ClassReflection.getMethod(System.class, "getProperty", String.class).invoke(null, name);
        }catch(Throwable e){
            return null;
        }
    }

    public static String getEnv(String name){
        return null;
    }

}
