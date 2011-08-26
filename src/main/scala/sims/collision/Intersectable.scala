/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math._

trait Intersectable[A <: Linear] {
	
	/**Computes the intersection between this linear element and `l`.
	 * The intersection method does <b>not</b> correspond to the geometrical intersection.
	 * Let A and B be two linear elements,
	 * 
	 * A and B intersect (i.e. an intersection point exists) \u21d4 card(A \u22c2 B) = 1
	 * */
	def intersection(l: A): Option[Vector2D] 

}