package DataStructures;

import java.awt.*;

class PNode {

    public double dist;
    public PNode next;
    public Point p;

    PNode(double dist, PNode next, Point p) {
        this.dist = dist;
        this.next = next;
        this.p = p;
    }

}
