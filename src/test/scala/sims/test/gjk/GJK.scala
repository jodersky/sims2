package sims.test.gjk

import scala.collection.mutable.ListBuffer
import sims.collision._
import sims.collision.narrowphase.NarrowPhaseDetector
import sims.math._

/** A narrowphase detector using the Gilbert-Johnson-Keerthi algorithm. */
class GJK[A <: Collidable: ClassManifest] extends NarrowPhaseDetector[A] {
	
	/** Checks whether the given simplex contains the origin. If it does, `None` is returned.
	  * Otherwise a new search direction is returned and the simplex is updated. */
	protected def checkSimplex(simplex: ListBuffer[Vector2D], direction: Vector2D): Option[Vector2D] = {
		if (simplex.length == 2) { //simplex == 2
			val a = simplex(0)
			val b = simplex(1)
			val ab = b - a
			val ao = -a
			
			if (ao directionOf ab) {
				Some(ab cross ao cross ab)
			} else {
				simplex.remove(1)
				Some(ao)
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
					Some(ab cross ao cross ab)
				} else if (ao directionOf ac) {
					simplex.remove(1)
					Some(ac cross ao cross ac)
				} else {
					simplex.remove(2)
					simplex.remove(1)
					Some(ao)
				}
			} else {
				if (ao directionOf (winding cross ac)) {
					if (ao directionOf ac) {
						simplex.remove(1)
						Some(ac cross ao cross ac)
					} else {
						simplex.remove(2)
						simplex.remove(1)
						Some(ao)
					}
				} else {
					None
				}
			}
		} //end simplex == 3
		
		else throw new IllegalArgumentException("Invalid simplex size.")
		
	}
	
	
	def getPenetration(pair: (A, A)): Option[Penetration] = {
		implicit val pr = pair
		val ms = new MinkowskiSum(pair)
		import ms._
		val s = support(Vector2D.i)
		val simplex = new ListBuffer[Vector2D]
		simplex prepend s
		var direction = -s
		
		var counter = 0
		while (counter < 100) {
			
			val a = support(direction)
			
			if ((a dot direction) < 0) return None
			
			simplex prepend a
			
			val newDirection = checkSimplex(simplex, direction)
			
			if (newDirection.isEmpty) return Some(EPA.getPenetration(simplex, ms))
			else direction = newDirection.get
			
			counter += 1
		}
		throw new IllegalArgumentException("Something went wrong, should not reach here.")
	}
	
	override def collision(pair: (A, A)): Option[Collision[A]] = {
		val p = getPenetration(pair)
		if (p == None) return None
		val man = FeatureManifold.getCollisionPoints(pair, p.get.normal)
		if (man == None) return None
		
		Some(new Collision[A] {
			val item1 = pair._1 
			val item2 = pair._2
			val normal = man.get.normal
			val overlap = p.get.overlap 
			val points = man.get.pts.toList
		})
	}
	
}


