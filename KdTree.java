package s3;
/*************************************************************************
 *************************************************************************/

import java.util.Arrays;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdRandom;

public class KdTree {
	private Node root;             // root of BST
	private static enum Line {VERTICAL, HORIZONTAL};

    private class Node {
        private Node left, right;  // left and right subtrees
        private RectHV rect;
        private Point2D p;

        public Node(Point2D p) {
            this.p = p;
            this.left = null;
            this.right = null;
        }
    }
	
    // construct an empty set of points
    public KdTree() {
    	root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }
    
    private int size(Node x) {
    	if(x == null)
    		return 0;
    	else
    		return 1 + size(x.left) + size(x.right);
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
    	root = insert(root, Line.HORIZONTAL, p);
    };
    
    private Node insert(Node x, Line line, Point2D p) {
    	if(x == null) return new Node(p);
    	if(line == Line.VERTICAL) {
    		if		(p.x() < x.p.x()) x.left = insert(x.left, Line.HORIZONTAL, p); 		// TODO: Find nicer way for comparing the points
    		else if	(p.x() > x.p.x()) x.right = insert(x.right, Line.HORIZONTAL, p);
    		else x.p = p; 																// <- this is not needed
    	}
    	else if(line == Line.HORIZONTAL) {
    		if		(p.x() < x.p.x()) x.left = insert(x.left, Line.VERTICAL, p);
    		else if	(p.x() > x.p.x()) x.right = insert(x.right, Line.VERTICAL, p);
    		else x.p = p; 																// <- this is not needed
    	}
    	return x;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(root, Line.HORIZONTAL, p);
    }
    
    private boolean contains(Node x, Line line, Point2D p) {
    	
    	if(x == null) return false;
    	if(line == Line.VERTICAL) {
    		if		(p.x() < x.p.x()) return contains(x.left, Line.HORIZONTAL, p); 		// TODO: Find nicer way for comparing the points
    		else if	(p.x() > x.p.x()) return contains(x.right, Line.HORIZONTAL, p);
    		else return true;
    	}
    	else if(line == Line.HORIZONTAL) {
    		if		(p.x() < x.p.x()) return contains(x.left, Line.VERTICAL, p);
    		else if	(p.x() > x.p.x()) return contains(x.right, Line.VERTICAL, p);
    		else return true;
    	}
    	StdOut.println("Error in contains, should not be here");
    	return false;
    }

    // draw all of the points to standard draw
    public void draw() {

    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return p;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
    	int N = StdIn.readInt();
    	KdTree kdt = new KdTree();
    	
    	Point2D[] points = new Point2D[N];
    	for(int i = 0; i < N; i++) {
    		double x = StdRandom.uniform(0, 10)/10.0;
    		double y = StdRandom.uniform(0, 10)/10.0;
    		points[i] = new Point2D(x, y);
    		
    		StdOut.println("x = " + x + " y = " + y);
    	}
    	
    	for(int i = 0; i < N; i++) {
    		kdt.insert(points[i]);
    	}
    	for(int i = 0; i < N; i++) {
    		StdOut.println("Contains point " + "p = " + points[i] + ", " + kdt.contains(points[i]));
    	}
    	/*
        In in = new In();
        Out out = new Out();
        int nrOfRecangles = in.readInt();
        int nrOfPointsCont = in.readInt();
        int nrOfPointsNear = in.readInt();
        RectHV[] rectangles = new RectHV[nrOfRecangles];
        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
        for (int i = 0; i < nrOfRecangles; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsCont; i++) {
            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsNear; i++) {
            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        KdTree set = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble(), y = in.readDouble();
            set.insert(new Point2D(x, y));
        }
        for (int i = 0; i < nrOfRecangles; i++) {
            // Query on rectangle i, sort the result, and print
            Iterable<Point2D> ptset = set.range(rectangles[i]);
            int ptcount = 0;
            for (Point2D p : ptset)
                ptcount++;
            Point2D[] ptarr = new Point2D[ptcount];
            int j = 0;
            for (Point2D p : ptset) {
                ptarr[j] = p;
                j++;
            }
            Arrays.sort(ptarr);
            out.println("Inside rectangle " + (i + 1) + ":");
            for (j = 0; j < ptcount; j++)
                out.println(ptarr[j]);
        }
        out.println("Contain test:");
        for (int i = 0; i < nrOfPointsCont; i++) {
            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
        }

        out.println("Nearest test:");
        for (int i = 0; i < nrOfPointsNear; i++) {
            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
        }

        out.println();
        */
    }
}
