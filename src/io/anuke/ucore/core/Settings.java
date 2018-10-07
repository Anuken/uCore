package io.anuke.ucore.core;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StreamUtils.OptimizedByteArrayOutputStream;
import io.anuke.ucore.function.Supplier;
import io.anuke.ucore.io.DefaultSerializers;
import io.anuke.ucore.io.ExtendedPreferences;
import io.anuke.ucore.io.ReusableByteArrayInputStream;
import io.anuke.ucore.io.TypeSerializer;
import io.anuke.ucore.io.TypeSerializer.TypeReader;
import io.anuke.ucore.io.TypeSerializer.TypeWriter;
import io.anuke.ucore.util.OS;

import java.io.*;

@SuppressWarnings("unchecked")
public class Settings{
    private static Preferences prefs;
    private static ObjectMap<String, Object> defaults = new ObjectMap<>();
    private static boolean disabled = false;
    private static Runnable errorHandler;

    private static ObjectMap<Class<?>, TypeSerializer<?>> serializers = new ObjectMap<>();
    private static ObjectMap<String, TypeSerializer<?>> serializerNames = new ObjectMap<>();
    private static ObjectMap<Class<?>, String> classNames = new ObjectMap<>();

    private static ByteArrayOutputStream byteStream = new OptimizedByteArrayOutputStream(16);
    private static ReusableByteArrayInputStream byteInputStream = new ReusableByteArrayInputStream();
    private static DataOutputStream dataOutput = new DataOutputStream(byteStream);
    private static DataInputStream dataInput = new DataInputStream(byteInputStream);

    static{
        DefaultSerializers.register();
    }

    public static Preferences prefs(){
        return prefs;
    }

    public static void setErrorHandler(Runnable handler){
        errorHandler = handler;
    }

    public static void load(String appName, String name){
        if(Gdx.app.getType() == ApplicationType.WebGL){
            prefs = Gdx.app.getPreferences(name);
        }else{
            prefs = new ExtendedPreferences(OS.getAppDataDirectory(appName).child(name));
        }
    }

    /** Loads binds as well as prefs. */
    public static void loadAll(String appName, String name){
        load(appName, name);
        KeyBinds.load();
    }

    public static Object getDefault(String name){
        return defaults.get(name);
    }

    public static void put(String name, Object val){
        if(val instanceof Float)
            putFloat(name, (Float) val);
        else if(val instanceof Integer)
            putInt(name, (Integer) val);
        else if(val instanceof String)
            putString(name, (String) val);
        else if(val instanceof Boolean)
            putBool(name, (Boolean) val);
        else if(val instanceof Long)
            putLong(name, (Long) val);
    }

    public static void putString(String name, String val){
        prefs.putString(name, val);
    }

    public static <T> void setSerializer(Class<T> type, TypeWriter<T> writer, TypeReader<T> reader){
        setSerializer(type, new TypeSerializer<T>(){
            @Override
            public void write(DataOutput stream, T object) throws IOException{
                writer.write(stream, object);
            }

            @Override
            public T read(DataInput stream) throws IOException{
                return reader.read(stream);
            }
        });
    }

    public static <T> void setSerializer(Class<T> type, TypeSerializer<T> serializer){
        serializers.put(type, serializer);
        serializerNames.put(classID(type), serializer);
    }

    public static TypeSerializer getSerializer(String name){
        return serializerNames.get(name);
    }

    public static TypeSerializer getSerializer(Class<?> type){
        return serializers.get(type);
    }

    public static String classID(Class<?> type){
        if(classNames.containsKey(type)){
            return classNames.get(type);
        }
        classNames.put(type, type.toString().split("@")[0]);
        return classNames.get(type);
    }

    public static synchronized void putObject(String name, Object value){
        putObject(name, value, value.getClass());
    }

    public static synchronized void putObject(String name, Object value, Class<?> type){
        byteStream.reset();
        if(!serializers.containsKey(type)){
            throw new IllegalArgumentException(type + " does not have a serializer registered!");
        }
        TypeSerializer serializer = serializers.get(type);
        try{
            serializer.write(dataOutput, value);
            putBytes(name, byteStream.toByteArray());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static synchronized void putBytes(String name, byte[] bytes){
        if(prefs instanceof ExtendedPreferences){
            ((ExtendedPreferences) prefs).putBytes(name, bytes);
        }else{
            String str = new String(Base64Coder.encode(bytes));
            putString(name, str);
        }
    }

    public static void clearBytes(String name){
        putBytes(name, new byte[]{});
    }

    public static void putFloat(String name, float val){
        prefs.putFloat(name, val);
    }

    public static void putInt(String name, int val){
        prefs.putInteger(name, val);
    }

    public static void putBool(String name, boolean val){
        prefs.putBoolean(name, val);
    }

    public static void putLong(String name, long val){
        prefs.putLong(name, val);
    }

    public static String getString(String name){
        return prefs.getString(name, (String) def(name));
    }

    public static byte[] getBytes(String name){
        if(prefs instanceof ExtendedPreferences){
            return ((ExtendedPreferences) prefs).getBytes(name);
        }else{
            String str = getString(name, null);
            if(str == null){
                return null;
            }
            return Base64Coder.decode(str);
        }
    }

    public static synchronized <T> T getObject(String name, Class<T> type, Supplier<T> def){
        T t = getObject(name, type);
        return t == null ? def.get() : t;
    }

    private static synchronized <T> T getObject(String name, Class<T> type){
        if(!serializers.containsKey(type)){
            throw new IllegalArgumentException("Type " + type + " does not have a serializer registered!");
        }

        TypeSerializer serializer = serializers.get(type);

        try{
            byteInputStream.setBytes(getBytes(name));
            Object obj = serializer.read(dataInput);
            return (T)obj;
        }catch(Exception e){
            return null;
        }
    }

    public static float getFloat(String name){
        return prefs.getFloat(name, (Float) def(name));
    }

    public static int getInt(String name){
        return prefs.getInteger(name, (Integer) def(name));
    }

    public static boolean getBool(String name){
        return prefs.getBoolean(name, (Boolean) def(name));
    }

    public static long getLong(String name){
        return prefs.getLong(name, (Long) def(name));
    }

    public static String getString(String name, String def){
        return prefs.getString(name, def);
    }

    public static float getFloat(String name, float def){
        return prefs.getFloat(name, def);
    }

    public static int getInt(String name, int def){
        return prefs.getInteger(name, def);
    }

    public static boolean getBool(String name, boolean def){
        return prefs.getBoolean(name, def);
    }

    public static long getLong(String name, long def){
        return prefs.getLong(name, def);
    }

    public static boolean has(String name){
        return prefs.contains(name);
    }

    public static void save(){
        try{
            prefs.flush();
        }catch(GdxRuntimeException e){
            if(errorHandler != null){
                if(!disabled){
                    errorHandler.run();
                }
            }else{
                throw e;
            }

            disabled = true;
        }
    }

    public static Object def(String name){
        if(!defaults.containsKey(name))
            throw new IllegalArgumentException("No setting with name \"" + name + "\" exists!");
        return defaults.get(name);
    }

    /**
     * Set up a bunch of defaults.
     * Format: name1, default1, name2, default2, etc
     */
    public static void defaultList(Object... objects){
        for(int i = 0; i < objects.length; i += 2){
            defaults((String) objects[i], objects[i + 1]);
        }
    }

    /**
     * Sets a default value up.
     * This is REQUIRED for every pref value.
     */
    public static void defaults(String name, Object object){
        defaults.put(name, object);
    }
}
