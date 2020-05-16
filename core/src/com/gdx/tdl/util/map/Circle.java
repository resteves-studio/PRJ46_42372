package com.gdx.tdl.util.map;

public class Circle {
    private Point center;
    private float radius;

    public Point findCircleCenter(Point p1, Point p2, float angle, Point p3) {
        // calculo do raio
        float distance = p1.distanceFrom(p2);
        this.radius = Math.round((float) (distance / Math.abs(2.0f*Math.cos(Math.toRadians(angle) - Math.PI/2.0f))));
        float diameter = 2f * radius;

        // verificacoes acerca do raio
        if (radius == 0.0)
            return p1;
        /*if (radius < 0.0) TODO
            throw new IllegalArgumentException("the radius can't be negative");
        if (radius == 0.0 && p1 != p2)
            throw new IllegalArgumentException("no circles can ever be drawn");
        if (Objects.equals(p1, p2))
            throw new IllegalArgumentException("an infinite number of circles can be drawn");
        if (distance > diameter)
            throw new IllegalArgumentException("the points are too far apart to draw a circle");*/


        Point center = new Point((p1.getX() + p2.getX()) / 2f, (p1.getY() + p2.getY()) / 2f);
        if (distance == diameter)
            return center;

        float mirrorDistance = (float) Math.sqrt(radius * radius - distance * distance / 4f);
        float dx = (p2.getX() - p1.getX()) * mirrorDistance / distance;
        float dy = (p2.getY() - p1.getY()) * mirrorDistance / distance;

        Point c1 = new Point(Math.round(center.getX() - dy), Math.round(center.getY() + dx));
        Point c2 = new Point(Math.round(center.getX() + dy), Math.round(center.getY() - dx));

        if (c1.distanceFrom(p3) > c2.distanceFrom(p3)) {
            //System.out.println("CENTER 1 > " + c1);
            return this.center = c1;
        }
        //System.out.println("CENTER 2 > " + c2);
        return this.center = c2;
    }

    public Point[] findCircleIntersection(Circle c2) {
        // distancia entre raios
        float dx = c2.center.getX() - this.center.getX();
        float dy = c2.center.getY() - this.center.getY();
        float distance = c2.center.distanceFrom(this.center);

        // verificacoes
        /*if (distance > (this.radius + c2.radius)) TODO
            throw new IllegalArgumentException("Distance bigger than he sum of the radii");
        if (distance < Math.abs(this.radius - c2.radius))
            throw new IllegalArgumentException("Distance shorter than the sub of the radii");*/

        // distancia entre P0 e P2 (ponto medio)
        float a = ((this.radius*this.radius) - (c2.radius*c2.radius) + (distance*distance)) / (2.0f*distance);

        // coordenadas do P2
        float x2 = Math.round(this.center.getX() + (dx * a/distance));
        float y2 = Math.round(this.center.getY() + (dy * a/distance));

        // distancia entre P2 e qualquer um dos P3
        float h = (float) Math.sqrt((this.radius*this.radius) - (a*a));

        // offsets entre P2 e P3
        float rx = Math.round(-dy * (h/distance));
        float ry = Math.round(dx * (h/distance));

        Point[] ppp = new Point[] {new Point(x2+rx, y2+ry),
                                    new Point(x2-rx, y2-ry)};

        //System.out.println("INTERSECTION 0 > " + ppp[0].toString());
        //System.out.println("INTERSECTION 1 > " + ppp[1].toString());

        return ppp;
    }
}
