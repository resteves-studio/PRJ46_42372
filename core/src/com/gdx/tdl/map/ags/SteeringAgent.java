package com.gdx.tdl.map.ags;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.map.EntityLocation;
import com.gdx.tdl.util.map.SteeringUtils;

public abstract class SteeringAgent implements Steerable<Vector2> {
    public static final short BIT_EMPTY  = -1;
    public static final short BIT_NOBODY =  0;
    public static final short BIT_PLAYER =  1;
    public static final short BIT_BALL   =  2;

    private float maxLinearSpeed, maxLinearAcceleration;
    private float maxAngularSpeed, maxAngularAcceleration;
    private float boundingRadius;
    private boolean tagged;

    SteeringAcceleration<Vector2> steeringAcceleration;
    SteeringBehavior<Vector2> behaviour;

    Vector2 initialPos, nullVector;

    private FixtureDef fixtureDef;
    private CircleShape circle;
    Sprite sprite;
    Body body;

    SteeringAgent(World world, Vector2 pos, float boundingRadius, BodyDef.BodyType bodyType) {
        this.boundingRadius = boundingRadius;
        this.initialPos = pos.cpy();
        this.nullVector = new Vector2(0, 0);

        this.maxLinearSpeed         = 120f;
        this.maxLinearAcceleration  = 5000f;
        this.maxAngularSpeed        = 30;
        this.maxAngularAcceleration = 10;

        this.tagged = false;

        this.steeringAcceleration = new SteeringAcceleration<>(new Vector2());

        this.sprite = new Sprite();

        buildAgent(world, pos.x, pos.y, bodyType);
        this.sprite.setBounds(0, 0, boundingRadius*3, boundingRadius*3);
        this.sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);

        this.body.setUserData(this);
    }

    // constroi o agente
    private void buildAgent(World world, float posX, float posY, BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(posX, posY);

        circle = new CircleShape();
        circle.setRadius(boundingRadius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.filter.categoryBits = isCattegory();
        fixtureDef.filter.maskBits = collidesWith();

        body = world.createBody(bodyDef).createFixture(fixtureDef).getBody();
    }

    // definicao dos filtros de colisao
    public abstract short isCattegory();
    public abstract short collidesWith();

    //
    public abstract void agentDraw();

    //
    public void update(float delta) {
        AssetLoader.batch.begin();
        agentDraw();
        AssetLoader.batch.end();

        if (behaviour != null) {
            behaviour.calculateSteering(steeringAcceleration);
            applySteering(delta);
        }
    }

    // aplicacao do movimento
    private void applySteering(float delta) {
        boolean accelerationApplied = false;

        // aplicacao da parte linear
        if (!steeringAcceleration.linear.isZero()) {
            Vector2 force = steeringAcceleration.linear.scl(delta);
            body.applyForceToCenter(force, true);
            accelerationApplied = true;
        } else {
            body.setLinearVelocity(0f, 0f);
            body.setAngularVelocity(0);
        }

        // aplicacao da parte angular
        if (steeringAcceleration.angular != 0) {
            body.applyTorque(steeringAcceleration.angular * delta, true);
            accelerationApplied = true;
        } else {
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero()) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * delta);
                body.setTransform(body.getPosition(), newOrientation);
            }
        }

        // caso exista alguma aplicada
        if (accelerationApplied) {
            // linear
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }

            // angular
            if (body.getAngularVelocity() > maxAngularSpeed) {
                body.setAngularVelocity(maxAngularSpeed);
            }
        }
    }


    // getters
    public Body getBody() { return body; }
    public Vector2 getInitialPos() { return initialPos; }
    CircleShape getShape() { return circle; }
    FixtureDef getFixtureDef() { return fixtureDef; }
    SteeringBehavior<Vector2> getBehaviour() { return behaviour; }

    // setters
    public void setAtPosition(Vector2 pos) { this.body.setTransform(pos, this.body.getAngle()); }
    public void setBehaviour(SteeringBehavior<Vector2> behaviour) { this.behaviour = behaviour; }


    // ---- Funcoes de Steering ---- //
    @Override public Vector2 getLinearVelocity() { return body.getLinearVelocity(); }
    @Override public float getAngularVelocity() { return body.getAngularVelocity(); }
    @Override public float getBoundingRadius() { return boundingRadius; }
    @Override public boolean isTagged() { return tagged; }
    @Override public void setTagged(boolean tagged) { this.tagged = tagged; }


    // ---- Funcoes de Limiter ---- //
    @Override public float getMaxAngularAcceleration() { return maxAngularAcceleration; }
    @Override public float getMaxAngularSpeed() { return maxAngularSpeed; }
    @Override public float getMaxLinearAcceleration() { return maxLinearAcceleration; }
    @Override public float getMaxLinearSpeed() { return maxLinearSpeed; }
    @Override public void setMaxAngularAcceleration(float maxAngularAcceleration) { this.maxAngularAcceleration = maxAngularAcceleration; }
    @Override public void setMaxAngularSpeed(float maxAngularSpeed) { this.maxAngularSpeed = maxAngularSpeed; }
    @Override public void setMaxLinearAcceleration(float maxLinearAcceleration) { this.maxLinearAcceleration = maxLinearAcceleration; }
    @Override public void setMaxLinearSpeed(float maxLinearSpeed) { this.maxLinearSpeed = maxLinearSpeed; }
    @Override public void setZeroLinearSpeedThreshold(float value) { }

    @Override public float getZeroLinearSpeedThreshold() { return 0; }


    // ---- Funcoes de Location ---- //
    @Override public Vector2 getPosition() { return body.getPosition(); }
    @Override public float getOrientation() { return body.getAngle(); }
    @Override public void setOrientation(float orientation) { body.setTransform(getPosition(), orientation); }
    @Override public float vectorToAngle(Vector2 vector) { return SteeringUtils.vectorToAngle(vector); }
    @Override public Vector2 angleToVector(Vector2 outVector, float angle) { return SteeringUtils.angleToVector(outVector, angle); }
    @Override public Location newLocation() { return new EntityLocation(); }
}
