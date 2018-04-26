package io.anuke.ucore.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ObjectMap;
import io.anuke.ucore.scene.Skin.TintedDrawable;
import io.anuke.ucore.scene.ui.Button.ButtonStyle;
import io.anuke.ucore.scene.ui.CheckBox.CheckBoxStyle;
import io.anuke.ucore.scene.ui.ImageButton.ImageButtonStyle;
import io.anuke.ucore.scene.ui.KeybindDialog.KeybindDialogStyle;
import io.anuke.ucore.scene.ui.Label.LabelStyle;
import io.anuke.ucore.scene.ui.List.ListStyle;
import io.anuke.ucore.scene.ui.ScrollPane.ScrollPaneStyle;
import io.anuke.ucore.scene.ui.Slider.SliderStyle;
import io.anuke.ucore.scene.ui.TextButton.TextButtonStyle;
import io.anuke.ucore.scene.ui.TextField.TextFieldStyle;
import io.anuke.ucore.scene.ui.Window.WindowStyle;

public class SkinAlias {
    public static final ObjectMap<String, Class<?>> aliases = new ObjectMap<String, Class<?>>(){{
        put("font", BitmapFont.class);
        put("color", Color.class);
        put("tintedDrawable", TintedDrawable.class);

        put("buttonStyle", ButtonStyle.class);
        put("textButtonStyle", TextButtonStyle.class);
        put("imageButtonStyle", ImageButtonStyle.class);
        put("scrollPaneStyle", ScrollPaneStyle.class);
        put("windowStyle", WindowStyle.class);
        put("keybindDialogStyle", KeybindDialogStyle.class);
        put("sliderStyle", SliderStyle.class);
        put("labelStyle", LabelStyle.class);
        put("textFieldStyle", TextFieldStyle.class);
        put("checkBoxStyle", CheckBoxStyle.class);
        put("listStyle", ListStyle.class);
    }};
}
