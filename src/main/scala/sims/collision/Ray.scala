/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import scala.math._
import sims.math._

/**A ray.
 * @param point starting point of this ray
 * @param direction this ray's directional vector
 * @throws IllegalArgumentException if the directional vector is the null vector*/
case class Ray(point: Vector2D, direction: Vector2D)
	extends Linear
	with Intersectable[Segment] {
  
	/*def closest(point: Vector2D) = {
		var t = ((point - this.point) dot (direction)) / (direction dot direction)
  	if (t < 0) t = 0
  	this.point + direction * t
	}*/
	
	def intersection(segment: Segment): Option[Vector2D] = {
		
		val n = segment.leftNormal
  	
  	// Handle case when two segments parallel
  	if ((n dot direction) == 0) None
  	else {
  		val t = (n dot (segment.point1 - point)) / (n dot direction)
  		val i = point + direction * t
  		if (0 <= t && (i - segment.point1).length <= segment.length) Some(i)
  		else None
  	}
		/*
		// Returns 2 times the signed triangle area. The result is positive if
  	// abc is ccw, negative if abc is cw, zero if abc is degenerate.
  	def signed2DTriArea(a: Vector2D, b: Vector2D, c: Vector2D) = {
  		(a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
  	}
  	
  	if (signed2DTriArea(point, point + direction, segment.point1) * signed2DTriArea(point, point + direction, segment.point2) < 0) {
  		val ab = segment.point2 - segment.point1
  		val ac = segment.point2 - point
  		val t = (ac.x * ab.y - ac.y * ab.x) / (direction.y * ab.x - direction.x - ab.y)
  		if (t >= 0) Some(point + direction * t) else None
  	} else None*/
	}
	
  /**Checks if this ray contains the point <code>p</code>.*/
  def contains(p: Vector2D) = {
    val v = p - point
    p == point ||
    v ~ direction &&
    signum(direction.x) == signum(v.x) &&
    signum(direction.y) == signum(v.y)
  }
}
