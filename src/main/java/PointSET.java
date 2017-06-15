import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.*;

public class PointSET {
  private final Set<Point2D> set;
  public PointSET() // construct an empty set of points
  {
    set = new TreeSet<Point2D>();
  }

  public boolean isEmpty() // is the set empty?
  {
    return set.isEmpty();
  }

  public int size() // number of points in the set
  {
    return set.size();
  }

  public void insert(Point2D p) // add the point to the set (if it is not already in the set)
  {
    set.add(p);
  }

  public boolean contains(Point2D p) // does the set contain point p?
  {
    return set.contains(p);
  }

  public void draw() // draw all points to standard draw
  {
  }

  public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle
  {
    List<Point2D> list = new ArrayList<Point2D>();
    for (Point2D q: set){
      if (rect.contains(q)){
        list.add(q);
      }
    }
    return list;
  }

  public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
  {
    Point2D nearest = null;
    double min = 0;
    for (Point2D q: set){
      double distance = p.distanceTo(q);
      if (nearest == null || distance < min){
        min = distance;
        nearest = q;
      }
    }

    return nearest;
  }

  public static void main(String[] args) // unit testing of the methods (optional)
  {
  }
}

