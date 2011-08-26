package sims.test.gjk

import scala.collection.mutable.ListBuffer
import sims.math._

/** The implementation was adapted from dyn4j by William Bittle (see http://www.dyn4j.org). */
object EPA {
	
	val MaxIterations = 100
	
	val DistanceEpsilon = 0.0001
	
	protected class Edge(val normal: Vector2D, val distance: Double, val index: Int)
	
	private def winding(simplex: ListBuffer[Vector2D]) = {
		for (i <- 0 until simplex.size) {
			val j = if (i + 1 == simplex.size) 0 else i + 1
			//val winding = math.signum(simplex(j) - simplex(i))
		}
		
	}
		
	
	def getPenetration(simplex: ListBuffer[Vector2D], minkowskiSum: MinkowskiSum): Penetration = {
		// this method is called from the GJK detect method and therefore we can assume
		// that the simplex has 3 points
		
		// get the winding of the simplex points
		// the winding may be different depending on the points added by GJK
		// however EPA will preserve the winding so we only need to compute this once
		val winding = math.signum((simplex(1) - simplex(0)) cross (simplex(2) - simplex(1))).toInt
		// store the last point added to the simplex
		var point = Vector2D.Null
		// the current closest edge
		var edge: Edge = null;
		// start the loop
		for (i <- 0 until MaxIterations) {
			// get the closest edge to the origin
			edge = this.findClosestEdge(simplex, winding);
			// get a new support point in the direction of the edge normal
			point = minkowskiSum.support(edge.normal);
			
			// see if the new point is significantly past the edge
			val projection: Double = point dot edge.normal
			if ((projection - edge.distance) < DistanceEpsilon) {
				// then the new point we just made is not far enough
				// in the direction of n so we can stop now and
				// return n as the direction and the projection
				// as the depth since this is the closest found
				// edge and it cannot increase any more
				return new Penetration(edge.normal, projection)
			}
			
			// lastly add the point to the simplex
			// this breaks the edge we just found to be closest into two edges
			// from a -> b to a -> newPoint -> b
			simplex.insert(edge.index, point);
		}
		// if we made it here then we know that we hit the maximum number of iterations
		// this is really a catch all termination case
		// set the normal and depth equal to the last edge we created
		return new Penetration(edge.normal, point dot edge.normal)
	}
	
	private def findClosestEdge(simplex: ListBuffer[Vector2D], winding: Int): Edge = {
		// get the current size of the simplex
		val size = simplex.size;
		// create an edge
		var edge = new Edge(Vector2D.Null, Double.PositiveInfinity, 0)
		// find the edge on the simplex closest to the origin
		for (i <- 0 until size) {
			// compute j
			val j = if (i + 1 == size) 0 else i + 1
			// get the points that make up the current edge
			val a = simplex(i);
			val b = simplex(j);
			// create the edge
			val direction = simplex(j) - simplex(i);
			// depending on the winding get the edge normal
			// it would findClosestEdge(List<Vector2> simplex, int winding) {
			// get the current size of the simplex
			val normal = if (winding > 0) direction.rightNormal.unit
									 else direction.leftNormal.unit
		
			// project the first point onto the normal (it doesnt matter which
			// you project since the normal is perpendicular to the edge)
			val d: Double = math.abs(simplex(i) dot normal);
			// record the closest edge
			if (d < edge.distance) 
				edge = new Edge(normal, d, j)
		}
		// return the closest edge
		return edge;
	}

}

class Penetration(val normal: Vector2D, val overlap: Double)