package io.anuke.ucore.lights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.lights.shaders.LightShader;
import io.anuke.ucore.lights.shaders.PixelShader;
import io.anuke.ucore.noise.Noise;
import io.anuke.ucore.util.Physics;
import io.anuke.ucore.util.RectQuadTree;

/**
 * Handler that manages everything related to lights updating and rendering
 * <p>Implements {@link Disposable}
 *
 * @author kalle_h
 */
public class RayHandler implements Disposable{

    /**
     * Gamma correction value used if enabled
     * TODO: remove final modifier and provide method to change
     * this default value if needed to anyone?
     */
    static final float GAMMA_COR = 0.625f;
    /**
     * if this is public why we have a setter?
     * TODO: remove public modifier and add getter
     */
    public static boolean isDiffuse = true;
    static Vector2 vector = new Vector2();
    static boolean gammaCorrection = false;
    static float gammaCorrectionParameter = 1f;
    /**
     * Blend function for lights rendering with both shadows and diffusion
     * <p>Default: (GL20.GL_DST_COLOR, GL20.GL_ZERO)
     */
    public final BlendFunc diffuseBlendFunc =
            new BlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO);
    /**
     * Blend function for lights rendering with shadows but without diffusion
     * <p>Default: (GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA)
     */
    public final BlendFunc shadowBlendFunc =
            new BlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
    /**
     * Blend function for lights rendering without shadows and diffusion
     * <p>Default: (GL20.GL_SRC_ALPHA, GL20.GL_ONE)
     */
    public final BlendFunc simpleBlendFunc =
            new BlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
    final Matrix4 combined = new Matrix4();
    final Color ambientLight = new Color();
    /**
     * This Array contain all the lights.
     * <p>
     * <p>NOTE: DO NOT MODIFY THIS LIST
     */
    final Array<Light> lightList = new Array<>(false, 16);
    /**
     * This Array contain all the disabled lights.
     * <p>
     * <p>NOTE: DO NOT MODIFY THIS LIST
     */
    final Array<Light> disabledLights = new Array<>(false, 16);
    final ShaderProgram lightShader;
    private final Rectangle r1 = new Rectangle();
    private final Rectangle r2 = new Rectangle();
    LightMap lightMap;
    ShaderProgram customLightShader = null;
    ShaderProgram customDiffuseShader = null;

    Color tintColor = Color.WHITE;

    boolean culling = true;
    boolean shadows = true;
    boolean blur = true;

    int blurNum = 1;

    boolean customViewport = false;
    int viewportX = 0;
    int viewportY = 0;
    int viewportWidth = Gdx.graphics.getWidth();
    int viewportHeight = Gdx.graphics.getHeight();

    /** How many lights passed culling and rendered to scene last time */
    int lightRenderedLastFrame = 0;

    /** camera matrix corners */
    float x1, x2, y1, y2;

    Array<Rectangle> rectangles = new Array<>();
    RectQuadTree tree = new RectQuadTree(4, new Rectangle(-1000, -1000, 2000, 2000));
    Array<Rectangle> tmprects = new Array<>();
    private float cdist = -1f;

    /**
     * Class constructor specifying the physics world from where collision
     * geometry is taken.
     * <p>
     * <p>NOTE: FBO size is 1/4 * screen size and used by default.
     * <p>
     * <ul>Default setting are:
     * <li>culling = true
     * <li>shadows = true
     * <li>diffuse = false
     * <li>blur = true
     * <li>blurNum = 1
     * <li>ambientLight = 0f
     * </ul>
     */
    public RayHandler(){
        this(Gdx.graphics.getWidth() / 4, Gdx.graphics
                .getHeight() / 4);
    }

    /**
     * Class constructor specifying the physics world from where collision
     * geometry is taken, and size of FBO used for intermediate rendering.
     */
    public RayHandler(int fboWidth, int fboHeight){

        resizeFBO(fboWidth, fboHeight);
        lightShader = LightShader.createLightShader();
    }

    /**
     * @return if gamma correction is enabled or not
     */
    public static boolean getGammaCorrection(){
        return gammaCorrection;
    }

    /**
     * Enables/disables gamma correction.
     * <p>
     * <p><b>This need to be done before creating instance of rayHandler.</b>
     * <p>
     * <p>NOTE: To match the visuals with gamma uncorrected lights the light
     * distance parameters is modified implicitly.
     */
    public static void setGammaCorrection(boolean gammaCorrectionWanted){
        gammaCorrection = gammaCorrectionWanted;
        gammaCorrectionParameter = gammaCorrection ? GAMMA_COR : 1f;
    }

    /**
     * Enables/disables usage of diffuse algorithm.
     * <p>
     * <p>If set to true lights are blended using the diffuse shader. This is
     * more realistic model than normally used as it preserve colors but might
     * look bit darker and also it might improve performance slightly.
     */
    public static void useDiffuseLight(boolean useDiffuse){
        isDiffuse = useDiffuse;
    }

    /** Sets the internal quadtree's bounds. */
    public void setBounds(float x, float y, float w, float h){
        tree = new RectQuadTree(4, new Rectangle(x, y, w, h));
        updateRects();
    }

    /**
     * Resize the FBO used for intermediate rendering.
     */
    public void resizeFBO(int fboWidth, int fboHeight){
        if(lightMap != null){
            lightMap.dispose();
        }
        lightMap = new LightMap(this, fboWidth, fboHeight);
    }

    /**
     * Sets combined matrix basing on camera position, rotation and zoom
     * <p>
     * <p> Same as calling:
     * {@code setCombinedMatrix(
     * camera.combined,
     * camera.position.x,
     * camera.position.y,
     * camera.viewportWidth * camera.zoom,
     * camera.viewportHeight * camera.zoom );}
     *
     * @see #setCombinedMatrix(Matrix4, float, float, float, float)
     */
    public void setCombinedMatrix(OrthographicCamera camera){
        this.setCombinedMatrix(
                camera.combined,
                camera.position.x,
                camera.position.y,
                camera.viewportWidth * camera.zoom,
                camera.viewportHeight * camera.zoom);
    }

    /**
     * Sets combined camera matrix.
     * <p>
     * <p>Matrix must be set to work in box2d coordinates, it will be copied
     * and used for culling and rendering. Remember to update it if camera
     * changes. This will work with rotated cameras.
     *
     * @param combined matrix that include projection and translation matrices
     * @param x combined matrix position
     * @param y combined matrix position
     * @param viewPortWidth NOTE!! use actual size, remember to multiple with zoom value
     * if pulled from OrthoCamera
     * @param viewPortHeight NOTE!! use actual size, remember to multiple with zoom value
     * if pulled from OrthoCamera
     * @see #setCombinedMatrix(OrthographicCamera)
     */
    public void setCombinedMatrix(Matrix4 combined, float x, float y,
                                  float viewPortWidth, float viewPortHeight){

        System.arraycopy(combined.val, 0, this.combined.val, 0, 16);
        // updateCameraCorners
        final float halfViewPortWidth = viewPortWidth * 0.5f;
        x1 = x - halfViewPortWidth;
        x2 = x + halfViewPortWidth;

        final float halfViewPortHeight = viewPortHeight * 0.5f;
        y1 = y - halfViewPortHeight;
        y2 = y + halfViewPortHeight;
    }

    /**
     * Utility method to check if light is on the screen
     *
     * @param x - light center x-coord
     * @param y - light center y-coord
     * @param radius - maximal light distance
     * @return true if camera screen intersects or contains provided
     * light, represented by circle/box area
     */
    boolean intersect(float x, float y, float radius){
        return (x1 < (x + radius) && x2 > (x - radius) &&
                y1 < (y + radius) && y2 > (y - radius));
    }

    /** Sets the light map buffer's texture filter to Nearest. */
    public void pixelate(){
        getLightMapBuffer().getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        customDiffuseShader = PixelShader.createPixelShader();
    }

    public void setTint(Color color){
        this.tintColor = color;
    }

    /**
     * Updates and renders all active lights.
     * <p>
     * <p><b>NOTE!</b> Remember to set combined matrix before this method.
     * <p>
     * <p>Don't call this inside of any begin/end statements.
     * Call this method after you have rendered background but before UI.
     * Box2d bodies can be rendered before or after depending how you want
     * the x-ray lights to interact with them.
     *
     * @see #update()
     * @see #render()
     */
    public void updateAndRender(){
        update();
        render();
    }

    /**
     * Manual update method for all active lights.
     * <p>
     * <p>Use this if you have less physics steps than rendering steps.
     *
     * @see #updateAndRender()
     * @see #render()
     */
    public void update(){
        for(Light light : lightList){
            light.update();
        }
    }

    /**
     * Prepare all lights for rendering.
     * <p>
     * <p>You should need to use this method only if you want to render lights
     * on a frame buffer object. Use {@link #render()} otherwise.
     * <p>
     * <p><b>NOTE!</b> Don't call this inside of any begin/end statements.
     *
     * @see #renderOnly()
     * @see #render()
     */
    public void prepareRender(){
        lightRenderedLastFrame = 0;

        Gdx.gl.glDepthMask(false);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        simpleBlendFunc.apply();

        boolean useLightMap = (shadows || blur);
        if(useLightMap){
            lightMap.frameBuffer.begin();
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        ShaderProgram shader = customLightShader != null ? customLightShader : lightShader;
        shader.begin();
        {
            shader.setUniformMatrix("u_projTrans", combined);
            if(customLightShader != null) updateLightShader();
            for(Light light : lightList){
                if(customLightShader != null) updateLightShaderPerLight(light);
                light.render();
            }
        }
        shader.end();

        if(useLightMap){
            if(customViewport){
                lightMap.frameBuffer.end(
                        viewportX,
                        viewportY,
                        viewportWidth,
                        viewportHeight);
            }else{
                lightMap.frameBuffer.end();
            }

            boolean needed = lightRenderedLastFrame > 0;
            // this way lot less binding
            if(needed && blur)
                lightMap.gaussianBlur();
        }
    }

    /**
     * Manual rendering method for all lights.
     * <p>
     * <p><b>NOTE!</b> Remember to set combined matrix and update lights
     * before using this method manually.
     * <p>
     * <p>Don't call this inside of any begin/end statements.
     * Call this method after you have rendered background but before UI.
     * Box2d bodies can be rendered before or after depending how you want
     * the x-ray lights to interact with them.
     *
     * @see #updateAndRender()
     * @see #update()
     * @see #setCombinedMatrix(Matrix4, float, float, float, float)
     */
    public void render(){
        prepareRender();
        lightMap.render();
    }

    /**
     * Manual rendering method for all lights tha can be used inside of
     * begin/end statements
     * <p>
     * <p>Use this method if you want to render lights in a frame buffer
     * object. You must call {@link #prepareRender()} before calling this
     * method. Also, {@link #prepareRender()} must not be inside of any
     * begin/end statements
     *
     * @see #prepareRender()
     */
    public void renderOnly(){
        lightMap.render();
    }

    /**
     * Called before light rendering start
     * <p>
     * Override this if you are using custom light shader
     */
    protected void updateLightShader(){

    }

    /**
     * Called for custom light shader before each light is rendered
     * <p>
     * Override this if you are using custom light shader
     */
    protected void updateLightShaderPerLight(Light light){

    }

    /**
     * Checks whether the given point is inside of any light volume
     *
     * @return true if point is inside of any light volume
     */
    public boolean pointAtLight(float x, float y){
        for(Light light : lightList){
            if(light.contains(x, y)) return true;
        }
        return false;
    }

    /**
     * Checks whether the given point is outside of all light volumes
     *
     * @return true if point is NOT inside of any light volume
     */
    public boolean pointAtShadow(float x, float y){
        for(Light light : lightList){
            if(light.contains(x, y)) return false;
        }
        return true;
    }

    /**
     * Disposes all this rayHandler lights and resources
     */
    public void dispose(){
        removeAll();
        if(lightMap != null) lightMap.dispose();
        if(lightShader != null) lightShader.dispose();
    }

    /**
     * Removes and disposes both all active and disabled lights
     */
    public void removeAll(){
        for(Light light : lightList){
            light.dispose();
        }
        lightList.clear();

        for(Light light : disabledLights){
            light.dispose();
        }
        disabledLights.clear();
    }

    /**
     * Set custom light shader, null to reset to default
     * <p>
     * Changes will take effect next time #render() is called
     */
    public void setLightShader(ShaderProgram customLightShader){
        this.customLightShader = customLightShader;
    }

    public void setDiffuseShader(ShaderProgram diffuse){
        this.customDiffuseShader = diffuse;
    }

    /**
     * Enables/disables culling.
     * <p>
     * <p>This save CPU and GPU time when the world is bigger than the screen.
     * <p>
     * <p>Default = true
     */
    public void setCulling(boolean culling){
        this.culling = culling;
    }

    /**
     * Enables/disables Gaussian blur.
     * <p>
     * <p>This make lights much more softer and realistic look but cost some
     * precious shader time. With default FBO size on android cost around 1ms.
     * <p>
     * <p>Default = true
     *
     * @see #setBlurNum(int)
     */
    public void setBlur(boolean blur){
        this.blur = blur;
    }

    /**
     * Sets number of Gaussian blur passes.
     * <p>
     * <p>Blurring can be pretty heavy weight operation, 1-3 should be safe.
     * Setting this to 0 is the same as disabling it.
     * <p>
     * <p>Default = 1
     *
     * @see #setBlur(boolean)
     */
    public void setBlurNum(int blurNum){
        this.blurNum = blurNum;
    }

    /**
     * Enables/disables shadows
     */
    public void setShadows(boolean shadows){
        this.shadows = shadows;
    }

    /**
     * Sets ambient light brightness. Specifies shadows brightness.
     * <p>Default = 0
     *
     * @param ambientLight shadows brightness value, clamped to [0f; 1f]
     * @see #setAmbientLight(Color)
     * @see #setAmbientLight(float, float, float, float)
     */
    public void setAmbientLight(float ambientLight){
        this.ambientLight.a = MathUtils.clamp(ambientLight, 0f, 1f);
    }

    /**
     * Sets ambient light color.
     * Specifies how shadows colored and their brightness.
     * <p>
     * <p>Default = Color(0, 0, 0, 0)
     *
     * @param r shadows color red component
     * @param g shadows color green component
     * @param b shadows color blue component
     * @param a shadows brightness component
     * @see #setAmbientLight(float)
     * @see #setAmbientLight(Color)
     */
    public void setAmbientLight(float r, float g, float b, float a){
        this.ambientLight.set(r, g, b, a);
    }

    /**
     * Sets ambient light color.
     * Specifies how shadows colored and their brightness.
     * <p>
     * <p>Default = Color(0, 0, 0, 0)
     *
     * @param ambientLightColor color whose RGB components specify the shadows coloring and
     * alpha specify shadows brightness
     * @see #setAmbientLight(float)
     * @see #setAmbientLight(float, float, float, float)
     */
    public void setAmbientLight(Color ambientLightColor){
        this.ambientLight.set(ambientLightColor);
    }

    /**
     * Sets rendering to custom viewport with specified position and size
     * <p>Note: you will be responsible for update of viewport via this method
     * in case of any changes (on resize)
     */
    public void useCustomViewport(int x, int y, int width, int height){
        customViewport = true;
        viewportX = x;
        viewportY = y;
        viewportWidth = width;
        viewportHeight = height;
    }

    /**
     * Sets rendering to default viewport
     * <p>
     * <p>0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
     */
    public void useDefaultViewport(){
        customViewport = false;
    }

    /**
     * Enables/disables lightMap automatic rendering.
     * <p>
     * <p>If set to false user needs to use the {@link #getLightMapTexture()}
     * and render that or use it as a light map when rendering. Example shader
     * for spriteBatch is given. This is faster way to do if there is not that
     * much overdrawing or if just couple object need light/shadows.
     * <p>
     * <p>Default = true
     */
    public void setLightMapRendering(boolean isAutomatic){
        lightMap.lightMapDrawingDisabled = !isAutomatic;
    }

    /**
     * Expert functionality
     *
     * @return Texture that contain lightmap texture that can be used as light
     * texture in your shaders
     */
    public Texture getLightMapTexture(){
        return lightMap.frameBuffer.getColorBufferTexture();
    }

    /**
     * Expert functionality, no support given
     *
     * @return FrameBuffer that contains lightMap
     */
    public FrameBuffer getLightMapBuffer(){
        return lightMap.frameBuffer;
    }

    public void clearRects(){
        rectangles.clear();
    }

    public void addRect(Rectangle rect){
        rectangles.add(rect);
    }

    public void removeRect(Rectangle rect){
        rectangles.removeValue(rect, true);
    }

    public Array<Rectangle> getRects(){
        return rectangles;
    }

    /** Updates the internal quadtree's rectangles. Call this every time you modify or move a rectangle. */
    public void updateRects(){
        tree.clear();

        for(Rectangle rect : rectangles){
            tree.insert(rect);
        }
    }

    //TODO more efficient raycast algorithm
    void raycast(Light light, Vector2 start, Vector2 end){

        Vector2 closest = vector;
        cdist = -1f;

        closest.set(end);

        tmprects.clear();

        r2.setPosition(Math.min(start.x, end.x), Math.min(start.y, end.y));
        r2.setSize(Math.max(start.x, end.x), Math.max(start.y, end.y));

        r2.width -= r2.x;
        r2.height -= r2.y;

        //TODO
        tree.getIntersect(tmprects, r2);
        //tmprects.addAll(rectangles);

        for(Rectangle rect : tmprects){

            Vector2 vect = Physics.raycastRect(start.x, start.y, end.x, end.y, rect.x + rect.width / 2, rect.y + rect.height / 2, rect.width / 2, rect.height / 2);

            if(vect == null) continue;

            float dist = vect.dst(start);

            if(cdist < 0 || dist < cdist){
                closest.set(vect);
                cdist = dist;
            }
        }

        float noise = 0;

        if(light.noisemag > 0)
            noise = Noise.fnoise(light.m_index, Timers.time() / light.noisetime, light.noisescl, light.noisemag);

        //closest.sub(start).setLength(closest.len() + Mathf.range(80)).add(start);
        float dst = closest.dst(start) - Math.abs(noise);
        float frac = dst / start.dst(end);

        light.mx[light.m_index] = closest.x;
        light.my[light.m_index] = closest.y;
        light.f[light.m_index] = frac;
    }
}
