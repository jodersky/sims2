/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import sims.math._
import sims.collision.ConvexPolygon

/**A rectangle is a polygon.
  * @define shape rectangle
  * 
  * @param halfWidth this rectangle's half width
  * @param halfHeight this rectangle's half height
  * @param density density of this rectangle */
case class Rectangle(halfWidth: Double, halfHeight : Double) extends ConvexPolygon with Shape {
  
  val area = halfWidth * halfHeight * 4
  
  val inertia = 1.0 / 12.0  * mass * ((2 * halfWidth) * (2 * halfWidth) + (2 * halfHeight) * (2 * halfHeight))
  
  /**Returns the vectors from the center to the vertices of this rectangle.
   * The first vertex is the upper-right vertex at a rotation of 0.
   * Vertices are ordered counter-clockwise.*/
  def halfDiags: Array[Vector2D] = Array(Vector2D(halfWidth, halfHeight),
                                         Vector2D(-halfWidth, halfHeight),
                                         Vector2D(-halfWidth, -halfHeight),
                                         Vector2D(halfWidth, -halfHeight)) map (_ rotate rotation)
  
  /**Returns the position vectors of this rectangle's vertices.
   * The first vertex is the upper-right vertex at a rotation of 0.
   * Vertices are ordered counter-clockwise.*/
  def vertices = for (h <- halfDiags) yield position + h
  
} 