package com.gdx.tdl.map.ags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.map.Circle;
import com.gdx.tdl.util.map.Point;

public class PlayerD extends SteeringAgent {
    private PlayerO playerTarget, playerWithBall;
    private EmptyAgent basketTarget, mainTarget;
    private boolean permissionToFollow;
    private Ball ballTarget;
    private int num;

    public PlayerD(World world, Vector2 pos, float boundingRadius, int num) {
        super(world, pos, boundingRadius, BodyDef.BodyType.DynamicBody);

        this.num = num;
        this.permissionToFollow = false;
    }


    @Override
    public void agentDraw() {
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(body.getPosition().cpy().x - sprite.getWidth()/2, body.getPosition().cpy().y - sprite.getHeight()/2);
        if (playerTarget.hasBall()) sprite.setRegion(AssetLoader.red);
        else sprite.setRegion(AssetLoader.redII);
        sprite.draw(AssetLoader.batch);

        if (permissionToFollow)
            setMainTargetPosition(calculateTargetPosition());

        if (body.getPosition().equals(mainTarget.getBody().getPosition()))
            steeringAcceleration.linear.setZero();
    }

    private Vector2 calculateTargetPosition() {
        // se o seu player tiver a bola, fica entre ele e o cesto
        if (getPlayerTarget().hasBall()) {
            Vector2 u = getBasketTarget().getPosition().cpy().sub(getPlayerTarget().getPosition().cpy()).nor();
            Vector2 v = getPlayerTarget().getPosition().cpy().add(u.scl(3.5f * getBoundingRadius(), 3.5f * getBoundingRadius()));
            return new Vector2(v.x, v.y);
        }

        Point p1 = Point.vector2Point(getPlayerTarget().getPosition());   // jogador
        Point p2 = Point.vector2Point(getPlayerWithBall().getPosition()); // bola
        Point p3 = Point.vector2Point(getBasketTarget().getPosition());   // cesto
        /*System.out.println("P1 > " + p1);
        System.out.println("P2 > " + p2);
        System.out.println("P3 > " + p3);*/

        Vector2 ab = new Vector2(p3.getX()-p1.getX(), p3.getY()-p1.getY());
        Vector2 bc = new Vector2(p2.getX()-p3.getX(), p2.getY()-p3.getY());
        float theta = (float) Math.acos((ab.x*bc.x + ab.y*bc.y) / (Math.sqrt(ab.x*ab.x + ab.y*ab.y) * Math.sqrt(bc.x*bc.x + bc.y*bc.y)));
        theta = (float) Math.toDegrees(theta);
        float thetaInv = 180 - theta;
        //System.out.println("THETA > " + thetaInv);

        // se os jogadores tiverem afastados entre eles (angulo obtuso) terao uma ajuda mais interior
        if (thetaInv >= 90 || (p1.getY() > Gdx.graphics.getHeight()*2/3f && p2.getY() > Gdx.graphics.getHeight()*2/3f)) {
            // ver distanica entre os jogadores ou o angulo em si
            // criar um "peso" para saber o quao perto do cesto ele fica
            // retornar vetor com essa nova posicao
            float per = 3*(-180/thetaInv); // TODO melhorar o per
            Vector2 u = getBasketTarget().getPosition().cpy().sub(getPlayerTarget().getPosition().cpy()).nor();
            Vector2 v = getBasketTarget().getPosition().cpy().add(u.scl(per*getBoundingRadius(), per*getBoundingRadius()));
            return new Vector2(v.x, v.y);
        } else if (thetaInv <= 25) {
            Vector2 u = getBasketTarget().getPosition().cpy().sub(getPlayerTarget().getPosition().cpy()).nor();
            Vector2 v = getPlayerTarget().getPosition().cpy().add(u.scl(4f * getBoundingRadius(), 4f * getBoundingRadius()));
            return new Vector2(v.x, v.y);
        }

        /* caso um jogador diferente tenha a bola, passa a existir um triangulo jogador/bola/cesto
           sao criados circulos entre cada dupla de pontos do triangulo */
        Circle c12 = new Circle();
        Circle c13 = new Circle();
        Circle c23 = new Circle();

        /* calculados os centros desses circulos    TODO remover o12, o13, o23 se nao for necessario
             o12 - centro do circulo entre jogador e bola
             o13 - centro do circulo entre jogador e cesto
             o23 - centro do circulo entre bola e cesto
         */
        int[] angles = getAngles(thetaInv);
        c12.findCircleCenter(p1, p2, angles[0], p3);
        c13.findCircleCenter(p1, p3, angles[1], p2);
        c23.findCircleCenter(p2, p3, angles[2], p1);

        // a intersecao desses circulos da-nos um ponto em comum
        Point p0;

        Point[] p1213 = c12.findCircleIntersection(c13);
        Point[] p1223 = c12.findCircleIntersection(c23);
        Point[] p1323 = c13.findCircleIntersection(c23);

        // atribuido o primeiro ponto de intersecao
        p0 = p1213[0];
        // caso for igual ao ponto ja existente (aplicado intervalo),
        // e atribuido o segundo ponto de intersecao (que e o novo)
        if (p0.getX() >= p1.getX() - 2 && p0.getX() <= p1.getX() + 2 && p0.getY() >= p1.getY() - 2 && p0.getY() <= p1.getY() + 2)
            p0 = p1213[1];

        // o ponto de intersecao tera de ser igual em todos
        if (!(p0.getX() >= p1223[0].getX() - 2) && !(p0.getX() <= p1223[0].getX() + 2) && !(p0.getY() >= p1223[0].getY() - 2) && !(p0.getY() <= p1223[0].getY() + 2))
            if (!(p0.getX() >= p1223[1].getX() - 2) && !(p0.getX() <= p1223[1].getX() + 2) && !(p0.getY() >= p1223[1].getY() - 2) && !(p0.getY() <= p1223[1].getY() + 2))
                System.out.println("Algo se passa, one...");

        // se nao for, algo se passa...
        if (!(p0.getX() >= p1323[0].getX() - 2) && !(p0.getX() <= p1323[0].getX() + 2) && !(p0.getY() >= p1323[0].getY() - 2) && !(p0.getY() <= p1323[0].getY() + 2))
            if (!(p0.getX() >= p1323[1].getX() - 2) && !(p0.getX() <= p1323[1].getX() + 2) && !(p0.getY() >= p1323[1].getY() - 2) && !(p0.getY() <= p1323[1].getY() + 2))
                System.out.println("Algo se passa, two...");


        //System.out.println("INTERI BOI > " + p0);

        // o ponto em comum traduz-se na posicao onde deve estar
        return new Vector2(p0.getX(), p0.getY());
    }

