package sims.test.gui
package scenes

import sims.math._
import sims.dynamics._
import sims.dynamics._

object BasicScene extends Scene {
	
	def init() = {
		world.gravity = Vector2D.Null
		val s = new Circle(1)
		world += new Body(s) {linearVelocity = Vector2D(0.1, 0.01); angularVelocity = 1}
		world += new Body(new Rectangle(2,1)) {linearVelocity = Vector2D(0.1, 0.01); angularVelocity = 1}
	}
	

}