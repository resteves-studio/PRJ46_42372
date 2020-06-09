package com.gdx.tdl.map.ags;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class PlayerCheck extends SteeringAgent {
    float boundingRadius;
    Vector2 pos;

    PlayerCheck(World world, Vector2 pos, float boundingRadius, BodyDef.BodyType bodyType) {
        super(world, pos, boundingRadius, bodyType);

        this.boundingRadius = boundingRadius;
        this.pos = pos;
    }

    void setPosition(Vector2 pos) { this.pos = pos; }

    @Override
    public void agentDraw() {
        sprite.setRegion(AssetLoader.check);
        sprite.setSize(boundingRadius, boundingRadius);
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(pos.x - boundingRadius*2, pos.y + boundingRadius);
        sprite.draw(AssetLoader.batch);
    }

    @Override public short isCattegory() { return BIT_NOBODY; }
    @Override public short collidesWith() { return BIT_NOBODY; }
}
