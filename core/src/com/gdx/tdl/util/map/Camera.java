package com.gdx.tdl.util.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gdx.tdl.util.AssetLoader;

class Camera {
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    Camera() {
        // ----- camera -----
        this.camera = new OrthographicCamera();
        this.camera.viewportWidth = Gdx.graphics.getWidth();
        this.camera.viewportHeight = Gdx.graphics.getHeight();
        this.camera.position.set(this.camera.viewportWidth*0.5f, this.camera.viewportHeight*0.5f, 0.0f); // centra a pos da camera
        this.camera.update();

        AssetLoader.batch.setProjectionMatrix(this.camera.combined); // the combined projection and view matrix

        // ----- shape renderer -----
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
    }

    // ----- getters -----
    OrthographicCamera getCamera() {
        return this.camera;
    }
    ShapeRenderer getShapeRenderer() {
        return this.shapeRenderer;
    }
}
