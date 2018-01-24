package io.anuke.ucore.layer3D;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**Prototype renderer. Splits object layers into an array, so no sorting is required.
 * This is quite rigid, however, and at the moment layer Z values are ignored.
 * Used for static things.
 * @author Anuken*/
public class ArrayRenderer implements LayerRenderer{
	private static final int MAX_LAYERS = 100;
	private static ArrayRenderer instance;
	private static final float e = 0.001f;

	public OrthographicCamera camera;
	public float spacing = 1f;
	public int steps = 1;
	public float camrotation = 0f;
	
	private int toplayer = 0;
	private Array<TextureLayer>[] layers = new Array[MAX_LAYERS];

	public ArrayRenderer() {
		for(int i = 0; i < layers.length; i++){
			layers[i] = new Array<TextureLayer>();
		}
	}

	public static ArrayRenderer instance(){
		if(instance == null)
			instance = new ArrayRenderer();
		return instance;
	}

	public void render(Batch batch){
		for(int l = 0; l < toplayer; l++){
			for(TextureLayer layer : layers[l]){
				float x = 0, y = layer.object.z + l * spacing;
				float rotation = layer.object.rotation + camrotation;
				TextureRegion region = layer.region;

				float oy = layer.object.y;
				float ox = layer.object.x;
				ox -= camera.position.x;
				oy -= camera.position.y;

				float cos = (float) Math.cos(camrotation * MathUtils.degRad);
				float sin = (float) Math.sin(camrotation * MathUtils.degRad);

				float newX = ox * cos - oy * sin;
				float newY = ox * sin + oy * cos;

				ox = newX;
				oy = newY;

				x += ox + camera.position.x;
				y += oy + camera.position.y;

				for(int i = 0; i <= steps; i++){
					batch.setColor(layer.object.color);
					batch.draw(region, x - region.getRegionWidth() / 2 - e, y - region.getRegionHeight() / 2 - e, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth() + e * 2, region.getRegionHeight() + e * 2, 1, 1, rotation);
					y += spacing / steps;
				}
			}
		}
	}
	
	@Override
	public void add(LayeredObject object){
		for(int i = object.offset; i < object.regions.length; i++){
			layers[i].add(new TextureLayer(object, object.regions[i]));
		}

		if(object.regions.length > toplayer){
			toplayer = object.regions.length;
		}
	}
	
	@Override
	public void remove(LayeredObject object){
		for(int i = toplayer; i >= 0; i--){
			Array<TextureLayer> array = layers[i];

			for(int j = object.offset; j < layers[i].size; j++){

				if(array.get(j).object == object){

					array.removeIndex(j);

					if(array.size == 0 && i >= toplayer){
						toplayer--;
					}

					break;
				}
			}
		}
	}

	class TextureLayer{
		float x, y;
		LayeredObject object;
		TextureRegion region;

		public TextureLayer(LayeredObject object, TextureRegion region) {
			this.object = object;
			this.x = object.x;
			this.y = object.y;
			this.region = region;
		}
	}

}
