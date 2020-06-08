package com.gdx.tdl.map.ags;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class Ball extends SteeringAgent {
    private EmptyAgent basket, target, playerTarget;

    public Ball(World world, Vector2 pos, float boundingRadius, EmptyAgent playerTarget) {
        super(world, pos, boundingRadius, BodyDef.BodyType.DynamicBody);
        this.playerTarget = playerTarget;
    }

    @Override
    public void agentDraw() {
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(body.getPosition().cpy().x - sprite.getWidth()/2, body.getPosition().cpy().y - sprite.getHeight()/2);
        sprite.setRegion(AssetLoader.ball);
        sprite.draw(AssetLoader.batch);

        Vector2 u = basket.getPosition().cpy().sub(playerTarget.getPosition().cpy()).nor();
        Vector2 v = playerTarget.getPosition().cpy().add(u.scl(4f * getBoundingRadius(), 4f * getBoundingRadius()));
        target.getBody().setTransform(v, target.getBody().getAngle());

        float tx = Math.abs(body.getPosition().x - target.getBody().getPosition().x);
        float ty = Math.abs(body.getPosition().y - target.getBody().getPosition().y);

        if (tx < 50 && ty < 50) {
            steeringAcceleration.linear.setZero();
        }
    }


    @Override public short isCattegory() { return BIT_BALL; }
    @Override public short collidesWith() { return BIT_NOBODY; }

    // getters
    public EmptyAgent getTarget() { return target; }

    // setters
    public void setTarget(EmptyAgent target) { this.target = target; }
    public void setPlayerToFollow(EmptyAgent playerTarget) { this.playerTarget = playerTarget; }
    public void setBasketTarget(EmptyAgent basket) { this.basket = basket; }
}
