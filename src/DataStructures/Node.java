package DataStructures;

import GUI.*;

import java.awt.Point;

public class Node<T> {
    public Node<T> next;
    public T value;
    public boolean isStart = false;
    public boolean isEnd = false;

    public Node(Node<T> next, T value) {
        this.value = value;
        this.next = next;

        if (value instanceof Point) {
            Point p = (Point) value;
            // System.out.println("Node: s " + Frame.getSPoint());
            if (p.x == Frame.getSPoint().x && p.y == Frame.getSPoint().y) {
                isStart = true;
            }
        }
        
        if (value instanceof Point) {
            Point p = (Point) value;
            // System.out.println("Node: e " + Frame.getEPoint());
            if (p.x == Frame.getEPoint().x && p.y == Frame.getEPoint().y) {
                isEnd = true;
            }
        }
    }

}
