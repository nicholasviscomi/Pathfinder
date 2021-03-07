package Algorithms;

import DataStructures.Node;

import java.util.ArrayList;
import java.awt.Point;

public interface Algorithm {
    public boolean solve();
    public ArrayList<Node<Point>> getPath(Node<Point> ePoint);
}
