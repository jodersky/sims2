package sims.dynamics

import sims.collision.{Collision => CCollision}
import sims.dynamics.constraints._
import sims.math._

class RestingPoint(collision: CCollision[Shape], point: Vector2D) extends Constraint {
	import collision._
	val body1 = item1.body
	val body2 = item2.body
	
	def relativeVelocity = body2.velocityOfPoint(point) - body1.velocityOfPoint(point)
	
	override def inequality = true
	
	override val limit = Some((0.0, Double.PositiveInfinity))
	
	override def value = -overlap
	
	override def jacobian = 
		new Jacobian(-normal.unit, -((point - body1.position) cross normal.unit),
								 normal.unit,  ((point - body2.position) cross normal.unit))
			
	val slop = 0.005
	override def error =
		if (collision.overlap > slop)
			-(collision.overlap - slop)
		else 0.0
}

class ImpactPoint(collision: CCollision[Shape], point: Vector2D) extends Constraint {
	import collision._
	val body1 = item1.body
	val body2 = item2.body
	
	def relativeVelocity = body2.velocityOfPoint(point) - body1.velocityOfPoint(point)
	
	override def inequality = true
	
	override val limit = Some((0.0, Double.PositiveInfinity))
	
	override def value = -overlap
	
	override def jacobian = 
		new Jacobian(-normal.unit, -((point - body1.position) cross normal.unit),
								 normal.unit,  ((point - body2.position) cross normal.unit))
	
	val restitution = math.min(collision.item1.restitution, collision.item2.restitution)
	override def bias = (relativeVelocity dot collision.normal.unit) * restitution
			
	override def error = 0
}

object ContactResolver {
	
	def relativeVelocity(collision: CCollision[Shape], point: Vector2D) = 
		collision.item2.body.velocityOfPoint(point) - collision.item2.body.velocityOfPoint(point)
	
	def resolve(collision: CCollision[Shape]): Seq[Constraint] = for (p <- collision.points) yield {
		val v = (relativeVelocity(collision, p) dot collision.normal.unit)
		if (v < -1) new RestingPoint(collision, p)
		else new ImpactPoint(collision, p)
	}
}