package com.gdx.tdl.map.ags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.map.Circle;
import com.gdx.tdl.util.map.Point;

import java.util.HashMap;

public class PlayerD extends SteeringAgent {
    private PlayerO playerTarget, playerWithBall;
    private EmptyAgent basketTarget, mainTarget;
    private boolean permissionToFollow, homem;
    private String playerColor, zoneSpace;
    private HashMap<String, Vector2> A, B, C, D, E;
    private int num;

    public PlayerD(World world, Vector2 pos, float boundingRadius, int num, String playerColor, String zoneSpace) {
        super(world, pos, boundingRadius, BodyDef.BodyType.DynamicBody);

        A = new HashMap<>();
        A.put("B", new Vector2(Gdx.graphics.getWidth()*5.50f/10f, Gdx.graphics.getHeight()/2f));
        A.put("C", new Vector2(Gdx.graphics.getWidth()*7.25f/10f, Gdx.graphics.getHeight()*7.75f/10f));
        A.put("D", new Vector2(Gdx.graphics.getWidth()*5.50f/10f, Gdx.graphics.getHeight()*6.50f/10f));
        A.put("E", new Vector2(Gdx.graphics.getWidth()*3.75f/10f, Gdx.graphics.getHeight()*7.75f/10f));

        B = new HashMap<>();
        B.put("A", new Vector2(Gdx.graphics.getWidth()*5.50f/10f, Gdx.graphics.getHeight()/2f));
        B.put("C", new Vector2(Gdx.graphics.getWidth()*7.25f/10f, Gdx.graphics.getHeight()*7.75f/10f));
        B.put("D", new Vector2(Gdx.graphics.getWidth()*5.50f/10f, Gdx.graphics.getHeight()*6.50f/10f));
        B.put("E", new Vector2(Gdx.graphics.getWidth()*3.75f/10f, Gdx.graphics.getHeight()*7.75f/10f));

        C = new HashMap<>();
        C.put("A", new Vector2(Gdx.graphics.getWidth()*7.50f/10f, Gdx.graphics.getHeight()/2f));
        C.put("B", new Vector2(Gdx.graphics.getWidth()*6.00f/10f, Gdx.graphics.getHeight()/2f));
        C.put("D", new Vector2(Gdx.graphics.getWidth()*7.25f/10f, Gdx.graphics.getHeight()*7.75f/10f));
        C.put("E", new Vector2(Gdx.graphics.getWidth()*5.50f/10f, Gdx.graphics.getHeight()*7.75f/10f));

        D = new HashMap<>();
        D.put("A", new Vector2(Gdx.graphics.getWidth()*6.50f/10f, Gdx.graphics.getHeight()*4/10f));
        D.put("B", new Vector2(Gdx.graphics.getWidth()*4.50f/10f, Gdx.graphics.getHeight()*4/10f));
        D.put("C", new Vector2(Gdx.graphics.getWidth()*7.25f/10f, Gdx.graphics.getHeight()*7.75f/10f));
        D.put("E", new Vector2(Gdx.graphics.getWidth()*3.75f/10f, Gdx.graphics.getHeight()*7.75f/10f));

        E = new HashMap<>();
        E.put("A", new Vector2(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f));
        E.put("B", new Vector2(Gdx.graphics.getWidth()*3.50f/10f, Gdx.graphics.getHeight()/2f));
        E.put("C", new Vector2(Gdx.graphics.getWidth()*5.50f/10f, Gdx.graphics.getHeight()*7.75f/10f));
        E.put("D", new Vector2(Gdx.graphics.getWidth()*3.75f/10f, Gdx.graphics.getHeight()*7.75f/10f));

        this.num = num;
        this.homem = true;
        this.zoneSpace = zoneSpace;
        this.playerColor = playerColor;
        this.permissionToFollow = false;
    }


