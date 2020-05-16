package com.gdx.tdl.util.map;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public abstract class AbstractScreen implements Screen, InputProcessor {
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera orthCamera;
    private Camera camera;

    ShapeRenderer shapeRenderer;
    protected World world;

    protected OptionsTable optionsTable;

    protected AbstractScreen() {
        this.world = new World(new Vector2(0, 0), true);
        this.b2dr = new Box2DDebugRenderer();

        this.camera = new Camera();
        this.orthCamera = this.camera.getCamera();
        this.shapeRenderer = this.camera.getShapeRenderer();

        this.optionsTable = new OptionsTable(world);
    }

    // cada subclasse ira construir o seu Screen
    public abstract void buildScreen(float delta);

    @Override
    public void render(float delta) {
        // cor de fundo
        //Gdx.gl.glClearColor(200.0f/255, 80.0f/255, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 60 fps, 6 iterations, 2 iterations
        world.step(1/60f, 6, 2);

        // render do debugger
        new Box2DDebugRenderer().render(world, camera.getCamera().combined);

        buildScreen(delta);
    }

    @Override
    public void resize(int width, int height) {
        this.orthCamera.viewportWidth = width;
        this.orthCamera.viewportHeight = height;
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        shapeRenderer.dispose();
    }

    // --- Metodos obrigatorios de Screen --- //
    @Override public void show() { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    // ----- metodos obrigatorios de InputProcessor -----
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(int amount) { return false; }

}
