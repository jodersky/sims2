/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import sims.dynamics._
import sims.dynamics.constraints._

import sims.collision.{Collision => CCollision}

/** A class representing a physical collision,
  * implementing constraints to handle collision response. */
class Collision(collision: CCollision[Shape]) extends Constraining {
	
	
	
	private def getNonPenetrationConstraints() = for (point <- collision.points) yield 
		new Constraint {
			val body1 = collision.item1.body
			val body2 = collision.item2.body
			def v = body2.velocityOfPoint(point) - body1.velocityOfPoint(point) 
			val e = {
				if ((v dot collision.normal.unit) > 0) 0.0
				else if ((v dot collision.normal.unit) > -1) 0.0
				else math.min(collision.item1.restitution, collision.item2.restitution)
			}
			def jacobian = new Jacobian(-collision.normal, -((point - body1.position) cross collision.normal),
																	collision.normal, ((point - body2.position) cross collision.normal))
			
			override def bias = (v dot collision.normal.unit) * e
			def value = -collision.overlap
			override def inequality = true
			override val limit = Some((0.0, Double.PositiveInfinity))
			
			val slop = 0.005
			override def error =
				if (collision.overlap > slop)
					-(collision.overlap - slop)
				else 0.0
	}
	
	val constraints = getNonPenetrationConstraints() 
	
}

object Collision {
	
	/**Converts a collision to a physical collision
	 * (sims.collision.Collision to a sims.dynamics.Collision)*/
	implicit def collision2Physical(c: sims.collision.Collision[Shape]) = new Collision(c)
	
	implicit def collision2Constructor(c: sims.collision.Collision[Shape]) = new { def toPhysical = new Collision(c) }
	
}