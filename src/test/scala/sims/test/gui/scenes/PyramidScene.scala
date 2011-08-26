package sims.test.gui
package scenes

import sims.math._
import sims.dynamics._
import sims.dynamics._

object PyramidScene extends Scene {
	override def description = "A pyramid made out of circles."
	
	val Base = 40
	var Radius = 0.2
	
	val s = math.sqrt(3)
	
	def pyramid: Seq[Body] = (for (i <- 0 until Base) yield {
		for (j <- 0 until Base - i) yield new Body(
			new Circle(Radius) {
				position = Vector2D(2 * j * Radius + i * Radius, s * i * Radius)
				restitution = 0.5
			}
		) {fixed = (i == 0)}
	}).flatten
		
	override def init() = {
		//world.gravity = Vector2D.Null
		for (b <- pyramid) world += b
	
		val b = new Circle(0.3) {
			override val density = 10.0
			position = Vector2D(4,15)
		}
		val bd = new Body(b){
			linearVelocity = Vector2D(0, -2.5)
		}
	
		world += bd
	}

}