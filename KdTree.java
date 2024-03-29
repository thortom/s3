package s3;
/*************************************************************************
 *************************************************************************/



import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdRandom;

public class KdTree {
	private Node root;             // root of BST
	private static enum Line {VERTICAL, HORIZONTAL};

    private static class Node {
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
    	root = insert(p, root, null, Line.VERTICAL); 			// null is used for no parent
    };
 
    // recursive helper function for adding a point
    private Node insert(Point2D p, Node child, Node parent, Line line) {
    	if(child == null) {
    		if(child == parent)	return new Node(p, 0, 0, 1, 1); 				// This is the root
    		if(line == Line.HORIZONTAL) { 										// The parents line is VERTICAL here
        		if		(p.x() < parent.p.x()) return new Node(p, parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
        		else if	(p.x() >= parent.p.x()) return new Node(p, parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
        	}
    		else if(line == Line.VERTICAL) { 									// The parents line is HORIZONTAL here
        		if		(p.y() < parent.p.y()) return new Node(p, parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
        		else if	(p.y() >= parent.p.y()) return new Node(p, parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
        	}
    	}
    	if(p.equals(child.p)) 		// Avoid adding duplicated points
    	{
    		return child;
    	}
    	if(line == Line.VERTICAL) {
    		if		(p.x() < child.p.x())  child.left = insert(p, child.left, child, Line.HORIZONTAL);
    		else if	(p.x() >= child.p.x()) child.right = insert(p, child.right, child, Line.HORIZONTAL);
    	}
    	else if(line == Line.HORIZONTAL) {
    		if		(p.y() < child.p.y())  child.left = insert(p, child.left, child, Line.VERTICAL);
    		else if	(p.y() >= child.p.y()) child.right = insert(p, child.right, child, Line.VERTICAL);
    	}
    	return child;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(root, Line.VERTICAL, p);
    }
    
    // recursive helper function for checking if point p is in the set
    private boolean contains(Node x, Line line, Point2D p) {
    	
    	if(x == null) 			return false;
    	if (p.equals(x.p)) 		return true;
    	if(line == Line.VERTICAL) {
    		if		(p.x() < x.p.x()) 		return contains(x.left, Line.HORIZONTAL, p);
    		else if	(p.x() >= x.p.x()) 		return contains(x.right, Line.HORIZONTAL, p);
    	}
    	else if(line == Line.HORIZONTAL) {
    		if		(p.y() < x.p.y()) 		return contains(x.left, Line.VERTICAL, p);
    		else if	(p.y() >= x.p.y()) 		return contains(x.right, Line.VERTICAL, p);
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
    
    // recursive helper function for drawing all the points
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
    	Queue<Point2D> queue = new Queue<Point2D>();
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
    	    			queue.enqueue(current.p);

    	    		current = current.right;
    	    	}
    	    	else
    	    		done = true; 
    	    }
    	}
    	
        return queue;
    }
    /* ########### We do not use #########################
     * Failed attempt at making a nearest neighbor search.
     * Cost us many hours of work
     * ###################################################
     * 
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest2(Point2D p) 
    {
    	if(root == null)
    	{
    		return null;
    	}
    	else
    	{
    		return nearest(root, Line.VERTICAL, p);
    	}
    }
    
    private Point2D nearest(Node child, Line line, Point2D p)
    {
    	   
    	if(child == null) 
    	{
    		return null;
    	}
    		
    	if(p.equals(child.p))
    	{
    		return child.p;
    	}
    	
    	Point2D best = child.p;
    	
    	if(line == Line.VERTICAL)
    	{
    		if(p.x() < child.p.x())
    		{ 
    			if(child.left != null)
        		{
    				child.left.p = nearest(child.left, Line.HORIZONTAL, p);
		 			
            		if(child.left.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
            		{
            			best = child.left.p;
            		}
        		} 
    			
        		if(best.distanceSquaredTo(p) > (child.p.x() - p.x()))
        		{
        			if(child.right != null)
            		{
        				child.right.p = nearest(child.right, Line.HORIZONTAL, p);
           			 
                		if(child.right.p.distanceSquaredTo(p) < best.distanceSquaredTo(p))
                    	{
                    		best = child.right.p;
                    	}
            		}
       			}
        		return best;
    		}
    		else if	(p.x() >= child.p.x())
    		{
    			if(child.right != null)
        		{
    				child.right.p = nearest(child.right, Line.HORIZONTAL, p);
    			 	
            		if(child.right.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
            		{
            			best = child.right.p;
            		}
        		}
    			
        		if(best.distanceSquaredTo(p) > (p.x() - child.p.x()))
        		{
        			if(child.left != null)
            		{
        				child.left.p = nearest(child.left, Line.HORIZONTAL, p);
    					
                		if(child.left.p.distanceSquaredTo(p) < best.distanceSquaredTo(p))
                		{
                			best = child.left.p;
                		}
            		}         			
    			}
        		return best;
    		}
    	}
    	else if(line == Line.HORIZONTAL)
    	{
    		if(p.y() < child.p.y())
    		{
    			if(child.left != null)
        		{
    				child.left.p = nearest(child.left, Line.VERTICAL, p);
    				
            		if(child.left.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
            		{
           				best = child.left.p;
           			}
        		} 
    			
       			if(best.distanceSquaredTo(p) > (child.p.y()-p.y()))
       			{
       				if(child.right != null)
            		{
       					child.right.p = nearest(child.right, Line.VERTICAL, p);
    			 		
                		if(child.right.p.distanceSquaredTo(p) < best.distanceSquaredTo(p))
              			{
               				best = child.right.p;
               			}
            		}        			
    			}
       			return best;
    		}
    		else if	(p.y() >= child.p.y())
    		{
    			if(child.right != null)
        		{
    				child.right.p = nearest(child.right, Line.VERTICAL, p);
    				
            		if(child.right.p.distanceSquaredTo(p) < child.p.distanceSquaredTo(p))
            		{
            			best = child.right.p;
            		}
        		} 
    			
       			if(best.distanceSquaredTo(p) > (p.y() - child.p.y()))
       			{
       				if(child.left != null)
            		{
       					child.left.p = nearest(child.left, Line.VERTICAL, p);
    			 		
                		if(child.left.p.distanceSquaredTo(p) < best.distanceSquaredTo(p))
                		{
          					best = child.left.p;
           				}
            		}
        		}
       			return best;
    		}
    	}
    	return best;
    }
    ################################################ */
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) 
    {
    	return recursiveNearest(root, Line.VERTICAL, p, null); 		// best point is null here
    }
    
    // recursive helper function for neighbor search 
    private Point2D recursiveNearest(Node current, Line line, Point2D p, Point2D best) {
    	Point2D second = null;
    	Point2D first = null;
    	
    	if (current == null) 	 return null;
    	if (p.equals(current.p)) return current.p;		// stop if point p is found
    	if (best == null) 		 best = current.p; 		// first call to the function, set root.p as -> best
    	else if (p.distanceSquaredTo(current.p) < p.distanceSquaredTo(best))
    		best = current.p;
    	
    	// four cases that are nearly the same...
    	// 		Vertical splitting line vs. Horizontal
    	// 		point of interest to the right/left or up/down
    	if (line == Line.VERTICAL) {
    		if(p.x() < current.p.x()) {
    			first = recursiveNearest(current.left, Line.HORIZONTAL, p, best);
    			if (first != null && first != best)
    				if (p.distanceSquaredTo(first) < p.distanceTo(best))
    					best = first;
    			if (current.right != null && current.right.rect.distanceSquaredTo(p) < best.distanceSquaredTo(p))
    				second = recursiveNearest(current.right, Line.HORIZONTAL, p, best);
    		}
    		else {
    			first = recursiveNearest(current.right, Line.HORIZONTAL, p, best);
    			if (first != null && first != best)
    				if (p.distanceSquaredTo(first) < p.distanceSquaredTo(best))
    					best = first;
    			if (current.left != null && current.left.rect.distanceSquaredTo(p) < best.distanceSquaredTo(p))
    				second = recursiveNearest(current.left, Line.HORIZONTAL, p, best);
    		}
    	}
    	else if (line == Line.HORIZONTAL) {
    		if(p.y() < current.p.y()) {
    			first = recursiveNearest(current.left, Line.VERTICAL, p, best);
    			if (first != null && first != best)
    				if (p.distanceSquaredTo(first) < p.distanceTo(best))
    					best = first;
    			if (current.right != null && current.right.rect.distanceSquaredTo(p) < best.distanceSquaredTo(p))
    				second = recursiveNearest(current.right, Line.VERTICAL, p, best);
    		}
    		else {
    			first = recursiveNearest(current.right, Line.VERTICAL, p, best);
    			if (first != null && first != best)
    				if (p.distanceSquaredTo(first) < p.distanceSquaredTo(best))
    					best = first;
    			if (current.left != null && current.left.rect.distanceSquaredTo(p) < best.distanceSquaredTo(p))
    				second = recursiveNearest(current.left, Line.VERTICAL, p, best);
    		}
    	}
    	if (second != null) 	return second;
    	else if (first != null) return first;
    	else 					return best;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
    	
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
	    		double x = StdRandom.uniform(0, N)/(N+.0);
	    		double y = StdRandom.uniform(0, N)/(N+.0);
	    		points[i] = new Point2D(x, y);
	    		
	    		//StdOut.println("x = " + x + " y = " + y);
	    	}
    	}
    	
