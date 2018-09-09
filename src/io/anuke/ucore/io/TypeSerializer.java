package io.anuke.ucore.io;

import java.io.*;

public interface TypeSerializer<T>{
    void write(DataOutput stream, T object) throws IOException;
    T read(DataInput stream) throws IOException;
}
