package io.anuke.ucore.util;

import com.badlogic.gdx.utils.Bits;

public class ByteSet{
    private Bits bits = new Bits(256);

    public boolean contains(byte b){
        return bits.get(u(b));
    }

    public void add(byte b){
        bits.set(u(b));
    }

    public void remove(byte b){
        bits.clear(u(b));
    }

    private int u(byte b){
        return b < 0 ? b + 255 : b;
    }
}
