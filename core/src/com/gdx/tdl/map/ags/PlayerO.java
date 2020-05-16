package com.gdx.tdl.map.ags;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class PlayerO extends SteeringAgent {
    private boolean hasBall, hasMoved;
    private PlayerCheck check;
    private EmptyAgent target;
    private int num, lastMove, receiver;

    public PlayerO(World world, Vector2 pos, float boundingRadius, boolean hasBall, int num) {
        super(world, pos, boundingRadius, BodyDef.BodyType.DynamicBody);

        this.target = new EmptyAgent(world, pos, 0.01f);
        this.check = new PlayerCheck(world, body.getPosition().cpy(), boundingRadius/1.25f, BodyDef.BodyType.DynamicBody);

        this.num = num;
        this.lastMove = -1;
        this.receiver = -1;
        this.hasMoved = false;
        this.hasBall = hasBall;
    }

    @Override
    public void agentDraw() {
        if (hasBall)
            sprite.setRegion(isTagged() ? AssetLoader.blueSelected : AssetLoader.blue);
        else
            sprite.setRegion(isTagged() ? AssetLoader.blueIISelected : AssetLoader.blueII);

        if (hasMoved) {
            check.setPosition(body.getPosition().cpy());
            check.agentDraw();
        }

        //sprite.setTexture(AssetLoader.one);
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(body.getPosition().cpy().x - sprite.getWidth()/2, body.getPosition().cpy().y - sprite.getHeight()/2);
        sprite.draw(AssetLoader.batch);

        if (body.getPosition().equals(target.getBody().getPosition()))
            steeringAcceleration.linear.setZero();
    }


    @Override public short isCattegory() { return BIT_PLAYER; }
    @Override public short collidesWith() { return BIT_PLAYER | BIT_BALL; }


    // getters
    public boolean hasBall() { return this.hasBall; }
    public int getLastMove() { return this.lastMove; }
    public int getReceiver() { return this.receiver; }
    public EmptyAgent getTarget() { return this.target; }

    // setters
    public void setHasBall(boolean hasBall) { this.hasBall = hasBall; }
    public void setLastMove(int lastMove) { this.lastMove = lastMove; }
    public void setReceiver(int receiver) { this.receiver = receiver; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
    public void setTargetPosition(Vector2 pos) { this.target.getBody().setTransform(pos, this.target.getBody().getAngle()); }

}
