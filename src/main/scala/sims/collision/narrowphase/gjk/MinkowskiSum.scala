package sims.collision.narrowphase
package gjk

import sims.collision._
import sims.math._

class MinkowskiSum(pair: (Collidable, Collidable)) {
	
	def support(direction: Vector2D) = 
		pair._1.support(direction) - pair._2.support(-direction)
		
}