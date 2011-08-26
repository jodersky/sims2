/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import scala.math._
import sims.collision.ConvexPolygon
import sims.math._

/**A regular polygon with <code>n</code> sides whose excircle has a radius <code>radius</code>.
  * @define shape regular polygon
  * 
  * @param n number of sides.
  * @param radius radius of the excircle
  * @param density density of this regular polygon
  * @throws IllegalArgumentException if <code>n</code> is smaller than 3 */
case class RegularPolygon(n: Int, radius: Double) extends ConvexPolygon with Shape {
  require(n >= 3, "A polygon must have at least 3 sides.")
  
  /**Height of one of the constituting triangles.*/
  private val h: Double = radius * cos(Pi / n)
  /**Half width of one of the constituting triangles.*/
  private val b: Double = radius * sin(Pi / n)
  
  def halfDiags = (for (i <- (0 until n).toArray) yield (Vector2D(0, radius) rotate (2 * Pi * i / n))) map (_ rotate rotation)
  
  def vertices = for (h <- halfDiags) yield position + h
  
  val area = n * h * b / 2
  
  /**Moment of inertia of one of the constituting triangles about the center of this polygon.*/
  private val Ic: Double = density * b * (3 * b + 16) * pow(h, 4) / 54
  
  val inertia = n * Ic
}
