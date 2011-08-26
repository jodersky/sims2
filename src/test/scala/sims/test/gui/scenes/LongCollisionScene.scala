package sims.test.gui
package scenes

import sims.dynamics._
import sims.math._
import sims.dynamics._

object LongCollisionScene extends Scene {
	override def description = "A test to verify conservation in a collision."
	
	def makeBodies() = (for (i <- 0 until 10) yield {
		new Circle(0.2) {
			position = Vector2D(0 + 2.01 * radius * i, 0)
			restitution = 0.8
		}
	}.asBody) ++ (for (i <- 0 until 10) yield {
		new Circle(0.2) {
			position = Vector2D(6 + 2.01 * radius * i, 0)
			restitution = 0.8
		}
	}.asBody)
		
	override def init() = {
		val bodies = makeBodies()
		bodies(0).fixed = true
		bodies(19).fixed = true
		for (b <- bodies) world += b
		world.gravity = Vector2D.Null
		
		val bullet = new Body(new Circle(0.2) {
			position = Vector2D(5, 0)
			restitution = 0.8}) {
			linearVelocity = Vector2D(3, 0)
		}
		world += bullet
		
		world.iterations = 10
		
		registerListeners()
	}
	
	def registerListeners() = {
		reactions += {
			case Stepped(`world`) => println(
					"P: " + world.bodies.map(P(_)).sum.toFloat + 
					"\tE: " + world.bodies.map(E(_)).sum.toFloat
				)
		}
	}
	
	def P(b: Body) = if (b.fixed) 0 else b.linearMomentum.length
	def E(b: Body) = if (b.fixed) 0 else (b.linearVelocity dot b.linearVelocity) * b.mass * 0.5

}