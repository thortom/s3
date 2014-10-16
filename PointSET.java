package s3;
/****************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    
 *  Dependencies:
 *  Author:
 *  Date:
 *
 *  Data structure for maintaining a set of 2-D points, 
 *    including rectangle and nearest-neighbor queries
 *
 *************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    // construct an empty set of points
	
	SET<Point2D> points;
	    
	public PointSET() 
    {
    	points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() 
    {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() 
    {
        return points.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) 
    {
    	points.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) 
    {
    	return points.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() 
    {
    	StdDraw.setPenColor(StdDraw.BLACK);
    	StdDraw.setPenRadius(.01);
    	
    	for(Point2D t : points)
    	{
    		StdDraw.point(t.x(), t.y());
    	}
    	
    	return;
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) 
    {
    	if(points.isEmpty())
    	{
    		return null;
    	}
    	Queue<Point2D> queue = new Queue<Point2D>();
    	for(Point2D t : points)
    	{
    		if(rect.contains(t))
    		{
    			queue.enqueue(t);
    		}
    	}
        return queue;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) 
    {
    	if(points.isEmpty())
    	{
    		return null;
    	}
    	
    	Point2D p2 = null;
    	double min2 = 2.0;
    	
    	for(Point2D t : points)
    	{
    		double min1 = t.distanceSquaredTo(p);
    		    		
    		if(min1 < min2)
        	{
        		min2 = min1;
        		p2 = t;
        	}
    	}
    	return p2;    	
    }

    public static void main(String[] args) {
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
        PointSET set = new PointSET();
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
        set.draw();
    }

}