    @Override
    public void agentDraw() {
        sprite.setRotation(body.getAngle() * 180 / (float) Math.PI);
        sprite.setPosition(body.getPosition().cpy().x - sprite.getWidth()/2, body.getPosition().cpy().y - sprite.getHeight()/2);
        if (homem) {
            if (playerTarget.hasBall()) sprite.setRegion(playerColor());
            else sprite.setRegion(playerIIColor());
        } else {
            if (getPlayerWithBall().getZoneSpace().equals(this.zoneSpace))
                sprite.setRegion(playerColor());
            else sprite.setRegion(playerIIColor());
        }
        sprite.draw(AssetLoader.batch);

        if (permissionToFollow)
            setMainTargetPosition(homem ? calculateTargetPosition() : calculateZoneSpace());

        if (body.getPosition().equals(mainTarget.getBody().getPosition()))
            steeringAcceleration.linear.setZero();
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

    // defesa individual - calcula a posicao do target
    private Vector2 calculateTargetPosition() {
        // se o seu player tiver a bola, fica entre ele e o cesto
        if (getPlayerTarget().hasBall()) {
            Vector2 u = getBasketTarget().getPosition().cpy().sub(getPlayerTarget().getPosition().cpy()).nor();
            Vector2 v = getPlayerTarget().getPosition().cpy().add(u.scl(4f * getBoundingRadius(), 4f * getBoundingRadius()));
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
            float per = 4.5f * (-180 / thetaInv); // TODO melhorar o per
            Vector2 u = getBasketTarget().getPosition().cpy().sub(getPlayerTarget().getPosition().cpy()).nor();
            Vector2 v = getBasketTarget().getPosition().cpy().add(u.scl(per*getBoundingRadius(), per*getBoundingRadius()));
            return new Vector2(v.x, v.y);
        } else if (thetaInv < 45) {
            Vector2 u = getBasketTarget().getPosition().cpy().sub(getPlayerTarget().getPosition().cpy()).nor();
            Vector2 v = getPlayerTarget().getPosition().cpy().add(u.scl(4f * getBoundingRadius(), 4f * getBoundingRadius()));
            return new Vector2(v.x, v.y);
        }

        /* caso um jogador diferente tenha a bola, passa a existir um triangulo jogador/bola/cesto
           sao criados circulos entre cada dupla de pontos do triangulo */
        Circle c12 = new Circle();
        Circle c13 = new Circle();
        Circle c23 = new Circle();

        /* calculados os centros desses circulos
            > o12 - centro do circulo entre jogador e bola
            > o13 - centro do circulo entre jogador e cesto
            > o23 - centro do circulo entre bola e cesto
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

    // adapta os angulos conforme a posicao dos jogadores
    private int[] getAngles(float theta) {
        int[] angles = new int[3];

        if (theta >= 70 && theta < 90) {
            angles[0] = 120;
            angles[1] = 120;
            angles[2] = 120;
        } else if (theta >= 50 && theta < 70) {
            angles[0] = 130;
            angles[1] = 130;
            angles[2] = 100;
        } else if (theta >= 30 && theta < 50) {
            angles[0] = 135;
            angles[1] = 130;
            angles[2] = 95;
        }

        return angles;
    }

    // defesa zona - calcula o espaco e a posicao do target
    private Vector2 calculateZoneSpace() {
        float x = getPlayerWithBall().getPosition().x;
        float y = getPlayerWithBall().getPosition().y;

        // se o jogador com bola tiver na zona A
        if (x > Gdx.graphics.getWidth()/2f && y < Gdx.graphics.getHeight()/2f) {
            getPlayerWithBall().setZoneSpace("A");

            // verifica se o defesa nao e o jogador dessa zona e retorna a sua posicao
            if (!this.zoneSpace.equals("A"))
                return A.get(this.zoneSpace);
        }

        // se o jogador com bola tiver na zona B
        else if (x < Gdx.graphics.getWidth()/2f && y < Gdx.graphics.getHeight()/2f) {
            getPlayerWithBall().setZoneSpace("B");

            // verifica se o defesa nao e o jogador dessa zona e retorna a sua posicao
            if (!this.zoneSpace.equals("B"))
                return B.get(this.zoneSpace);
        }

        // se o jogador com bola tiver na zona C
        else if (x > Gdx.graphics.getWidth()*6.95/10f && y > Gdx.graphics.getHeight()/2f) {
            getPlayerWithBall().setZoneSpace("C");

            // verifica se o defesa nao e o jogador dessa zona e retorna a sua posicao
            if (!this.zoneSpace.equals("C"))
                return C.get(this.zoneSpace);
        }

        // se o jogador com bola tiver na zona D
        else if (x > Gdx.graphics.getWidth()*4/10f && x < Gdx.graphics.getWidth()*6.95/10f && y > Gdx.graphics.getHeight()/2f) {
            getPlayerWithBall().setZoneSpace("D");

            // verifica se o defesa nao e o jogador dessa zona e retorna a sua posicao
            if (!this.zoneSpace.equals("D"))
                return D.get(this.zoneSpace);
        }

        // se o jogador com bola tiver na zona E
        else if (x < Gdx.graphics.getWidth()*4/10f && y > Gdx.graphics.getHeight()/2f) {
            getPlayerWithBall().setZoneSpace("E");

            // verifica se o defesa nao e o jogador dessa zona e retorna a sua posicao
            if (!this.zoneSpace.equals("E"))
                return E.get(this.zoneSpace);
        }

        // se for o defesa daquele espaco, fica 1x1 com o jogador da bola
        Vector2 u = getBasketTarget().getPosition().cpy().sub(getPlayerWithBall().getPosition().cpy()).nor();
        Vector2 v = getPlayerWithBall().getPosition().cpy().add(u.scl(4f * getBoundingRadius(), 4f * getBoundingRadius()));
        return new Vector2(v.x, v.y);
    }


    @Override public short isCattegory() { return BIT_PLAYER; }
    @Override public short collidesWith() { return BIT_PLAYER; }

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
    public void setPlayerColor(String playerColor) { this.playerColor = playerColor; }
    public void setPlayerTarget(PlayerO playerTarget) { this.playerTarget = playerTarget; }
    public void setPlayerWithBall(PlayerO playerWithBall) { this.playerWithBall = playerWithBall; }
    public void setBasketTarget(EmptyAgent basketTarget) { this.basketTarget = basketTarget; }
    public void setPermissionToFollow(boolean permissionToFollow) { this.permissionToFollow = permissionToFollow; }
    public void setHomem() { this.homem = true; }
    public void setZona() { this.homem = false; }
    public void setPosToInitial() {
        this.mainTarget.getBody().setTransform(initialPos, this.body.getAngle());
        this.body.setTransform(initialPos, this.body.getAngle());
    }
}