    	for(int i = 0; i < N; i++) {
    		kdt.insert(points[i]);
    	}
    	
        kdt.draw();

    	
//    	In in = new In();
//    	Out out = new Out();    
//    	int N = in.readInt(), C = in.readInt(), T = 50;
//    	Point2D[] queries = new Point2D[C];
//    	KdTree tree = new KdTree();
//    	out.printf("Inserting %d points into tree\n", N);
//    	for (int i = 0; i < N; i++) {
//    	    tree.insert(new Point2D(in.readDouble(), in.readDouble()));
//    	}
//    	out.printf("tree.size(): %d\n", tree.size());
//    	out.printf("Testing `nearest` method, querying %d points\n", C);
//
//    	for (int i = 0; i < C; i++) {
//    	    queries[i] = new Point2D(in.readDouble(), in.readDouble());
//    	    out.printf("%s: %s\n", queries[i], tree.nearest(queries[i]));
//    	}
//    	for (int i = 0; i < T; i++) {
//    	    for (int j = 0; j < C; j++) {
//    	        tree.nearest(queries[j]);
//    	    }
//    	}
    	
//    	KdTree kdtree = new KdTree();
//    	
//    	for(int i = 0; i < 1000000; i++)
//    	{
//    		double x = StdRandom.uniform(0.0, 1.0)/1.0;
//    		double y = StdRandom.uniform(0.0, 1.0)/1.0;
//    		Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//    	}
//    	double time;
//    	double t2 = 0;
//    	for(int i = 0; i < 10; i++)
//    	{
//    		double x = StdRandom.uniform(0.0, 1.0)/1.0;
//    		double y = StdRandom.uniform(0.0, 1.0)/1.0;
//    		Point2D query = new Point2D(x, y);
//    		
//    		Stopwatch timer = new Stopwatch();
//    		kdtree.nearest(query);
//    		time = timer.elapsedTime();
//    		BigDecimal decimal = new BigDecimal(time);
//    		t2 += decimal.doubleValue();
//    		StdOut.println(10 + "    " + timer.elapsedTime());
//    	}
//    	
//    	StdOut.println("Timi er : " + t2/10);


    }
}
