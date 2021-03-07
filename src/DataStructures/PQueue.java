package DataStructures;

import java.awt.*;

public class PQueue {
    /*
    doubly linked queue
    enqueue goes to back 
    while curr.next != null && curr > curr.next  {
        swap()

    }
    */

    public PNode head, tail;
    public int size = 0;

    public PQueue() {
        head = null;
        tail = null;
    }

    public void enqueue(Point p, double dist) {
        PNode d = new PNode(dist, null, p);
        
        if (head == null) {
            head = d;
            size++;
        }

        if (tail != null) {
            tail.next = d;
            size++;
        }

        tail = d;

        sort();

        return;
    }

    void sort() {

        if (head == null) {
            return;
        }

        PNode curr = head;

        while (curr.next != null) {
            if (curr.dist > curr.next.dist) {
                Point tp = curr.p;
                double td = curr.dist;

                curr.p = curr.next.p;
                curr.dist = curr.next.dist;

                curr.next.p = tp;
                curr.next.dist = td;
            }
            curr = curr.next;
        }

        return;
    }

    public Point dequeue() {
        if (head == null) { return null; }

        PNode tmp = head;
        head = head.next;

        if (head == null) { tail = null; }

        size--;

        return tmp.p;
    }

    public void clear() {
        size = 0;
        head = null;
        tail = null;
    }

    public void print() {
        if (head == null) {
            return;
        }

        PNode curr = head;
        System.out.print(curr.dist);

        while (curr.next != null) {
            curr = curr.next;
            System.out.print("-->" + curr.dist);
        }

        System.out.println();
        return;
    }

}