    // adapta os angulos conforme a posicao dos jogadores TODO melhorar
    private int[] getAngles(float theta) {
        int[] angles = new int[3];

        if (theta >= 80 && theta <= 90) {
            angles[0] = 120;
            angles[1] = 120;
            angles[2] = 120;
        } else if (theta >= 60 && theta < 80) {
            angles[0] = 135;
            angles[1] = 120;
            angles[2] = 105;
        } else if (theta >= 40 && theta < 60) {
            angles[0] = 150;
            angles[1] = 110;
            angles[2] = 100;
        } else if (theta < 40) {
            angles[0] = 160;
            angles[1] = 105;
            angles[2] = 95;
        }

        return angles;
    }


    @Override public short isCattegory() { return BIT_PLAYER; }
    @Override public short collidesWith() { return BIT_PLAYER | BIT_BALL; }

    // getters
    public int getNum() { return this.num; }
    public EmptyAgent getMainTarget() { return this.mainTarget; }
    public Vector2 getMainTargetPosition() { return this.mainTarget.getBody().getPosition().cpy(); }
    PlayerO getPlayerTarget() { return this.playerTarget; }
    PlayerO getPlayerWithBall() { return this.playerWithBall; }
    EmptyAgent getBasketTarget() { return this.basketTarget; }
    public boolean getPermissionToFollow() { return this.permissionToFollow; }

    // setters
    public void setMainTarget(EmptyAgent mainTarget) { this.mainTarget = mainTarget; }
    public void setMainTargetPosition(Vector2 pos) { this.mainTarget.getBody().setTransform(pos, mainTarget.getBody().getAngle()); }
    public void setInitMainTargetPosition() { setMainTargetPosition(calculateTargetPosition()); }
    public void setPlayerTarget(PlayerO playerTarget) { this.playerTarget = playerTarget; }
    public void setPlayerWithBall(PlayerO playerWithBall) { this.playerWithBall = playerWithBall; }
    public void setBasketTarget(EmptyAgent basketTarget) { this.basketTarget = basketTarget; }
    public void setPermissionToFollow(boolean permissionToFollow) { this.permissionToFollow = permissionToFollow; }
    public void setPosToInitial() {
        this.mainTarget.getBody().setTransform(initialPos, this.body.getAngle());
        this.body.setTransform(initialPos, this.body.getAngle());
    }



    public Ball getBallTarget() { return this.ballTarget; }
    public void setBallTarget(Ball ballTarget) { this.ballTarget = ballTarget; }
}
