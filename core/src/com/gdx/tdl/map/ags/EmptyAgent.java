package com.gdx.tdl.map.ags;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class EmptyAgent extends SteeringAgent {
    public EmptyAgent(World world, Vector2 pos, float boundingRadius) {
        super(world, pos, boundingRadius, BodyDef.BodyType.StaticBody);
    }

    @Override
    public short isCattegory() { return BIT_EMPTY; }

    @Override
    public short collidesWith() { return BIT_NOBODY; }

    @Override
    public void agentDraw() {
        sprite.setPosition(body.getPosition().cpy().x - sprite.getWidth()/2, body.getPosition().cpy().y - sprite.getHeight()/2);
        sprite.setRegion(AssetLoader.empty);
        sprite.draw(AssetLoader.batch);
    }
}
