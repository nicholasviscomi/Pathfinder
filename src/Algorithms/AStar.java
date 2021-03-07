package Algorithms;

import DataStructures.*;

import java.util.ArrayList;
import java.awt.*;


public class AStar implements Algorithm {

    private ArrayList<ArrayList<Node<Point>>> grid;
    private Point sPoint, ePoint;

    public AStar(ArrayList<ArrayList<Node<Point>>> grid, Point sPoint, Point ePoint) {
        this.grid = grid;
        this.sPoint = sPoint;
        this.ePoint = ePoint;
    }


    public boolean solve() {
        return true;
    }

    public ArrayList<Node<Point>> getPath(Node<Point> end) {
        return new ArrayList<>();
    }

}
