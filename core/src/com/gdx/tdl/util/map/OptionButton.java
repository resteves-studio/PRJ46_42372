package com.gdx.tdl.util.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class OptionButton {
    public static final short BIT_BUTTON = 4;

    private Sprite sprite;
    private Texture texture, textureSelected;
    private String text;
    private boolean isSelected;
    private float posX, posY, fontSize;
    Body body;

    private float hx = Gdx.graphics.getWidth()/20f;
    private float hy = Gdx.graphics.getHeight()/12f;

    OptionButton(World world, float posX, float posY, Texture texture, Texture textureSelected, String text, float fontSize) {
        this.sprite = new Sprite();
        this.posX = posX;
        this.posY = posY;
        this.isSelected = false;
        this.texture = texture;
        this.textureSelected = textureSelected;
        this.text = text;
        this.fontSize = fontSize;
        buttonDraw(world, posX, posY);
    }

    OptionButton(World world, float posX, float posY, Texture texture, Texture textureSelected) {
        this(world, posX, posY, texture, textureSelected, null, 0);
    }

    OptionButton(World world, float posX, float posY, Texture texture, String text, float fontSize) {
        this(world, posX, posY, texture, null, text, fontSize);
    }

    OptionButton(World world, float posX, float posY, Texture texture) {
        this(world, posX, posY, texture, null, null, 0);
    }

    private void buttonDraw(World world, float posX, float posY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(posX, posY);

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(hx, hy, new Vector2(hx, hy), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rect;
        fixtureDef.filter.categoryBits = BIT_BUTTON;
        fixtureDef.filter.maskBits = BIT_BUTTON;

        body = world.createBody(bodyDef).createFixture(fixtureDef).getBody();

        sprite.setBounds(posX, posY, Gdx.graphics.getWidth()/15f, Gdx.graphics.getWidth()/15f);
        sprite.setCenter(hx + posX, hy + posY);

        body.setUserData(this);
    }

    void update() {
        if (textureSelected != null)
            sprite.setRegion(isSelected ? textureSelected : texture);
        else
            sprite.setRegion(texture);
        sprite.draw(AssetLoader.batch);

        if (text != null) {
            AssetLoader.font.getData().setScale(fontSize);
            AssetLoader.font.draw(AssetLoader.batch, text, hx*2 + posX, hy + posY);
        }
    }

    // getteres
    public Body getBody() { return body; }
    public boolean getIsSelected() { return this.isSelected; }

    // setters
    public void setIsSelected(boolean isSelected) { this.isSelected = isSelected; }
}
