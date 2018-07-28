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
import io.anuke.ucore.util.Mathf;

public class SkinAlias{
    public static final ObjectMap<String, Class<?>> aliases = Mathf.map(
        "Font", BitmapFont.class,
        "Color", Color.class,
        "TintedDrawable", TintedDrawable.class,

        "ButtonStyle", ButtonStyle.class,
        "TextButtonStyle", TextButtonStyle.class,
        "ImageButtonStyle", ImageButtonStyle.class,
        "ScrollPaneStyle", ScrollPaneStyle.class,
        "WindowStyle", WindowStyle.class,
        "KeybindDialogStyle", KeybindDialogStyle.class,
        "SliderStyle", SliderStyle.class,
        "LabelStyle", LabelStyle.class,
        "TextFieldStyle", TextFieldStyle.class,
        "CheckBoxStyle", CheckBoxStyle.class,
        "ListStyle", ListStyle.class
    );
}
