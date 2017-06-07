package io.anuke.ucore.cui;

import com.badlogic.gdx.utils.Array;

public class Canvas{
	private static Canvas instance;
	
	private Array<Layout> layouts = new Array<>();
	private Stylesheet styles;
	
	public Canvas(Stylesheet styles){
		this.styles = styles;
		instance = this;
	}
	
	public void addLayout(Layout layout){
		layouts.add(layout);
	}
	
	public Stylesheet getStylesheet(){
		return styles;
	}
	
	public static Canvas instance(){
		return instance;
	}
}
