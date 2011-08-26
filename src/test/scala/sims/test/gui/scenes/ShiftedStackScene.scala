package sims.test.gui
package scenes

import sims.math._
import sims.dynamics._

object ShiftedStackScene extends Scene {
	override def description = "A stack of shifted rectangles."
	/*override val world = new DebugWorld{
		import sims.collision._
		import sims.collision.narrowphase._
		import sims.collision.broadphase._
		override val detector = SAP[Shape] narrowedBy new sims.test.gjk.GJK[Shape]
	}*/
	
	val width = 1.0
	val height = 0.2
	
	def stack() = for (i <- 0 until 2) yield
		new Body(new Rectangle(width / 2, height / 2) {
			position = Vector2D(0.25 * (i % 2) , i * height)
			restitution = 0.0
		}) {fixed = i == 0}
	
	override def init() = {
		for (s <- stack()) world += s
		world.iterations = 100
	}

}