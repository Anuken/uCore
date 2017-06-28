package io.anuke.ucore.scene.ui;

import io.anuke.ucore.function.Listenable;

public class ConfirmDialog extends Dialog{
	Listenable confirm;

	public ConfirmDialog(String title, String text, Listenable confirm) {
		super(title, "dialog");
		this.confirm = confirm;
		text(text);
		button("Ok", true);
		button("Cancel", false);
	}
	
	@Override
	protected void result(Object object){
		if(object == Boolean.TRUE){
			hide();
			confirm.listen();
		}
	}

}
