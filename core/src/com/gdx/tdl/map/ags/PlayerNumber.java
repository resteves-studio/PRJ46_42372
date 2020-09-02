package com.gdx.tdl.map.ags;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class PlayerNumber extends SteeringAgent {
    float boundingRadius;
    String numColor;
    Vector2 pos;
    int num;

    public PlayerNumber(World world, Vector2 pos, float boundingRadius, int num, BodyDef.BodyType bodyType, String numColor) {
        super(world, pos, boundingRadius, bodyType);

        this.boundingRadius = boundingRadius * 1.25f;
        this.numColor = numColor;
        this.pos = pos;
        this.num = num;
    }

    public void setPosition(Vector2 pos) { this.pos = pos; }

    @Override
    public void agentDraw() {
        switch (num) {
            case 1:
                sprite.setRegion(this.numColor.equals("White") ? AssetLoader.whiteOne : AssetLoader.darkOne);
                break;
            case 2:
                sprite.setRegion(this.numColor.equals("White") ? AssetLoader.whiteTwo : AssetLoader.darkTwo);
                break;
            case 3:
                sprite.setRegion(this.numColor.equals("White") ? AssetLoader.whiteThree : AssetLoader.darkThree);
                break;
            case 4:
                sprite.setRegion(this.numColor.equals("White") ? AssetLoader.whiteFour : AssetLoader.darkFour);
                break;
            case 5:
                sprite.setRegion(this.numColor.equals("White") ? AssetLoader.whiteFive : AssetLoader.darkFive);
                break;
        }
        sprite.setSize(boundingRadius, boundingRadius);
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(pos.x - boundingRadius/2, pos.y - boundingRadius/2);
        sprite.draw(AssetLoader.batch);
    }

    @Override public short isCattegory() { return BIT_NOBODY; }
    @Override public short collidesWith() { return BIT_NOBODY; }

    // setters
    public void setNumColor(String numColor) { this.numColor = numColor; }
}
