package com.gdx.tdl.map.ags;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class Ball extends SteeringAgent {
    private EmptyAgent basket, target, playerTarget;
    private String ballColor;
    private boolean frameOver;

    public Ball(World world, Vector2 pos, float boundingRadius, EmptyAgent playerTarget, String ballColor) {
        super(world, pos, boundingRadius, BodyDef.BodyType.DynamicBody);
        this.playerTarget = playerTarget;
        this.ballColor = ballColor;
        this.frameOver = false;
    }

    @Override
    public void agentDraw() {
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(body.getPosition().cpy().x - sprite.getWidth()/2, body.getPosition().cpy().y - sprite.getHeight()/2);
        sprite.setRegion(ballColor());
        sprite.draw(AssetLoader.batch);

        Vector2 u = basket.getPosition().cpy().sub(playerTarget.getPosition().cpy()).nor();
        Vector2 v = playerTarget.getPosition().cpy().add(u.scl(3f * getBoundingRadius(), 3f * getBoundingRadius()));
        target.getBody().setTransform(v, target.getBody().getAngle());

        float tx = Math.abs(body.getPosition().x - target.getBody().getPosition().x);
        float ty = Math.abs(body.getPosition().y - target.getBody().getPosition().y);

        if (tx < 50 && ty < 50) {
            steeringAcceleration.linear.setZero();
            setFrameOver(true);
        }
    }

    private Texture ballColor() {
        switch (this.ballColor) {
            case "Laranja":  return AssetLoader.ball_orange;
            case "Escuro":   return AssetLoader.ball_dark;
            case "Verde":    return AssetLoader.ball_green;
            case "Azul":     return AssetLoader.ball_blue;
            case "Vermelho": return AssetLoader.ball_red;
            case "Amarelo":  return AssetLoader.ball_yellow;
            default:         return AssetLoader.ball_light;
        }
    }


    @Override public short isCattegory() { return BIT_BALL; }
    @Override public short collidesWith() { return BIT_NOBODY; }

    // getters
    public EmptyAgent getTarget() { return target; }
    public boolean isFrameOver() { return this.frameOver; }

    // setters
    public void setTarget(EmptyAgent target) { this.target = target; }
    public void setPlayerToFollow(EmptyAgent playerTarget) { this.playerTarget = playerTarget; }
    public void setBasketTarget(EmptyAgent basket) { this.basket = basket; }
    public void setBallColor(String ballColor) { this.ballColor = ballColor; }
    public void setFrameOver(boolean frameOver) { this.frameOver = frameOver; }
}
