/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import scala.math._
import sims.math._

/**A base trait for all linear geometric elements specified by one point and a direction.
 * @throws IllegalArgumentException if the directional vector is the null vector*/
trait Linear {
	
	/**A point contained in this linear element.*/
	val point: Vector2D
	
	/**Direction vector.*/
	val direction: Vector2D
	
	/**Unit directional vector.*/
  lazy val direction0 = direction.unit
  
  /**Right normal vector to <code>direction</code>.*/
  lazy val rightNormal = direction.rightNormal
  
  /**Right normal unit vector to <code>direction</code>.*/
  lazy val rightNormal0 = rightNormal.unit
  
  /**Left normal vector to <code>direction</code>.*/
  lazy val leftNormal = direction.leftNormal
  
  /**Left normal unit vector to <code>direction</code>.*/
  lazy val leftNormal0 = leftNormal.unit
  
  ///**Computes the closest point on this linear element to a given point.*/
  //def closest(point: Vector2D): Vector2D
  
  ///**Computes the shortest distance form this linear element to a given point.*/
  //def distance(point: Vector2D) = (closest(point) - point).length
	
	require(direction != 0, "A linear element's direction cannot be the null vector.")
}

object Linear {
	
	/** Clips a segment passing through points `p1` and `p2` to a half plain
	  * given by a point `p` and a normal (pointing into the plain) `normal`. */
	def clip(p1: Vector2D, p2: Vector2D, p: Vector2D, normal: Vector2D): List[Vector2D] = {
  	val normal0 = normal.unit
  	val direction = p2 - p1
  	
  	val d1 = (p1-p) dot normal0
  	val d2 = (p2-p) dot normal0
  		
  	if (d1 < 0 && d2 < 0) return Nil
  	if (d1 >= 0 && d2 >= 0) return List(p1, p2)
  	
  	val intersection = p1 + direction * abs(d1) / (abs(d1) + abs(d2))
  	val inside = if (d1 > 0) p1 else p2
  		
  	List(inside, intersection)
  }
	
}