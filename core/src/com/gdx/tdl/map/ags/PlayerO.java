package com.gdx.tdl.map.ags;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;

public class PlayerO extends SteeringAgent {
    private boolean hasBall, hasMoved, frameOver;
    private String playerColor, zoneSpace;
    private PlayerCheck check;
    private EmptyAgent target;
    private int num, lastMove, receiver;

    public PlayerO(World world, Vector2 pos, float boundingRadius, boolean hasBall, int num, String playerColor) {
        super(world, pos, boundingRadius, BodyDef.BodyType.DynamicBody);

        this.target = new EmptyAgent(world, pos, 0.01f);
        this.check = new PlayerCheck(world, body.getPosition().cpy(), boundingRadius/1.25f, BodyDef.BodyType.DynamicBody);

        this.num = num;
        this.zoneSpace = "A";
        this.playerColor = playerColor;
        this.lastMove = -1;
        this.receiver = -1;
        this.hasMoved = false;
        this.hasBall = hasBall;
        this.frameOver = false;
    }

    @Override
    public void agentDraw() {
        if (hasBall) { sprite.setRegion(isTagged() ? playerSelectedColor() : playerColor()); }
        else { sprite.setRegion(isTagged() ? playerSelectedIIColor() : playerIIColor()); }

        if (hasMoved) {
            check.setPosition(body.getPosition().cpy());
            check.agentDraw();
        }

        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(body.getPosition().cpy().x - sprite.getWidth()/2, body.getPosition().cpy().y - sprite.getHeight()/2);
        sprite.draw(AssetLoader.batch);

        float tx = Math.abs(body.getPosition().x - target.getBody().getPosition().x);
        float ty = Math.abs(body.getPosition().y - target.getBody().getPosition().y);

        if (tx < 50 && ty < 50) {
            steeringAcceleration.linear.setZero();
            setFrameOver(true);
        }

        /*if (body.getPosition().equals(target.getBody().getPosition())) {
            steeringAcceleration.linear.setZero();
            setFrameOver(true);
        }*/
    }

    private Texture playerColor() {
        switch (this.playerColor) {
            case "Laranja":  return AssetLoader.orange;
            case "Escuro":   return AssetLoader.dark;
            case "Verde":    return AssetLoader.green;
            case "Azul":     return AssetLoader.blue;
            case "Vermelho": return AssetLoader.red;
            case "Amarelo":  return AssetLoader.yellow;
            default:         return AssetLoader.light;
        }
    }

    private Texture playerIIColor() {
        switch (this.playerColor) {
            case "Laranja":  return AssetLoader.orangeII;
            case "Escuro":   return AssetLoader.darkII;
            case "Verde":    return AssetLoader.greenII;
            case "Azul":     return AssetLoader.blueII;
            case "Vermelho": return AssetLoader.redII;
            case "Amarelo":  return AssetLoader.yellowII;
            default:         return AssetLoader.lightII;
        }
    }

    private Texture playerSelectedColor() {
        switch (this.playerColor) {
            case "Laranja":  return AssetLoader.orangeSelected;
            case "Escuro":   return AssetLoader.darkSelected;
            case "Verde":    return AssetLoader.greenSelected;
            case "Azul":     return AssetLoader.blueSelected;
            case "Vermelho": return AssetLoader.redSelected;
            case "Amarelo":  return AssetLoader.yellowSelected;
            default:         return AssetLoader.lightSelected;
        }
    }

    private Texture playerSelectedIIColor() {
        switch (this.playerColor) {
            case "Laranja":  return AssetLoader.orangeIISelected;
            case "Escuro":   return AssetLoader.darkIISelected;
            case "Verde":    return AssetLoader.greenIISelected;
            case "Azul":     return AssetLoader.blueIISelected;
            case "Vermelho": return AssetLoader.redIISelected;
            case "Amarelo":  return AssetLoader.yellowIISelected;
            default:         return AssetLoader.lightIISelected;
        }
    }

    public String numberColor() {
        switch (this.playerColor) {
            case "Laranja":
            case "Amarelo":
            case "Claro":
                return "Dark";
            default:
                return "White";
        }
    }


    @Override public short isCattegory() { return BIT_PLAYER; }
    @Override public short collidesWith() { return BIT_PLAYER; }


    // getters
    public boolean hasBall() { return this.hasBall; }
    public int getLastMove() { return this.lastMove; }
    public int getReceiver() { return this.receiver; }
    public EmptyAgent getTarget() { return this.target; }
    public String getZoneSpace() { return this.zoneSpace; }
    public boolean isFrameOver() { return this.frameOver; }

    // setters
    public void setHasBall(boolean hasBall) { this.hasBall = hasBall; }
    public void setLastMove(int lastMove) { this.lastMove = lastMove; }
    public void setReceiver(int receiver) { this.receiver = receiver; }
    public void setZoneSpace(String zoneSpace) { this.zoneSpace = zoneSpace; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
    public void setFrameOver(boolean frameOver) { this.frameOver = frameOver; }
    public void setPlayerColor(String playerColor) { this.playerColor = playerColor; }
    public void setTargetPosition(Vector2 pos) { this.target.getBody().setTransform(pos, this.target.getBody().getAngle()); }

}
