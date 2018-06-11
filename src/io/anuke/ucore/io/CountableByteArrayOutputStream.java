package io.anuke.ucore.io;

import java.io.ByteArrayOutputStream;

public class CountableByteArrayOutputStream extends ByteArrayOutputStream{

    public int position(){
        return count;
    }
}
