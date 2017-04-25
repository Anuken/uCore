package io.anuke.ucore.scene.ui;

public class PrefsDialog extends Dialog{

	public PrefsDialog(String title) {
		super(title);
	}
	
	void addSlider(String name, String title, int def, int min, int max,StringProcessor s){
		addSlider(name, title, def, min, max, 1, s);
	}
	
	void addSlider(String name, String title, int def, int min, int max, int step, StringProcessor s){
		/*
		
		String parsed = name.toLowerCase().replace(" ", "");
		Table table = getContentTable();
		Slider slider = new Slider(min, max, 1f, false);
		
		slider.setValue(prefs.getInteger(parsed, (int)def));
		
		Label label = new Label(name);
		slider.changed(()->{
			label.setText(name + ": " + s.get((int)slider.getValue()));
			prefs.putInteger(parsed, (int)slider.getValue());
			prefs.flush();
		});
		slider.change();
		table.add(label).minWidth(330);
		table.add(slider);
		table.addButton("Reset", ()->{
			slider.setValue(def);
			slider.change();
		});
		table.row();
		*/
	}
	
	interface StringProcessor{
		String get(int i);
	}

}
