/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims

package object math {
	
	implicit def double2PseudoVector3D(x3: Double) = new PseudoVector3D(x3)
	
	implicit def tuple2Vector[A: Numeric, B: Numeric](t: (A, B)): Vector2D = {
		val x = implicitly[Numeric[A]].toDouble(t._1)
		val y = implicitly[Numeric[B]].toDouble(t._2)
		new Vector2D(x, y)
	}
	
	def mod(x: Int, y: Int): Int = {val r = x % y; if (r < 0) r + y else r}
	
	
}