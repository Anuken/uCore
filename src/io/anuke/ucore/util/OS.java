package io.anuke.ucore.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;

public class OS {
    static public boolean isWindows = getPropertyNotNull("os.name").contains("Windows");
    static public boolean isLinux = getPropertyNotNull("os.name").contains("Linux");
    static public boolean isMac = getPropertyNotNull("os.name").contains("Mac");
    static public boolean isIos = false;
    static public boolean isAndroid = false;
    static public boolean isARM = getPropertyNotNull("os.arch").startsWith("arm");
    static public boolean is64Bit = getPropertyNotNull("os.arch").equals("amd64")
            || getPropertyNotNull("os.arch").equals("x86_64");

    // JDK 8 only.
    static public String abi = (getPropertyNotNull("sun.arch.abi") != null ? getPropertyNotNull("sun.arch.abi") : "");

    static {
        boolean isMOEiOS = "iOS".equals(getPropertyNotNull("moe.platform.name"));
        String vm = getPropertyNotNull("java.runtime.name");
        if (vm != null && vm.contains("Android Runtime")) {
            isAndroid = true;
            isWindows = false;
            isLinux = false;
            isMac = false;
            is64Bit = false;
        }
        if (isMOEiOS || (!isAndroid && !isWindows && !isLinux && !isMac)) {
            isIos = true;
            isAndroid = false;
            isWindows = false;
            isLinux = false;
            isMac = false;
            is64Bit = false;
        }
    }

    public static FileHandle getAppDataDirectory(String appname){
        if(OS.isWindows){
            return Gdx.files.absolute(System.getenv("AppData")).child(appname + "/");
        }else if(OS.isLinux){
            return Gdx.files.absolute(getProperty("user.home")).child("." + appname.toLowerCase() + "/");
        }else if(OS.isMac){
            return Gdx.files.absolute(getProperty("user.home") + "/Library/Application Support/").child(appname + "/");
        }else if(isIos || isAndroid){
            return Gdx.files.local("");
        }else{ //else, probably GWT
            return null;
        }
    }

    public static String getProperty(String name){
        try{
            Method method = ClassReflection.getMethod(System.class, "getProperty", String.class);
            return (String)method.invoke(null, name);
        }catch(Exception e){
            return null;
        }
    }

    public static String getEnv(String name){
        try{
            Method method = ClassReflection.getMethod(System.class, "getenv", String.class);
            return (String)method.invoke(null, name);
        }catch(Exception e){
            return null;
        }
    }

    public static String getPropertyNotNull(String name){
        String s = getProperty(name);
        return s == null ? "" : s;
    }
}
