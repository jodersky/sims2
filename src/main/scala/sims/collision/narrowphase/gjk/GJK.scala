package sims.collision.narrowphase
package gjk

import sims.collision._
import sims.math._
import scala.collection.mutable.ListBuffer

class GJK[A <: Collidable: ClassManifest] extends narrowphase.NarrowPhaseDetector[A] {
	
	
	def penetration(pair: (A, A)): Option[Penetration] = {
		val ms = new MinkowskiSum(pair)		
		val s = ms.support(Vector2D.i)
		val simplex = new ListBuffer[Vector2D]
		simplex prepend s
		var direction = -s
		
		while (true) {
			
			val a = ms.support(direction)
			
			if ((a dot direction) < 0) return None
			
			simplex prepend a
			
			val newDirection = checkSimplex(simplex, direction)
			
			if (newDirection == null) return Some(EPA.penetration(simplex, ms))
			else direction = newDirection
			
		}
		
		throw new IllegalArgumentException("Something went wrong, should not reach here.")
	}
	
	/** Checks whether the given simplex contains the origin. If it does, `null` is returned.
	  * Otherwise a new search direction is returned and the simplex is updated. */
	private def checkSimplex(simplex: ListBuffer[Vector2D], direction: Vector2D): Vector2D = {
		if (simplex.length == 2) { //simplex == 2
			val a = simplex(0)
			val b = simplex(1)
			val ab = b - a
			val ao = -a
			
			if (ao directionOf ab) {
				ab cross ao cross ab
			} else {
				simplex.remove(1)
				ao
			}
		} // end simplex == 2
		
		else if (simplex.length == 3) { //simplex == 3
			val a = simplex(0)
			val b = simplex(1)
			val c = simplex(2)
			val ab = b - a
			val ac = c - a
			val ao = -a
			val winding = ab cross ac
			
			if (ao directionOf (ab cross winding)) {
				if (ao directionOf ab) {
					simplex.remove(2)
					ab cross ao cross ab
				} else if (ao directionOf ac) {
					simplex.remove(1)
					ac cross ao cross ac
				} else {
					simplex.remove(2)
					simplex.remove(1)
					ao
				}
			} else {
				if (ao directionOf (winding cross ac)) {
					if (ao directionOf ac) {
						simplex.remove(1)
						ac cross ao cross ac
					} else {
						simplex.remove(2)
						simplex.remove(1)
						ao
					}
				} else {
					null
				}
			}
		} //end simplex == 3
		
		else throw new IllegalArgumentException("Invalid simplex size.")
		
	}
	
	def collision(pair: (A, A)): Option[Collision[A]] = {
		val p = penetration(pair)
		if (p.isEmpty) return None
		val manif = CS.getCollisionPoints(pair, p.get.normal)
		Some(new Collision[A] {
			val item1 = pair._1 
			val item2 = pair._2 
			val normal = manif.normal
			val overlap = p.get.overlap
			val points = manif.points
		})
		
	}

}

object GJK {
	
}