package sims.test.gui
package scenes

import sims.math._
import sims.dynamics._

object CloudScene extends Scene {
	override def description = "A cloud of circles."
		
	val MaxItems = 1000
	val MaxItemSize = 0.2
	val Width = 10
	val Height = 10
	
	val random = new scala.util.Random(1234567890)
	def randomCircles(): Seq[Body] = for (i <- 0 until MaxItems) yield {
		val rX = random.nextDouble * Width
		val rY = random.nextDouble * Height
		val c = new Circle(random.nextDouble * MaxItemSize) {
			position = Vector2D(rX, rY)
		}
		new Body(c) {
			linearVelocity = Vector2D(random.nextDouble * (if (random.nextBoolean) 1 else -1), random.nextDouble * (if (random.nextBoolean) 1 else -1))
			angularVelocity = random.nextDouble * (if (random.nextBoolean) 1 else -1) * 10
		}
	}
	
	def frame() = {
		val points = List(Vector2D(-1, -1), Vector2D(11, -1), Vector2D(11, 11), Vector2D(-1, 11))
		for (i <- 0 until points.length) yield {
			val sp = points(i)
			val ep = points((i + 1) % points.length)
			val center = (sp + ep) / 2
			val r = new Rectangle((center - ep).length, 0.2) {
				position = center
				rotation = math.Pi / 2 * i
			}
			new Body(r) {fixed = true}
		}
	}	
	
	override def init() = {
		world.gravity = Vector2D.Null
		for (r <- randomCircles()) world += r
		//for (r <- frame()) world += r
	}
}