/**********************************************************************
 *  readme.txt template                                                   
 *  Kd-tree
**********************************************************************/

Name:	Halldor Stefansson
Login:	halldors12
Section instructor: Kristinn Björgvin Árdal

Partner name:	Thor Tomasarson
Partner login:	thortom12
Partner section instructor: Sigurður Rúnar Helgason

/**********************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **********************************************************************/
The implemented Node contains left and right child Nodes, a rectangle (RectHV)
and a point (Point2D). It might be convenient to include an indication of
the linetype of the Node (Horizontal vs. Vertical), but not necessary.

/**********************************************************************
 *  Describe your method for range search in a kd-tree.
 **********************************************************************/
The range search method in our kd-tree uses non-recursive function range(RecthHV)
which returns Queue<Point2D> in the form of a Iterable<Point2D>. We walk down the
tree, if the Node's rectangle intersects the rectangle of interest, one left Node
at a time and stores each Node in a Stack<Node> until we reach the null (end). At
the end we pop the last Node from the stack, examine it (check if in the rectangle)
and put in the Queue<Point2D> if it is in the rectangle of interest. Then we
move one Node to the right and repeat the walk down left Nodes.
When the stack is empty then we are done, and return the Queue<Point2D> of points.

/**********************************************************************
 *  Describe your method for nearest neighbour search in a kd-tree.
 **********************************************************************/
The nearest neighbour search method in our kd-tree uses recursive calls to
recursiveNearest(..) the first call puts the nearest neighbour as the root.
The recursive function checks if the current Node point is closer to
the point of interest than the current-best. If so then update the 
current-best. Then the function checks first for the nearest neighbour on
the points side, point of interest, side of the splitting horizontal/vertical line.
When it comes back from the search on the point side, the function checks
if the other sides rectangle is close enough to the point of interest. To be
able to contain a closer neighbour, then we check there also. This is
done recursively.

/**********************************************************************
 *  Give the total memory usage in bytes (using tilde notation and 
 *  the standard 64-bit memory cost model) of your 2d-tree data
 *  structure as a function of the number of points N. Justify your
 *  answer below.
 *
 *  Include the memory for all referenced objects (deep memory),
 *  including memory for the nodes, points, and rectangles.
 **********************************************************************/

bytes per Point2D: 	32 bytes = 
					(16 bytes overhead) + 2x(8 bytes for coordinates)

bytes per RectHV:	48 bytes =
					(16 bytes overhead) + 4x(8 bytes for coordinates)

bytes per KdTree of N points (using tilde notation):   ~ 	112xN bytes 
															// N for number of nodes in KdTree
					/*
					Node: (16 overhead) + (2x8 bytes Node reference)
							+ (48 bytes RectHV) + (32 bytes Point2D)
					KdTree: (8 + N(16 + 2x8 + 48 + 32) bytes Nodes)
							+ (16 overhead)
							+ (16 overhead + 2x(4 bytes for int) bytes for Lines) 
							~ 112xN bytes
					*/

[include the memory for any referenced Node, Point2D and RectHV objects]


/**********************************************************************
 *  Give the expected running time in seconds (using tilde notation)
 *  to build a 2d-tree on N random points in the unit square.
 *  Use empirical evidence by creating a table of different values of N
 *  and the timing results. (Do not count the time to generate the N 
 *  points or to read them in from standard input.)
 **********************************************************************/

 				N 		Time 	 Ratio
 			   16000    0.015		-
 			   32000    0.035		2.33
 			   64000    0.072		2.05
 			   128000   0.191		2.65
 			   256000   0.503		2.63
 			   512000   1.316		2.72
 			   			Average:	2.48
 	Expected running time of KdTree is:
 		T(N) = aN^b
 			were b = lg(avgRatio) = lg(2.48) = 1.31
 			and T(128000) = a(128000)^b => a = 2.65/(N^1.31) = 5.4*10^-7

 	Ans ->	T(N) = ~5.4*10^-7*N^1.31

 	In the code we have one for-loop that iterates through each point (N)
 	and inserts one at a time. An insert operation takes at the most time
 	proportional to the height of the tree (lgN). That gives T(N) = O(NlgN),
 	which is close to the derived solution T(N) = ~5.4*10^-7*N^1.31.

/**********************************************************************
 *  How many nearest neighbour calculations can your brute-force
 *  implementation perform per second for input100K.txt (100,000 points)
 *  and input1M.txt (1 million points), where the query points are
 *  random points in the unit square? Explain how you determined the
 *  operations per second. (Do not count the time to read in the points
 *  or to build the 2d-tree.)
 *
 *  Repeat the question but with the 2d-tree implementation.
 **********************************************************************/

                     calls to nearest() per second
                     brute force           2d-tree
input100K.txt		1*10^7					8300(5*10^7)
input1M.txt			1.1*10^7				10000(5*10^8)

We started to find the average time to find nearest neighbour. Than we 
divided operations performed with the time, then we have operations per
second. 

In brute-force we similar results for N operations.
In 2d-tree when we used log(N) for the typical case from the lecture 
slides, we very low results. But when we used worst case, N, we got 
results more similar the brute-force results, but higher.

We believe from that results that have don't have the typical case but 
more like the worst case. 

/**********************************************************************
 *  Known bugs / limitations.
 **********************************************************************/
None that we can think of.

/**********************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and daematimar, but do
 *  include any help from people (including course staff, 
 *  classmates, and friends) and attribute them by name.
 **********************************************************************/
No help received.

/**********************************************************************
 *  Describe any serious problems you encountered.                    
 **********************************************************************/
Difficulties implementing nearest neighbour in KdTree. We started out 
with a recursive attempt with nearest(). It kept giving us some errors
and it grew out of bounds. When the recursive function nearest() had
become 100 lines of code. We took a step back and restructured the
hole function. See private Point2D nearest(Node child, Line line, Point2D p)
for the old code.

/**********************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **********************************************************************/
Halldor worked mostly on PointSET and the nearest function in KdTree.
Thor worked mostly on KdTree.
Both contributed to readme.


/**********************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **********************************************************************/
 We learned a lot on how to debug well with eclipse, and enjoyed the assignment also.
