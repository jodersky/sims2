/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.math

/**A z-axis aligned vector. Since SiMS is in 2D, 3D vectors are only used as a convenience to simulate operations that only exist
 * in three dimensions such as the cross product.*/
case class PseudoVector3D(z: Double) {
	
	def cross(v: Vector2D): Vector2D = v.leftNormal * z
	
}