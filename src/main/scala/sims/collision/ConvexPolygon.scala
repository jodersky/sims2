/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math._

/** Common properties of all convex polygons.
  * '''Note: convex polygons are not verified to be convex. It is up to the client to ensure this.''' */
trait ConvexPolygon extends Polygon {
	
	   /**Returns the projection of this polygon onto the line given by the directional vector <code>axis</code>.
   * @param axis directional vector of the line
   * @return projection of this polygon*/
  override def project(axis: Vector2D) = {
  	val dir = axis.unit
  	var min = vertices(0) dot dir
  	var max = vertices(0) dot dir
  	
  	for (v <- vertices) {
  		val d = v dot dir
  		if (d < min) min = d
  		if (d > max) max = d
  	}
  	
    new Projection(axis, min, max)
  }
  
  /**Checks if the point <code>point</code> is contained in this polygon.
   * <p>
   * A ray is created, originating from the point and following an arbitrary direction (X-Axis was chosen).
   * The number of intersections between the ray and this polygon's sides (including vertices) is counted.
   * The amount of intersections with vertices is subtracted form the previous number.
   * If the latter number is odd, the point is contained in the polygon.*/
  override def contains(point: Vector2D) = {
    val r = new Ray(point, Vector2D.i)
    var intersections = 0
    for (s <- sides; if !(r intersection s).isEmpty) intersections += 1
    for (v <- vertices; if (r contains v)) intersections -= 1
    intersections % 2 != 0
  }
  
  override def support(direction: Vector2D) = {
  	var maxDistance = vertices(0) dot direction
		var maxPoint = vertices(0)
		for (v <- vertices) {
			val s = v dot direction
			if (s > maxDistance) {
				maxDistance = s
				maxPoint = v
			}
		}
		maxPoint
	}
  
}