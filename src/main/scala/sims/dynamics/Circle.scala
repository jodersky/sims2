/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import scala.math.Pi
import sims.collision.{AABB, Projection}
import sims.math._

/** A circle.
  * @define shape circle
  * 
  * @param radius radius of this circle */
case class Circle(radius: Double) extends sims.collision.Circle with Shape {
	
	val area: Double = Pi * radius * radius
	
	lazy val inertia: Double = mass * radius * radius / 2
	
}