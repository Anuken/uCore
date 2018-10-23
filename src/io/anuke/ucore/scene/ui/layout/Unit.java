package io.anuke.ucore.scene.ui.layout;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import io.anuke.ucore.util.Mathf;

public enum Unit {
    px {
        @Override
        public float scl(float amount) {
            return amount;
        }
    },
    dp {
        float scl = -1;

        @Override
        public float scl(float amount) {
            if (scl < 0f) {
                //calculate scaling value if it hasn't been set yet
                if (Gdx.app.getType() == ApplicationType.Desktop) {
                    scl = 1f * product;
                } else if (Gdx.app.getType() == ApplicationType.WebGL) {
                    scl = 1f;
                } else {
                    //mobile scaling
                    scl = Math.max(Mathf.round2(Gdx.graphics.getDensity() / 1.5f + addition, 0.5f), 1f) * product;
                }
            }
            return amount * scl;
        }
    };
    public float addition = 0f;
    public float product = 1f;

    public abstract float scl(float amount);
}
