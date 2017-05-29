package io.anuke.ucore.scene.builders;
import com.badlogic.gdx.graphics.Color;

import io.anuke.ucore.scene.ui.Label;
import io.anuke.ucore.scene.utils.function.StringSupplier;

public class label extends builder<label, Label>{
	
	public label(String text){
		element = new Label(text);
		cell = context().add(element);
	}
	
	public label(StringSupplier prov){
		this("");
		update(l->{
			l.setText(prov.get());
		});
	}
	
	public label update(LabelUpdater run){
		element.update(()->{
			run.accept(element);
		});
		return this;
	}
	
	public label color(Color color){
		element.setColor(color);
		return this;
	}
	
	public interface LabelUpdater{
		public void accept(Label label);
	}
}
