package com.gdx.tdl.map.ags;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerNumber extends SteeringAgent {
    float boundingRadius;
    Vector2 pos;
    int num;

    PlayerNumber(World world, Vector2 pos, float boundingRadius, int num, BodyDef.BodyType bodyType) {
        super(world, pos, boundingRadius, bodyType);

        this.boundingRadius = boundingRadius * 1.25f;
        this.pos = pos;
        this.num = num;
    }

    void setPosition(Vector2 pos) { this.pos = pos; }

    @Override
    public void agentDraw() {
        switch (num) {
            case 1:
                sprite.setRegion(AssetLoader.one);
                break;
            case 2:
                sprite.setRegion(AssetLoader.two);
                break;
            case 3:
                sprite.setRegion(AssetLoader.three);
                break;
            case 4:
                sprite.setRegion(AssetLoader.four);
                break;
            case 5:
                sprite.setRegion(AssetLoader.five);
                break;
        }
        sprite.setSize(boundingRadius, boundingRadius);
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(pos.x - boundingRadius/2, pos.y - boundingRadius/2);
        sprite.draw(AssetLoader.batch);
    }

    @Override public short isCattegory() { return BIT_NOBODY; }
    @Override public short collidesWith() { return BIT_NOBODY; }
}
