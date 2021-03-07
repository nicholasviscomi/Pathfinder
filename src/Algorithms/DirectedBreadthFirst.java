package Algorithms;

import DataStructures.Node;
import DataStructures.PQueue;

import java.awt.*;
import java.util.ArrayList;


public class DirectedBreadthFirst implements Algorithm {
    /*
    priority queue with the next node that has the shortest distance to the end node at the top
    then expand the dequeued node and look around it adding them to the Priority queue.
    In the pqueue, when something in enqueued it goes through every element and sort the
    queue to be in increasing size of distance to end node
    */

    private final ArrayList<ArrayList<Node<Point>>> grid;
    public Point sPoint;
    public Point ePoint;

    PQueue pq = new PQueue();
    private final ArrayList<ArrayList<Boolean>> visited = new ArrayList<>();

    int[] dy = { -1, 0, 1, 0 };
    int[] dx = { 0, 1, 0, -1 };
    // int[] dy = {-1, 1, 0, 0, -1, 1, -1, 1};
    // int[] dx = {0, 0, 1, -1, 1, 1, -1, -1};

    // int[] vdy = {1, 1, -1, -1};
    // int[] vdx = {1, -1, 1, -1};

    public DirectedBreadthFirst(ArrayList<ArrayList<Node<Point>>> grid, Point sPoint, Point ePoint) {
        this.grid = grid;
        this.sPoint = sPoint;
        this.ePoint = ePoint;
        setVisited();
    }

    boolean found = false;

    public boolean solve() {
        setVisited();
        pq.clear();

        // pq.enqueue(Frame.sPoint, distFromEnd(Frame.sPoint));
        System.out.println("spoint getter: " + GUI.Frame.getSPoint());
        pq.enqueue(GUI.Frame.getSPoint(), distFromEnd(GUI.Frame.getSPoint()));

        while (pq.size > 0 && !found) {
            Point next = pq.dequeue();

            if (next.x == 5 && next.y == 15) {
                System.out.println("dbf next point: " + next);
            }
            
            Node<Point> nNode = GUI.Frame.nodeAtPoint(next);

            if (Helper.Helper.pEqualsP(next, ePoint)) {
//                System.out.println("Open Nodes: " + Frame.openNodes.size());
//                System.out.println("Path: " + getPath(Frame.nodeAtPoint(ePoint)).size());
                found = true;
                break;
            }

            exploreNeighbors(nNode);
        }

        return found;
    }

    /*
    get distnace form end for each point
    when it is added to the q it will move to the front
    then when it is dequeued it will continuously be the 1 of 4 neighbors closest to the end
    */

    public void exploreNeighbors(Node<Point> n) {

        for (int i = 0; i < 4; i++) {
            int newX = n.value.x + dx[i];
            int newY = n.value.y + dy[i];
            Point newPoint = new Point(newX, newY);
            Node<Point> newNode = new Node<Point>(n, newPoint);

            if (
                    !(newNode.value.y == GUI.Frame.height/20 ||
                newNode.value.y == -1 || 
                newNode.value.x == GUI.Frame.width/20 ||
                newNode.value.x == -1)
            ) {
                boolean isVisited = visited.get(newNode.value.y).get(newNode.value.x);
                boolean isWall = GUI.Frame.walls.contains(newPoint);

                if(!isVisited && !isWall) {
                    grid.get(newNode.value.y).set(newNode.value.x, newNode);
                    visited.get(newNode.value.y).set(newNode.value.x, true);

                    pq.enqueue(newPoint, distFromEnd(newPoint));
                    // System.out.println("Dist: " + distFromEnd(newPoint) + " point: " + newPoint);
                    GUI.Frame.openNodes.add(newPoint);
                }
            }
        }

    }

    public ArrayList<Node<Point>> getPath(Node<Point> end) {
        ArrayList<Node<Point>> path = new ArrayList<>();
        Node<Point> curr = end;

        System.out.println("get path: " + sPoint);
        while (curr.next != null) {// maybe add an exception where it breaks the loop if it equals the spoint
            path.add(curr);
            curr = curr.next;
        }   

        path.add(curr);

        return path;
    }

    public static double distFromEnd(Point p) {
        double dy = Math.abs(p.y - GUI.Frame.ePoint.y);
        double dx = Math.abs(p.x - GUI.Frame.ePoint.x);
        // dx = Math.pow(dx, 2);
        // dy = Math.pow(dy, 2);

        // return Math.sqrt(sum);
        return dx + dy;
    }

    void setVisited() {
        for (int i = 0; i < GUI.Frame.height/20; i++) {
            ArrayList<Boolean> row = new ArrayList<Boolean>();
            for (int j = 0; j < GUI.Frame.width/20; j++) {
                row.add(false);
            }
            visited.add(row);
        }
    }
}
