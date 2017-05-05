package io.anuke.ucore.scene.ui;

public class ConfirmDialog extends Dialog{
	Runnable confirm;

	public ConfirmDialog(String title, String text, Runnable confirm) {
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
			confirm.run();
		}
	}

}
