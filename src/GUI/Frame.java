package GUI;

import Algorithms.AStar;
import Algorithms.Algorithm;
import DataStructures.Node;
import Algorithms.BreadthFirstSearch;
import Algorithms.DirectedBreadthFirst;
import Helper.Helper;
import MazeGen.MazeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/*
make a new project with very similar things except the mazes are borders and not whole blocks
they should be 4 individual lines around a point
when cells get connected the line on a certain side of the cell will be removed

look through the functions used in the solving algos and recreate them (funcs like isWall)
eventually try and carry that creation over to this project

*/
@SuppressWarnings("ALL")
public class Frame extends JPanel implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    public static int width = 1200;
    public static int height = 600;
    static int gridSide = 20;
    public static Point sPoint;
    public static Point ePoint;

    static JFrame frame;
    // private JPanel panel;

    public static ArrayList<Point> walls = new ArrayList<>();
    public static ArrayList<ArrayList<Node<Point>>> grid = new ArrayList<>();
    public static ArrayList<Node<Point>> path = new ArrayList<>();
    public static ArrayList<Point> openNodes = new ArrayList<>();

    private boolean mouseOnScreen = false;

    private final JButton solve, clear, createMaze, directedFirst, aStarSolve;
    // private JCheckBox diagonalCB;

    //ALGORITHM CLASSES
    private BreadthFirstSearch bf;
    private DirectedBreadthFirst dbf;
    private AStar aStar;
    MazeGenerator mazeGen;

    // static char currKey;

    private boolean pathOnScreen = false;
    private boolean pathIsDrawn = false;
    private boolean nodesAreDrawn = false;

    static Timer pathTimer;
    static Timer oNodeTimer;
    public static void main(String[] args) {
        new Frame();
    }

    /*
    IDEAS:
        --> make a dot maze that is just dots all around the screen
        --> different algorithms (bidirectional swarm is so dope)
    */

    public Frame() {
        setLayout(null);
        setFocusable(true);
        requestFocus();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        sPoint = new Point(15, 15);
        ePoint = new Point(53, 15);

        frame = new JFrame();
        frame.setContentPane(this);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Magical Pathfinder");
        frame.setSize(width, height);
        frame.pack();
        frame.setLocationRelativeTo(null);

        solve = new JButton("Breadth First");
        Dimension solveSize = solve.getPreferredSize();
        solve.setBounds(30, 530, solveSize.width, solveSize.height);
        solve.setOpaque(true);
        solve.setVisible(true);
        solve.addActionListener(this);

        clear = new JButton("Clear");
        Dimension clearSize = clear.getPreferredSize();
        clear.setBounds(30, 530+solveSize.height, clearSize.width, clearSize.height);
        clear.setOpaque(true);
        clear.setVisible(true);
        clear.addActionListener(this);

        createMaze = new JButton("Create Maze");
        Dimension createMazeSize = createMaze.getPreferredSize();
        createMaze.setBounds(30 + solveSize.width, 530, createMazeSize.width, createMazeSize.height);
        createMaze.setOpaque(true);
        createMaze.setVisible(true);
        createMaze.addActionListener(this);

        directedFirst = new JButton("Directed Breadth First");
        Dimension dbfSize = directedFirst.getPreferredSize();
        directedFirst.setBounds(30 + clearSize.width, 530 + solveSize.height, dbfSize.width, dbfSize.height);
        directedFirst.setOpaque(true);
        directedFirst.setVisible(true);
        directedFirst.addActionListener(this);

        aStarSolve = new JButton("A*");
        Dimension aSize = aStarSolve.getPreferredSize();
        aStarSolve.setBounds(directedFirst.getX() + dbfSize.width + 5, directedFirst.getY(), aSize.width, aSize.height);
        aStarSolve.setOpaque(true);
        aStarSolve.setVisible(true);
        aStarSolve.addActionListener(this);

        frame.add(solve);
        frame.add(clear);
        frame.add(createMaze);
        frame.add(directedFirst);
        frame.add(aStarSolve);
        // frame.add(bidirectional);

        frame.setVisible(true);
        
        createGrid();

        pathTimer = new Timer(35, this);
        oNodeTimer = new Timer(2, this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw the walls on
        for (Point point : walls) {
            g.setColor(Color.BLUE);
            g.fillRect(point.x * 20, point.y * 20, gridSide, gridSide);
        }

        //create the grid
        for (int y = 0; y < Frame.height; y+=gridSide) {
            for (int x = 0; x < Frame.width; x+=gridSide) {
                if (y == sPoint.y*20 && x == sPoint.x*20) {
                    g.setColor(Color.RED);
                    g.fillRect(x, y, 20, 20);
                } else if (y == ePoint.y*20 && x == ePoint.x*20) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, 20, 20);
                } else {
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, gridSide, gridSide);
                }
            }
        }

        //keep the path drawn after it was animated previously
        // if (pathIsDrawm) {
        //     for (Node<Point> n : path) {
        //         System.out.println("paint spoint: " + path.get(0).value);
        //         g.setColor(new Color(0, 0, 0, 80));
        //         g.fillRect(n.value.x*20, n.value.y*20, 20, 20);
        //     }
        // }

        //animate drawing the path 
        if (path.size() > 0 && pathIndex < path.size() && !pathIsDrawn) {
            if (pathTimer.isRunning()) {
                for (int i = 0; i <= pathIndex; i++) {
                    Node<Point> n = path.get(i);
                    // System.out.println("paint spoint: " + n.value);
                    g.setColor(new Color(0, 0, 0, 80));
                    g.fillRect(n.value.x*20, n.value.y*20, 20, 20);
                }
                if (pathIndex == path.size()-1) {
                    System.out.println("stopping timer! ");
                    pathTimer.stop();
                    pathIsDrawn = true;
                    pathIndex = 0;
                }
            
            }
        } 

        if (nodesAreDrawn) {
            for (Point n : openNodes) {
                g.setColor(new Color(255, 0, 0, 30));
                g.fillRect(n.x*20, n.y*20, 20, 20);
            }
        }

        if (openNodes.size() > 0 && oNodeIndex < openNodes.size() && !nodesAreDrawn) {
            if (oNodeTimer.isRunning()) {
                for (int i = 0; i <= oNodeIndex; i++) {
                    // System.out.print("open node: "); Helper.printPoint(n);
                    Point n = openNodes.get(i);
                    g.setColor(new Color(255, 0, 0, 30));
                    g.fillRect(n.x*20, n.y*20, 20, 20);
                }
                if (oNodeIndex == openNodes.size()-1) {
                    System.out.println("stopping oNode timer! ");
                    oNodeTimer.stop();
                    nodesAreDrawn = true;
                    oNodeIndex = 0;
                }
            }
        }

    }

    static public Node<Point> nodeAtPoint(Point p) {
        return grid.get(p.y).get(p.x);
    }

    void solve(Algorithm alg) {
        if (pathOnScreen) { return; }
        
        createGrid();

        System.out.print("solve: ");
        Helper.printPoint(sPoint);

        if (alg.solve()) {
            path.clear();
            System.out.println("path bf " + path);
            path = alg.getPath(nodeAtPoint(ePoint));
            path = Helper.reverse(path);
            pathTimer.start();
            oNodeTimer.start();
        }

        frame.repaint();
        requestFocus();
        pathOnScreen = true;
    }

    void clear() {
        if (!pathOnScreen) {
            walls.clear();
        }
        path.clear();
        openNodes.clear();
        pathOnScreen = false;
        pathIsDrawn = false;
        nodesAreDrawn = false;
        pathIndex = 0;
        oNodeIndex = 0;
        repaint();
        requestFocus();
        createGrid();
    }

    int pathIndex = 0;
    int oNodeIndex = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solve) {
            bf = new BreadthFirstSearch(grid, sPoint, ePoint);
            solve(bf);
            return;
        }

        if (e.getSource() == directedFirst) {
            dbf = new DirectedBreadthFirst(grid, sPoint, ePoint);
            solve(dbf);
            return;
        }

        if (e.getSource() == aStarSolve) {
            aStar = new AStar(grid, sPoint, ePoint);
            solve(aStar);
        }

        if (e.getSource() == clear) {
            clear();
            return;
        }

        if (e.getSource() == pathTimer) {
            if (!nodesAreDrawn) { return; }
            if (pathIndex == path.size()-1) {
                System.out.println("stopping path timer! ");
                pathTimer.stop();
                pathIsDrawn = true;
                pathIndex = 0;
            }
            pathIndex++;
            repaint();
        }

        if (e.getSource() == oNodeTimer) {
            if (oNodeIndex == openNodes.size()-1) {
                System.out.println("stopping oNode timer!");
                oNodeTimer.stop();
                nodesAreDrawn = true;
                oNodeIndex = 0;
            }
            oNodeIndex++;
            repaint();
        }

        if (e.getSource() == createMaze) {
            clear();
            mazeGen = new MazeGenerator(sPoint, ePoint, grid);
            walls = mazeGen.recursiveDivision();
            repaint();
        }
         
    }

	@Override
	public void mouseEntered(MouseEvent e) {
        // System.out.println("entered screen");
        mouseOnScreen = true;
    }

	@Override
	public void mouseExited(MouseEvent e) {
        // System.out.println("exited screen");
        mouseOnScreen = false;
    }

    @Override
	public void mouseClicked(MouseEvent e) {
        if (mouseOnScreen && !pathOnScreen) {
            double x = e.getX();
            double y = e.getY();

            x = x/gridSide;
            y = y/gridSide;

            int sqX = (int) Math.ceil(x) - 1;
            int sqY = (int) Math.ceil(y) - 1;
    
            Point p = new Point(sqX, sqY);
            
            if (walls.contains(p)) { return; }

            if (!(Helper.pEqualsP(p, ePoint) || Helper.pEqualsP(p, sPoint))) {
                walls.add(p);
            }
            // }
            repaint();
        }
    }

	@Override
	public void mouseDragged(MouseEvent e) {
        if (mouseOnScreen && !pathOnScreen) {
            double x = e.getX();
            double y = e.getY();   

            // if ( x >= 595 || y >= 575) { return; }

            x = x/gridSide;
            y = y/gridSide;

            int sqX = (int) Math.ceil(x) - 1;
            int sqY = (int) Math.ceil(y) - 1;

            Point p = new Point();
            p.x = sqX;
            p.y = sqY;

            if (walls.contains(p)) { return; }

            if (!(Helper.pEqualsP(p, ePoint) || Helper.pEqualsP(p, sPoint))) {
                walls.add(p);
            }

            repaint();
        }
    }

    void createGrid() {
        grid.clear();

        for (int y = 0; y <= Frame.height; y+=gridSide) {
            ArrayList<Node<Point>> row = new ArrayList<>();
            for (int x = 0; x < Frame.width; x+=gridSide) {

                if (y == sPoint.y*20 && x == sPoint.x*20) { 
                    Point p = new Point(x/20, x/20); //start
                    Node<Point> n = new Node<>(null, p);
                    row.add(n); 
                } else if (y == ePoint.y*20 && x == ePoint.x*20) {
                    Point p = new Point(x/20, x/20); //end
                    Node<Point> n = new Node<>(null, p);
                    row.add(n);
                } else {
                    Point p = new Point(x/20, y/20);
                    Node<Point> n = new Node<>(null, p);
                    row.add(n);
                }

            }
            grid.add(row);
        }
        // System.out.println(grid.get(0).size());
    }

    public static Point getSPoint() {
        return sPoint;
    }
    
    public static Point getEPoint() {
        return ePoint;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // currKey = e.getKeyChar();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // currKey = (char) 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
	public void mousePressed(MouseEvent e) {}

	@Override
    public void mouseReleased(MouseEvent e) {}
    
	@Override
	public void mouseMoved(MouseEvent e) {}


}
