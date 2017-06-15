import edu.princeton.cs.algs4.*
import org.junit.Test
import org.junit.Assert.*
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import edu.princeton.cs.algs4.*

class TestPointSET{
  @Test fun testPointSET(){
    val ps = PointSET()
    assertEquals(true, ps.isEmpty())
    ps.insert(Point2D(0.0,0.0))
    assertEquals(false, ps.isEmpty())
  }
}

class TestKdTree{
  @Test fun testKdTree(){
    val kt = KdTree()
    assertEquals(true, kt.isEmpty())
    kt.insert(Point2D(0.0,0.0))
    assertEquals(false, kt.isEmpty())
  }

  @Test fun testSet(){
    val ps = PointSET()
    val kt = KdTree()
    for (i in 1..10000){
      StdRandom.uniform()
      val p = Point2D(StdRandom.uniform(), StdRandom.uniform())
      val q = Point2D(1.0 / i, 1.0 / i)
      ps.insert(p)
      kt.insert(p)
      assertEquals(ps.size(), kt.size())

      ps.insert(q)
      kt.insert(q)
      assertEquals(true, ps.contains(p))
      assertEquals(true, kt.contains(p))
      assertEquals(true, ps.contains(q))
      assertEquals(true, kt.contains(q))
      assertEquals(ps.size(), kt.size())
    }
  }

  @Test fun testRange(){
    val kt = KdTree()
    kt.insert(Point2D(.7, .2))
    // 0,0, 10,10
    assertEquals(true, kt.range(RectHV(.0,.0,1.0,1.0)).contains(Point2D(.7, .2)))
    kt.insert(Point2D(.5, .4))
    // 0,0, 7,10
    assertEquals(true, kt.range(RectHV(.0,.0,1.0,1.0)).contains(Point2D(.7, .2)))
    assertEquals(true, kt.range(RectHV(.0,.0,1.0,1.0)).contains(Point2D(.5, .4)))
    kt.insert(Point2D(.2, .3))
    // 0,0, 7,4
    kt.insert(Point2D(.4, .7))
    // 0,4, 7,10
  }

  @Test fun testIntersects(){
    assertEquals(false, RectHV(.9,.9,1.0,1.0).intersects(RectHV(.1,.1,.2,.2)))
    assertEquals(true, RectHV(.0,.0,1.0,1.0).intersects(RectHV(.1,.1,.2,.2)))
    assertEquals(true, RectHV(.0,.0,1.0,1.0).intersects(RectHV(.1,.1,1.2,1.2)))
  }
}

