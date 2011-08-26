/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dsl

import sims.dynamics._
import sims.math._

class RichBody(val body: Body) {
	
	def at(point: Vector2D) = new BodyPoint(body, point)
	def atLocal(point: Vector2D) = new BodyPoint(body, body.position + point)
	
	def :@(point: Vector2D) = at(point)
	def :@@(point: Vector2D) = atLocal(point)
	def @:(point: Vector2D) = at(point)
	def @@:(point: Vector2D) = atLocal(point)
	
	def @@(point: Vector2D) = at(point)
}


object Test {
	val b = new Body(null)
	
	
	val q = b :@@ (1.0, 2.0) revolute b
	
}