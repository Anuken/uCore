package com.badlogic.gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class PixmapIO {
    public static void writePNG(FileHandle file, Pixmap pixmap){
        throw new GdxRuntimeException("Writing pixmaps not supported in GWT backend");
    }
}
