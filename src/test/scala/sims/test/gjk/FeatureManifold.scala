package sims.test.gjk

import sims.collision._
import sims.math._

object FeatureManifold {
	
	def farthestFeature(collidable: Collidable, direction: Vector2D): Either[Vector2D, Segment] = collidable match {
		
		case c: Circle => Left(c.position + direction.unit * c.radius)
		
		case p: ConvexPolygon => {
			var max = p.vertices(0) dot direction.unit
			
			//maximum vertice index
			var i = 0
  	
			for (j <- 0 until p.vertices.length) {
				val d = p.vertices(j) dot direction.unit
				if (d > max) {
					max = d
					i = j
				}
			}
			
			/* 1) vertex is considered to be the first point of a segment
			 * 2) polygons vertices are ordered counter-clockwise
			 * 
			 * implies:
			 * previous segment is the (i-1)th segment
			 * next segment is the ith segment */
			val prev = if (i == 0) p.sides.last else p.sides(i - 1)
			val next = p.sides(i)
			
			// check which segment is less parallel to direction
			val side =
				if ((prev.direction0 dot direction).abs <= (next.direction0 dot direction).abs) prev
				else next
			
				Right(side)
		}
		
		case _ => throw new IllegalArgumentException("Collidable is of unknown type.")
		
	}
	
	def getCollisionPoints(pair: (Collidable, Collidable), normal: Vector2D): Option[Manifold] = {
		var points = new scala.collection.mutable.ArrayBuffer[Vector2D]
		
		val feature1 = farthestFeature(pair._1, normal)
		
		//is feature 1 a vertex?
		if (feature1.isLeft) {
			return Some(Manifold(Array(feature1.left.get), normal))
		}
		
		val feature2 = farthestFeature(pair._2, -normal)
		
		//is feature 2 a vertex?
		if (feature2.isLeft) {
			return Some(Manifold(Array(feature2.left.get), -normal))
		}
		
		//neither feature is a vertex
		val side1 = feature1.right.get
		val side2 = feature2.right.get
		
		
		val flipped = (side1.direction0 dot normal).abs > (side2.direction0 dot normal).abs
		val reference = if (!flipped) side1 else side2
		val incident = if (!flipped) side2 else side1
		
		
		//both features are sides, clip feature2 to feature1
		val clipped = incident clipped reference
		
		/*val n = if (!flipped) normal else -normal
		clipped match {
			case None => None
			case Some(Segment(point1, point2)) => Some( 
				Manifold(Array(point1, point2) filter ((v: Vector2D) => ((v - reference.point1) dot n) <= 0), n)
				)
		}*/
		error("")
	}

}

case class Manifold(pts: Array[Vector2D], normal: Vector2D)