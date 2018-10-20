package io.anuke.ucore.io;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A binary variant of a preferences file.
 * Faster and uses less space/memory than the default desktop preferences class.
 * This class is thread-safe.
 */
public class ExtendedPreferences implements Preferences{
    private final static byte TYPE_BOOL = 0;
    private final static byte TYPE_INT = 1;
    private final static byte TYPE_LONG = 2;
    private final static byte TYPE_FLOAT = 3;
    private final static byte TYPE_STRING = 4;
    private final static byte TYPE_BINARY = 5;

    private final FileHandle file;
    private final Map<String, Object> values;

    /**
     * Reads a binary preference file. <br><br>
     * Format: <br>
     * amount of values [int]<br>
     * for each value: <br>
     * - type of value [byte] <br>
     * - value name [UTF string] <br>
     * - value [size depends on type] <br>
     */
    public ExtendedPreferences(FileHandle file){
        this.file = file;
        this.values = new HashMap<>();
        if(!file.exists()) return;

        try(DataInputStream stream = new DataInputStream(file.read())){
            int amount = stream.readInt();
            for(int i = 0; i < amount; i++){
                String key = stream.readUTF();
                byte type = stream.readByte();

                switch(type){
                    case TYPE_BOOL:
                        values.put(key, stream.readBoolean());
                        break;
                    case TYPE_INT:
                        values.put(key, stream.readInt());
                        break;
                    case TYPE_LONG:
                        values.put(key, stream.readLong());
                        break;
                    case TYPE_FLOAT:
                        values.put(key, stream.readFloat());
                        break;
                    case TYPE_STRING:
                        values.put(key, stream.readUTF());
                        break;
                    case TYPE_BINARY:
                        int length = stream.readInt();
                        byte[] bytes = new byte[length];
                        stream.read(bytes);
                        values.put(key, bytes);
                        break;
                }
            }
        }catch(IOException e){
            new RuntimeException("Error reading preferences: " + file, e).printStackTrace();
        }
    }

    @Override
    public synchronized Preferences putBoolean(String key, boolean val){
        values.put(key, val);
        return this;
    }

    @Override
    public synchronized Preferences putInteger(String key, int val){
        values.put(key, val);
        return this;
    }

    @Override
    public synchronized Preferences putLong(String key, long val){
        values.put(key, val);
        return this;
    }

    @Override
    public synchronized Preferences putFloat(String key, float val){
        values.put(key, val);
        return this;
    }

    @Override
    public synchronized Preferences putString(String key, String val){
        values.put(key, val);
        return this;
    }

    public synchronized Preferences putBytes(String key, byte[] val){
        values.put(key, val);
        return this;
    }

    @Override
    public synchronized Preferences put(Map<String, ?> vals){
        values.putAll(vals);
        return this;
    }

    public byte[] getBytes(String key){
        return getBytes(key, null);
    }

    @Override
    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    @Override
    public int getInteger(String key){
        return getInteger(key, 0);
    }

    @Override
    public long getLong(String key){
        return getLong(key, 0);
    }

    @Override
    public float getFloat(String key){
        return getFloat(key, 0);
    }

    @Override
    public String getString(String key){
        return getString(key, "");
    }

    public synchronized byte[] getBytes(String key, byte[] defValue){
        if(values.containsKey(key)){
            return (byte[]) values.get(key);
        }
        return defValue;
    }

    @Override
    public synchronized boolean getBoolean(String key, boolean defValue){
        if(values.containsKey(key)){
            return (boolean) values.get(key);
        }
        return defValue;
    }

    @Override
    public synchronized int getInteger(String key, int defValue){
        if(values.containsKey(key)){
            return (int) values.get(key);
        }
        return defValue;
    }

    @Override
    public synchronized long getLong(String key, long defValue){
        if(values.containsKey(key)){
            return (long) values.get(key);
        }
        return defValue;
    }

    @Override
    public synchronized float getFloat(String key, float defValue){
        if(values.containsKey(key)){
            return (float) values.get(key);
        }
        return defValue;
    }

    @Override
    public synchronized String getString(String key, String defValue){
        if(values.containsKey(key)){
            return (String) values.get(key);
        }
        return defValue;
    }

    @Override
    public synchronized Map<String, ?> get(){
        return values;
    }

    @Override
    public synchronized boolean contains(String key){
        return values.containsKey(key);
    }

    @Override
    public synchronized void clear(){
        values.clear();
    }

    @Override
    public synchronized void remove(String key){
        values.remove(key);
    }

    @Override
    public synchronized void flush(){
        try(DataOutputStream stream = new DataOutputStream(file.write(false))){
            stream.writeInt(values.size());

            for(Entry<String, Object> entry : values.entrySet()){
                stream.writeUTF(entry.getKey());

                Object value = entry.getValue();

                if(value instanceof Boolean){
                    stream.writeByte(TYPE_BOOL);
                    stream.writeBoolean((Boolean) value);
                }else if(value instanceof Integer){
                    stream.writeByte(TYPE_INT);
                    stream.writeInt((Integer) value);
                }else if(value instanceof Long){
                    stream.writeByte(TYPE_LONG);
                    stream.writeLong((Long) value);
                }else if(value instanceof Float){
                    stream.writeByte(TYPE_FLOAT);
                    stream.writeFloat((Float) value);
                }else if(value instanceof String){
                    stream.writeByte(TYPE_STRING);
                    stream.writeUTF((String) value);
                }else if(value instanceof byte[]){
                    stream.writeByte(TYPE_BINARY);
                    stream.writeInt(((byte[]) value).length);
                    stream.write((byte[]) value);
                }
            }
        }catch(IOException e){
            throw new RuntimeException("Error writing preferences: " + file, e);
        }
    }
}
