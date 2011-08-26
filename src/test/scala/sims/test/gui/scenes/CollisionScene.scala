package sims.test.gui
package scenes

import sims.dynamics._
import sims.math._
import sims.dynamics._

object CollisionScene extends Scene {
	override def description = "A basic collision detection test."
	
	var c1 = (new Circle(1) {restitution = 1.0}).asBody
	var c2 = (new Circle(1) {position = Vector2D(3, 0)}).asBody
	var r1 = (new Rectangle(0.5, 0.5) {position = Vector2D(6,0)}).asBody
	
	def init() = {
		c1 = (new Circle(1) {restitution = 1.0}).asBody
		c2 = (new Circle(1) {position = Vector2D(3, 0); restitution = 1.0}).asBody
		r1 = (new Rectangle(0.5, 0.5) {position = Vector2D(6,0)}).asBody
		
		c1.linearVelocity = Vector2D(1, 0)
	
		world.gravity = Vector2D(0, 0)
		world += c1
		world += c2
		world += r1
		
		reactions += {
			case Stepped(`world`) =>
				println(
					"p: " + (c1.linearMomentum.length + c2.linearMomentum.length + r1.linearMomentum.length).toFloat + 
					"\tE: " + (E(c1) + E(c2) + E(r1)).toFloat
					)
		}
	}
	
	
	def E(b: Body) = {
		(b.linearVelocity dot b.linearVelocity) * b.mass / 2
	}
		
}