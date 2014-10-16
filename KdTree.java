package s3;
/*************************************************************************
 *************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdDraw;
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

        public Node(Point2D p, double xMin, double yMin, double xMax, double yMax) {
            this.p = p;
            rect = new RectHV(xMin, yMin, xMax, yMax);
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
    	root = insert(p, root, null, Line.VERTICAL); 																	// null is used for no parent
    };
    
    private Node insert(Point2D p, Node child, Node parent, Line line) {
    	if(child == null) {
    		if(child == parent)	return new Node(p, 0, 0, 1, 1); 				// This is the root
    		/*
    		if(p.compareTo(parent.p) == 0) { //p == parent.p) {
    			StdOut.println("extra point " + p);
    			return null;										// Avoid adding duplicated points
    		}
    		*/
    		if(line == Line.HORIZONTAL) { 										// The parents line is VERTICAL here 					// Move this to if and else if in main here below
        		if		(p.x() < parent.p.x()) return new Node(p, parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
        		else if	(p.x() >= parent.p.x()) return new Node(p, parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
        	}
    		else if(line == Line.VERTICAL) { 									// The parents line is HORIZONTAL here
        		if		(p.y() < parent.p.y()) return new Node(p, parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
        		else if	(p.y() >= parent.p.y()) return new Node(p, parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
        	}
    		//StdOut.println("extra point " + p + " parent " + parent.p);
    		return null; //StdOut.println("extra point " + p);				// TODO: fix extra points
    	}
    	if(p.equals(child.p))
    	{
    		return child;
    	}
    	if(line == Line.VERTICAL) {
    		if		(p.x() < child.p.x()) child.left = insert(p, child.left, child, Line.HORIZONTAL); 		// TODO: Find nicer way for comparing the points
    		else if	(p.x() >= child.p.x()) {
    			if (p.compareTo(child.p) == 0) {
    				//StdOut.println("extra1 " + p.x() + " " + p.y());
    				return child; //StdOut.println("extra1");
    			}
    			child.right = insert(p, child.right, child, Line.HORIZONTAL);
    		}
    	}
    	else if(line == Line.HORIZONTAL) {// Hello
    		if		(p.y() < child.p.y()) child.left = insert(p, child.left, child, Line.VERTICAL);
    		else if	(p.y() >= child.p.y()) {
    			if (p.compareTo(child.p) == 0) {
    				//StdOut.println("extra2 " + p.x() + " " + p.y());
    				return child; //StdOut.println("extra2");
    			}
    			child.right = insert(p, child.right, child, Line.VERTICAL);
    		}
    	}
    	return child;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(root, Line.VERTICAL, p);
    }
    
    private boolean contains(Node x, Line line, Point2D p) {
    	
    	if(x == null) return false;
    	/*
    	if((x.p.x() ==  0.885 || x.p.y() == 0.291) || (x.p.x() ==  0.314 && x.p.y() == 0.944)) {
			StdOut.println("x.p = " + x.p + " Line = " + line);
		}
    	*/
    	if(line == Line.VERTICAL) {
    		if		(p.x() < x.p.x()) return contains(x.left, Line.HORIZONTAL, p); 		// TODO: Find nicer way for comparing the points
    		else if	(p.x() > x.p.x()) return contains(x.right, Line.HORIZONTAL, p);
    		else {
    			if (p.compareTo(x.p) == 0) //StdOut.println("Verticla");
    				return true;
    			else return contains(x.right, Line.HORIZONTAL, p);
    				
    		}
    	}
    	else if(line == Line.HORIZONTAL) {
    		if		(p.y() < x.p.y()) return contains(x.left, Line.VERTICAL, p);
    		else if	(p.y() > x.p.y()) return contains(x.right, Line.VERTICAL, p);
    		else {
    			if (p.compareTo(x.p) == 0) //StdOut.println("Horizontal");
    				return true;
    			else return contains(x.right, Line.VERTICAL, p);
    		}
    	}
    	return false;
    }

    // draw all of the points to standard draw
    public void draw() {
    	StdDraw.setXscale(0, 1);
		StdDraw.setYscale(0, 1);
		StdDraw.line(0, 0, 0, 1);
		StdDraw.line(0, 0, 1, 0);
		StdDraw.line(1, 0, 1, 1);
		StdDraw.line(0, 1, 1, 1);
		drawPoints(root, Line.VERTICAL);
    }
    
    private void drawPoints(Node child, Line line) {
    	if(child == null) return;
    	if(line == Line.VERTICAL) {
    		StdDraw.setPenColor(StdDraw.RED);
    		StdDraw.setPenRadius();
    		StdDraw.line(child.p.x(), child.rect.ymin(), child.p.x(), child.rect.ymax());
    		
    		StdDraw.setPenColor(StdDraw.BLACK);
        	StdDraw.setPenRadius(.01);
        	StdDraw.point(child.p.x(), child.p.y());
    		
    		drawPoints(child.left, Line.HORIZONTAL);
    		drawPoints(child.right, Line.HORIZONTAL);
    		return;
    	}
    	if(line == Line.HORIZONTAL) {
    		StdDraw.setPenColor(StdDraw.BLUE);
    		StdDraw.setPenRadius();
    		StdDraw.line(child.rect.xmin(), child.p.y(), child.rect.xmax(), child.p.y());
    		
    		StdDraw.setPenColor(StdDraw.BLACK);
        	StdDraw.setPenRadius(.01);
        	StdDraw.point(child.p.x(), child.p.y());
    		
    		drawPoints(child.left, Line.VERTICAL);
    		drawPoints(child.right, Line.VERTICAL);
    		return;
    	}
    	
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
    	ArrayList<Point2D> lis = new ArrayList<Point2D>();
    	Stack<Node> stack = new Stack<Node>();
    	Node current = root;
    	boolean done = false;
    	 
    	while (!done) {
    	    if(current !=  null && current.rect.intersects(rect)) {
	    	    stack.push(current);                                               
	    	    current = current.left;  
    	    }
    	    else {
    	    	if (!stack.isEmpty()) {
    	    		current = stack.pop();
    	    		if(rect.contains(current.p))
    	    			lis.add(current.p);

    	    		current = current.right;
    	    	}
    	    	else
    	    		done = true; 
    	    }
    	}
    	
        return Collections.unmodifiableList(lis);
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) 
    {
    	return nearest(root, root, Line.VERTICAL, p);
    }
    
    private Point2D nearest(Node child, Node parent, Line line, Point2D p)
    {
    	Point2D best = parent.p;
   
    	if(child == null) 
    	{
    		return best = parent.p;
    	}
    	
    	if(p.equals(child.p))
    	{
    		return best = child.p;
    	}
    	if(line == Line.VERTICAL)
    	{
    		if(p.x() < child.p.x())
    		{
    			child.left.p = nearest(child.left, child, Line.HORIZONTAL, p);
        			
        		if(child.left.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
        		{
        			best = child.left.p;
        		}
        		else
        		{
        			best = child.p;
        		}
        		if(best.distanceSquaredTo(p) > (parent.p.x() - p.x()))
        		{
        			child.right.p = nearest(child.right, child, Line.HORIZONTAL, p);
            				
            		if(child.right.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
                	{
                		best = child.right.p;
                	}
       			}
    			else
    			{
    				best = child.p;
    			}
    			
    			return best;
    		}
    		else
    		{
    			child.right.p = nearest(child.right, child, Line.HORIZONTAL, p);
        			
        		if(child.right.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
        		{
        			best = child.right.p;
        		}
        		else
        		{
        			best = child.p;
        		}
        		if(best.distanceSquaredTo(p) > (parent.p.x() - p.x()))
        		{
        			child.left.p = nearest(child.left, child, Line.HORIZONTAL, p);
            				
            		if(child.left.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
            		{
            			best = child.left.p;
            		}
    			}
    			else
    			{
    				best = child.p;
    			}
    			
    			return best;
    		}
    	}
    	//if(line == Line.HORIZONTAL)
    	else
    	{
    		if(p.y() < child.p.y())
    		{
    			child.left.p = nearest(child.left, child, Line.VERTICAL, p);
        			
        		if(child.left.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
        		{
       				best = child.left.p;
       			}
       			else
       			{
       				best = child.p;
       			}
       			if(best.distanceSquaredTo(p) > (parent.p.x()-p.x()))
       			{
        			child.right.p = nearest(child.right, child, Line.VERTICAL, p);
            				
            		if(child.right.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
          			{
           				best = child.right.p;
           			}
    			}
    			else
    			{
    				best = child.p;
    			}
    			
    			return best;
    		}
    		else
    		{
    			child.right.p = nearest(child.right, child, Line.VERTICAL, p);
        			
        		if(child.right.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
        		{
        			best = child.right.p;
        		}
        		else
        		{
       				best = child.p;
       			}
       			if(best.distanceSquaredTo(p) > (parent.p.x() - p.x()))
       			{
        			child.left.p = nearest(child.left, child, Line.VERTICAL, p);
            				
            		if(child.left.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
            		{
      					best = child.left.p;
       				}
        		}
    			else
    			{
    				best = child.p;
    			}
    			
    			return best;
    		}
    	}
    	  	
    	//return p;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
    	/*
    	int N = StdIn.readInt();
    	KdTree kdt = new KdTree();
    	
    	Point2D[] points = new Point2D[N];
    	if(!StdIn.isEmpty()) {
    		for(int i = 0; i < N; i++) {
	    		double x = StdIn.readDouble();
	    		double y = StdIn.readDouble();
	    		points[i] = new Point2D(x, y);
	    	}
    	}
    	else {
	    	for(int i = 0; i < N; i++) {
	    		double x = StdRandom.uniform(0, 1000)/1000.0;
	    		double y = StdRandom.uniform(0, 1000)/1000.0;
	    		points[i] = new Point2D(x, y);
	    		
	    		StdOut.println("x = " + x + " y = " + y);
	    	}
    	}
    	
    	for(int i = 0; i < N; i++) {
    		kdt.insert(points[i]);
    	}
    	
    	//kdt.draw();
    	
    	StdOut.println("KdTree");
    	kdt.range(new RectHV(.5, .5, .9, .7));
    	
    	for(int i = 0; i < N; i++) {
    		StdOut.println("contains " + points[i] + ": " + kdt.contains(points[i]));
    	}
    	*/
    	
    	In in = new In();
        Out out = new Out();
        int N = in.readInt(), C = in.readInt(), T = 20;
        KdTree tree = new KdTree();
        Point2D [] points = new Point2D[C];
        out.printf("Inserting %d points into tree\n", N);
        for (int i = 0; i < N; i++) {
            tree.insert(new Point2D(in.readDouble(), in.readDouble()));
        }
        out.printf("tree.size(): %d\n", tree.size());
        out.printf("Testing contains method, querying %d points\n", C);
        for (int i = 0; i < C; i++) {
            points[i] = new Point2D(in.readDouble(), in.readDouble());
            out.printf("%s: %s\n", points[i], tree.contains(points[i]));
        }
        /*
        for (int i = 0; i < T; i++) {
            for (int j = 0; j < C; j++) {
                tree.contains(points[j]);
            }
        }
        */
        tree.draw();
        
    }
}
