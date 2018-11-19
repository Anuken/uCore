package io.anuke.ucore.io;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.ObjectSet;
import io.anuke.ucore.core.Settings;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DefaultSerializers{

    public static void register(){
        Settings.setSerializer(IntArray.class, new TypeSerializer<IntArray>(){
            @Override
            public void write(DataOutput stream, IntArray object) throws IOException{
                stream.writeInt(object.size);
                for(int i = 0; i < object.size; i++){
                    stream.writeInt(object.get(i));
                }
            }

            @Override
            public IntArray read(DataInput stream) throws IOException{
                int size = stream.readInt();
                IntArray a = new IntArray(size);
                for(int i = 0; i < size; i++){
                    a.add(stream.readInt());
                }
                return a;
            }
        });

        Settings.setSerializer(String.class, new TypeSerializer<String>(){
            @Override
            public void write(DataOutput stream, String object) throws IOException{
                stream.writeUTF(object == null ? "" : object);
            }

            @Override
            public String read(DataInput stream) throws IOException{
                return stream.readUTF();
            }
        });

        Settings.setSerializer(Array.class, new TypeSerializer<Array>(){
            @Override
            public void write(DataOutput stream, Array object) throws IOException{
                stream.writeInt(object.size);
                if(object.size != 0){
                    TypeSerializer ser = Settings.getSerializer(object.get(0).getClass());
                    if(ser == null) throw new IllegalArgumentException(object.get(0).getClass() + " does not have a serializer registered!");

                    stream.writeUTF(Settings.classID(object.get(0).getClass()));

                    for(Object element : object){
                        ser.write(stream, element);
                    }
                }
            }

            @Override
            public Array read(DataInput stream) throws IOException{
                int size = stream.readInt();
                Array arr = new Array(size);

                if(size == 0) return arr;

                String type = stream.readUTF();

                TypeSerializer ser = Settings.getSerializer(type);
                if(ser == null) throw new IllegalArgumentException(type + " does not have a serializer registered!");


                for(int i = 0; i < size; i++){
                    arr.add(ser.read(stream));
                }

                return arr;
            }
        });

        Settings.setSerializer(ObjectSet.class, new TypeSerializer<ObjectSet>(){
            @Override
            public void write(DataOutput stream, ObjectSet object) throws IOException{
                stream.writeInt(object.size);
                if(object.size != 0){
                    TypeSerializer ser = Settings.getSerializer(object.first().getClass());
                    if(ser == null) throw new IllegalArgumentException(object.first().getClass() + " does not have a serializer registered!");

                    stream.writeUTF(Settings.classID(object.first().getClass()));

                    for(Object element : object){
                        ser.write(stream, element);
                    }
                }
            }

            @Override
            public ObjectSet read(DataInput stream) throws IOException{
                int size = stream.readInt();
                ObjectSet arr = new ObjectSet();

                if(size == 0) return arr;

                String type = stream.readUTF();

                TypeSerializer ser = Settings.getSerializer(type);
                if(ser == null) throw new IllegalArgumentException(type + " does not have a serializer registered!");

                for(int i = 0; i < size; i++){
                    arr.add(ser.read(stream));
                }

                return arr;
            }
        });

        Settings.setSerializer(ObjectMap.class, new TypeSerializer<ObjectMap>(){
            @Override
            public void write(DataOutput stream, ObjectMap map) throws IOException{
                stream.writeInt(map.size);
                if(map.size == 0) return;
                Entry entry = map.entries().next();

                TypeSerializer keyser = Settings.getSerializer(Settings.classID(entry.key.getClass()));
                TypeSerializer valser = Settings.getSerializer(Settings.classID(entry.value.getClass()));
                if(keyser == null) throw new IllegalArgumentException(entry.key.getClass() + " does not have a serializer registered!");
                if(valser == null) throw new IllegalArgumentException(entry.value.getClass() + " does not have a serializer registered!");

                stream.writeUTF(Settings.classID(entry.key.getClass()));
                stream.writeUTF(Settings.classID(entry.value.getClass()));

                for(Object e : map.entries()){
                    Entry en = (Entry)e;
                    keyser.write(stream, en.key);
                    valser.write(stream, en.value);
                }
            }

            @Override
            public ObjectMap read(DataInput stream) throws IOException{
                int size = stream.readInt();
                ObjectMap map = new ObjectMap();
                if(size == 0) return map;

                String keyt = stream.readUTF();
                String valt = stream.readUTF();

                TypeSerializer keyser = Settings.getSerializer(keyt);
                TypeSerializer valser = Settings.getSerializer(valt);
                if(keyser == null) throw new IllegalArgumentException(keyt + " does not have a serializer registered!");
                if(valser == null) throw new IllegalArgumentException(valt + " does not have a serializer registered!");

                for(int i = 0; i < size; i++){
                    Object key = keyser.read(stream);
                    Object val = valser.read(stream);
                    map.put(key, val);
                }

                return map;
            }
        });
    }
}
