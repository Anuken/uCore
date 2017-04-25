package io.anuke.ucore.scene;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import io.anuke.ucore.scene.event.EventListener;
import io.anuke.ucore.scene.event.InputEvent;
import io.anuke.ucore.scene.utils.ChangeListener;
import io.anuke.ucore.scene.utils.ClickListener;

/**Extends the BaseElement (Actor) class to provide more functionality.
 * (that is probably a terrible idea)*/
public class Element extends BaseElement{
	private static final Vector2 vec = new Vector2();
	
	protected float alpha = 1f;
	private BooleanSupplier visibility;
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		validate();
		draw();
	}
	
	/**Simple drawing, using Draw#rect().*/
	public void draw(){
		
	}
	
	@Override
	public void act (float delta) {
		super.act(delta);
		if(visibility != null)
			setVisible(visibility.getAsBoolean());
	}
	
	public Vector2 worldPos(){
		return localToStageCoordinates(vec.set(0, 0));
	}
	
	/**Adds a click listener.*/
	public ClickListener clicked(Runnable r){
		ClickListener click;
		addListener(click = new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				r.run();
			}
		});
		return click;
	}
	
	public void change(){
		for(EventListener listener : this.getListeners()){
			if(listener instanceof ChangeListener)
				((ChangeListener)listener).changed(null, this);
		}
	}
	
	/**Adds a click listener.*/
	public void changed(Runnable r){
		addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Element actor){
				r.run();
			}
		});
	}
	
	public void setVisible(BooleanSupplier vis){
		visibility = vis;
	}
}
