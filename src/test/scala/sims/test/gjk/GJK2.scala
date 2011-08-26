package sims.test.gjk

/**
 * Created by IntelliJ IDEA.
 * User: jakob
 * Date: 3/27/11
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */

import scala.collection.mutable.ListBuffer
import sims.math._
import sims.collision._
import sims.collision.narrowphase.NarrowPhaseDetector

case class Separation(distance: Double, normal: Vector2D, point1: Vector2D, point2: Vector2D)
class GJK2[A <: Collidable: ClassManifest] extends NarrowPhaseDetector[A] {

  val margin = 0.1

  case class MinkowskiPoint(convex1: Collidable, convex2: Collidable, direction: Vector2D) {
    val point1 = convex1.support(direction) - direction.unit * margin
    val point2 = convex2.support(-direction) + direction.unit * margin
    val point = point1 - point2
  }
  
  def support(c1: A, c2: A, direction: Vector2D) = MinkowskiPoint(c1, c2, direction)
  implicit def minkowsi2Vector(mp: MinkowskiPoint) = mp.point

  def separation(c1: A, c2: A): Option[(Separation)] = {

    //initial search direction
    val direction0 = Vector2D.i

    //simplex points
    var a = support(c1, c2, direction0)
    var b = support(c1, c2, -direction0)

    var counter = 0
    while (counter < 100) {

      //closest point on the current simplex closest to origin
      val point = segmentClosestPoint(a, b,Vector2D.Null)

      if (point.isNull) return None

      //new search direction
      val direction = -point.unit

      //new Minkowski Sum point
      val c = support(c1, c2, direction)

      if (containsOrigin(a, b, c)) return None

      val dc = (direction dot c)
      val da = (direction dot a)

      if (dc - da < 0.0001) {
        val (point1, point2) = findClosestPoints(a, b)
        return Some(Separation(dc, direction, point1, point2))
      }

      if (a.lengthSquare < b.lengthSquare) b = c
      else a = c
      //counter += 1
    }
    return None
  }


  def findClosestPoints(a: MinkowskiPoint, b: MinkowskiPoint): (Vector2D, Vector2D) =  {
		var p1 = Vector2D.Null
		var p2 = Vector2D.Null

		// find lambda1 and lambda2
		val l: Vector2D = b - a

		// check if a and b are the same point
		if (l.isNull) {
			// then the closest points are a or b support points
			p1 = a.point1
			p2 = a.point2
		} else {
			// otherwise compute lambda1 and lambda2
			val ll = l dot l;
			val l2 = -l.dot(a) / ll;
			val l1 = 1 - l2;

			// check if either lambda1 or lambda2 is less than zero
			if (l1 < 0) {
				// if lambda1 is less than zero then that means that
				// the support points of the Minkowski point B are
				// the closest points
				p1 = b.point1
				p2 = b.point2
			} else if (l2 < 0) {
				// if lambda2 is less than zero then that means that
				// the support points of the Minkowski point A are
				// the closest points
				p1 = a.point1
				p2 = a.point2
			} else {
				// compute the closest points using lambda1 and lambda2
				// this is the expanded version of
				// p1 = a.p1.multiply(l1).add(b.p1.multiply(l2));
				// p2 = a.p2.multiply(l1).add(b.p2.multiply(l2));
        p1 = a.point1 * l1 + b.point1 * l2
        p2 = a.point2 * l1 + b.point2 * l2
			}
		}

    (p1, p2)
  }

  def segmentClosestPoint(a: Vector2D, b: Vector2D, point: Vector2D): Vector2D = {
    if (a == b) return a
    val direction = b - a
    var t = ((point - a) dot (direction)) / (direction dot direction)
    if (t < 0) t = 0
  	if (t > 1) t = 1
  	a + direction * t
  }

  def containsOrigin(a: Vector2D, b: Vector2D, c: Vector2D): Boolean =  {
		val sa = a.cross(b);
		val sb = b.cross(c);
		val sc = c.cross(a);
		// this is sufficient (we do not need to test sb * sc)
		sa * sb  > 0 && sa * sc > 0
	}

  def collision(pair: (A, A)): Option[Collision[A]] = {
    pair match {
      case (c1: Circle, c2: Circle) => collisionCircleCircle(c1, c2)(pair)
      case _ => gjkCollision(pair)
    }
  }

  private def gjkCollision(pair: (A, A)): Option[Collision[A]] = {
    val so = separation(pair._1, pair._2)
    if (so.isEmpty) return None //deep contact is not implemented yet
    val s = so.get
    Some(new Collision[A] {
      val item1 = pair._1
      val item2 = pair._2
      val overlap = -(s.distance - 2 * margin)
      val points = List(s.point1)
      val normal = s.normal.unit
    })

  }

  private def collisionCircleCircle(c1: Circle, c2: Circle)(pair: (A, A)) = {
		val d = (c2.position - c1.position)
		val l = d.length
		if (l <= c1.radius + c2.radius) {
			val p = c1.position + d.unit * (l - c2.radius)
			Some(new Collision[A] {
				val item1 = pair._1
				val item2 = pair._2
				val normal = d.unit
				val points = List(p)
				val overlap = (c1.radius + c2.radius - l)
			})
		} else None
	}

}