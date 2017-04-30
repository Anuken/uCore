package io.anuke.ucore.scene.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;

public class Cursors{
	public static Cursor arrow;
	public static Cursor ibeam;
	public static Cursor hand;
	
	public static void setIbeam(){
		if(ibeam != null)
			Gdx.graphics.setCursor(ibeam);
		else
			Gdx.graphics.setSystemCursor(SystemCursor.Ibeam);
	}
	
	public static void setHand(){
		if(hand != null)
			Gdx.graphics.setCursor(hand);
		else
			Gdx.graphics.setSystemCursor(SystemCursor.Hand);
	}
	
	public static void restoreCursor(){
		if(arrow != null){
			Gdx.graphics.setCursor(arrow);
		}else{
			Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
		}
	}
}
