import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.util.*;

public class KdTree {
  private static class Node {
    private final Point2D p;      // the point
    private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
    private final boolean isBlue;
    private Node lb;        // the left/bottom subtree
    private Node rt;        // the right/top subtree

    static void test(){
      Node n = new Node(null, new Point2D(.7, .2));
      assert(n.leftRect().xmin() == 0);
      assert(n.leftRect().ymin() == 0);
      assert(n.leftRect().xmax() == .7);
      assert(n.leftRect().ymax() == 1);

      assert(n.rightRect().xmin() == .7);
      assert(n.rightRect().ymin() == 0);
      assert(n.rightRect().xmax() == 1);
      assert(n.rightRect().ymax() == 1);

      assert(n.lowerRect().xmin() == 0);
      assert(n.lowerRect().ymin() == 0);
      assert(n.lowerRect().xmax() == 1);
      assert(n.lowerRect().ymax() == .2);

      assert(n.upperRect().xmin() == 0);
      assert(n.upperRect().ymin() == .2);
      assert(n.upperRect().xmax() == 1);
      assert(n.upperRect().ymax() == 1);

      Node m = new Node(n, new Point2D(.5, .4));
      assert(m.lowerRect().xmin() == 0);
      assert(m.lowerRect().ymin() == 0);
      assert(m.lowerRect().xmax() == .7);
      assert(m.lowerRect().ymax() == .4);

      assert(m.upperRect().xmin() == 0);
      assert(m.upperRect().ymin() == .4);
      assert(m.upperRect().xmax() == .7);
      assert(m.upperRect().ymax() == 1);
    }

    Node(Node parent, Point2D p){
      if (parent == null){
        isBlue = false;
        rect = new RectHV(0,0,1,1);
      }else{
        isBlue = !parent.isBlue;
        if (isBlue){
          rect = p.x() < parent.p.x() ? parent.leftRect() : parent.rightRect();
        }
        else{
          rect = p.y() < parent.p.y() ? parent.lowerRect() : parent.upperRect();
        }
      }
      println(rect);
      this.p = p;
    }

    RectHV leftRect(){
      return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
    }

    RectHV rightRect(){
      return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
    }

    RectHV lowerRect(){
      return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
    }

    RectHV upperRect(){
      return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
    }

    Point2D p1(){
      if (isBlue){
        return new Point2D(rect.xmin(), p.y());
      }
      else{
        return new Point2D(p.x(), rect.ymin());
      }
    }

    Point2D p2(){
      if (isBlue){
        return new Point2D(rect.xmax(), p.y());
      }
      else{
        return new Point2D(p.x(), rect.ymax());
      }
    }
  }

  private Node root;
  private int size;

  public KdTree() // construct an empty set of points
  {
    // Node.test();
  }

  public boolean isEmpty() // is the set empty?
  {
    return size == 0;
  }

  public int size() // number of points in the set
  {
    return size;
  }

  private Node put(Node parent, Node x, Point2D p)
  {
    if (x == null){
      size++;
      return new Node(parent, p);
    }

    double cmp;
    if (x.p.equals(p)){
      return x;
    }

    if (x.isBlue){
      cmp = p.y() - x.p.y();
    }
    else{
      cmp = p.x() - x.p.x();
    }
    if (cmp < 0){
      x.lb = put(x, x.lb, p);
    }
    else{
      x.rt = put(x, x.rt, p);
    }
    return x;
  }

  public void insert(Point2D p) // add the point to the set (if it is not already in the set)
  {
    root = put(null, root, p);
  }

  private Node get(Point2D p){
    Node x = root;
    while (x != null)
    {
      if (x.p.equals(p)){
        return x;
      }
      double cmp;
      if (x.isBlue){
        cmp = p.y() - x.p.y();
      }
      else{
        cmp = p.x() - x.p.x();
      }
      if (cmp < 0) x = x.lb;
      else x = x.rt;
    }
    return null;
  }

  public boolean contains(Point2D p) // does the set contain point p?
  {
    return get(p) != null;
  }

  private void draw(Node x){
    if (x == null){
      return;
    }
    StdDraw.setPenRadius(0.05);
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.point(x.p.x(), x.p.y());

    StdDraw.setPenRadius(0.01);
    StdDraw.setPenColor(x.isBlue ? StdDraw.BLUE : StdDraw.RED);
    Point2D p1 = x.p1();
    Point2D p2 = x.p2();
    StdDraw.line(p1.x(), p1.y(), p2.x(), p2.y());
    draw(x.lb);
    draw(x.rt);
  }

  public void draw() // draw all points to standard draw
  {
    draw(root);
  }

  static private void println(Object o){
    // System.out.println(o);
  }

  private void range(List<Point2D> list, Node x, RectHV rect){
    if (x == null){
      return;
    }

    if (rect.intersects(x.rect)){
      if (rect.contains(x.p)){
        list.add(x.p);
      }
      range(list, x.lb, rect);
      range(list, x.rt, rect);
    }
  }

  public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle
  {
    List<Point2D> list = new ArrayList<Point2D>();
    range(list, root, rect);

    return list;
  }

  private class Nearest{
    Point2D p;
    double distance;
  }

  private Nearest nearest(Nearest nearest, Node x, Point2D p){
    if (x == null){
      return nearest;
    }

    if (nearest.p == null){
      nearest.p = x.p;
      nearest.distance = p.distanceSquaredTo(nearest.p);
    }else{
      double distanceX = p.distanceSquaredTo(x.p);
      if (distanceX < nearest.distance){
        nearest.p = x.p;
        nearest.distance = distanceX;
      }else if (x.rect.distanceSquaredTo(p) >= nearest.distance){
        return nearest;
      }
    }

    if (x.isBlue){
      if (p.y() < x.p.y()){
        nearest = nearest(nearest, x.lb, p);
        nearest = nearest(nearest, x.rt, p);
      }
      else{
        nearest = nearest(nearest, x.rt, p);
        nearest = nearest(nearest, x.lb, p);
      }
    }
    else{
      if (p.x() < x.p.x()){
        nearest = nearest(nearest, x.lb, p);
        nearest = nearest(nearest, x.rt, p);
      }
      else{
        nearest = nearest(nearest, x.rt, p);
        nearest = nearest(nearest, x.lb, p);
      }
    }

    return nearest;
  }

  public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
  {
    return nearest(new Nearest(), root, p).p;
  }

  public static void main(String[] args) // unit testing of the methods (optional)
  {
  }
}

