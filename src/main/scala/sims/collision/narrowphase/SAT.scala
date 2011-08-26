/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision.narrowphase

import sims.collision._
import sims.math._

class SAT[A <: Collidable: ClassManifest] extends NarrowPhaseDetector[A] {
	
	def collision(pair: (A, A)): Option[Collision[A]] = {
		var c = getCollision(pair)
		c orElse getCollision(pair.swap)
	}
	
	private def getCollision(pair: (A,A)): Option[Collision[A]] = pair match {
		
		case (c1: Circle, c2: Circle) =>
			collisionCircleCircle(c1, c2)(pair)
			
		case (p1: ConvexPolygon, p2: ConvexPolygon) =>
			sat(pair, p1.sides.map(_.rightNormal0) ++ p2.sides.map(_.rightNormal0))
			
		case _ => None
		
	}
	
	private def collisionCircleCircle(c1: Circle, c2: Circle)(pair: (A, A)) = {
		val d = (c2.position - c1.position)
		val l = d.length
		if (l <= c1.radius + c2.radius) {
			val p = c1.position + d.unit * (l - c2.radius)
			Some(new Collision[A] {
				val item1 = pair._1
				val item2 = pair._2
				val normal = d.unit
				val points = List(p)
				val overlap = (c1.radius + c2.radius - l)
			})
		} else None
	}
	
	private def collisionPolyPoly(p1: ConvexPolygon, p2: ConvexPolygon)(pair: (A, A)): Option[Collision[A]] = {
		var minOverlap = Double.PositiveInfinity
		var reference: ConvexPolygon = null
		var incident: ConvexPolygon = null
		var referenceSide: Segment = null
		var incidentVerticeNumber = 0
		
		for (i <- 0 until p1.sides.length) {
			var overlaps = false
			
			for (j <- 0 until p2.vertices.length) {
				val s = p1.sides(i)
				val v = p2.vertices(j)
				
				val overlap = (s.rightNormal0 dot s.point1) - (s.rightNormal0 dot v)
				if (overlap > 0) overlaps = true
				if (overlap > 0 && overlap < minOverlap.abs) {
					minOverlap = overlap
					
					reference = p1
					referenceSide = s
					incident = p2
					incidentVerticeNumber = j
				}
			}
			if (!overlaps) return None
		}
		
		for (i <- 0 until p2.sides.length) {	
			var overlaps = false
			
			for (j <- 0 until p1.vertices.length) {
				val s = p2.sides(i)
				val v = p1.vertices(j)
				
				val overlap = (s.rightNormal0 dot s.point1) - (s.rightNormal0 dot v)
				if (overlap > 0) overlaps = true
				if (overlap > 0 && overlap < minOverlap.abs) {
					minOverlap = overlap
					
					reference = p2
					referenceSide = s
					incident = p1
					incidentVerticeNumber = j
				}
			}
			if (!overlaps) return None
		}
		
		val i = incidentVerticeNumber
		val side1 = incident.sides(i)
		val side2 = incident.sides(mod(i-1, incident.sides.length))
		
		val incidentSide = if ((side1.direction dot referenceSide.rightNormal0).abs <
													(side2.direction dot referenceSide.rightNormal0).abs) side1
											 else side2
											 
		val clipped: Segment = null //incidentSide clippedToSegment referenceSide
		
		Some(new Collision[A] {
			val item1 = reference.asInstanceOf[A]
			val item2 = incident.asInstanceOf[A]
			val normal = referenceSide.rightNormal0
			val points = List(clipped.point1, clipped.point2)
			val overlap = minOverlap
			
		})
		
	}
	
	
	
	
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
				if ((prev.direction.unit dot direction).abs <= (next.direction.unit dot direction).abs) prev
				else next
			
				Right(side)
		}
		
		case _ => throw new IllegalArgumentException("Collidable is of unknown type.")
		
	}
	

	
	//normal is considered pointing from _1 to _2
	//_1 reference, _2 incident
	def getCollisionPoints(pair: (A, A), normal: Vector2D, overlap: Double): Array[Vector2D] = {
		var points = new scala.collection.mutable.ArrayBuffer[Vector2D]
		
		val feature1 = farthestFeature(pair._1, normal)
		
		//is feature 1 a vertex?
		if (feature1.isLeft) {
			return Array(feature1.left.get)
		}
		
		val feature2 = farthestFeature(pair._2, -normal)
		
		//is feature 2 a vertex?
		if (feature2.isLeft) {
			return Array(feature2.left.get)
		}
		
		//neither feature is a vertex
		val side1 = feature1.right.get
		val side2 = feature2.right.get
		
		
		val flipped = (side1.direction.unit dot normal).abs > (side2.direction.unit dot -normal).abs
		val reference = if (!flipped) side1 else side2
		val incident = if (!flipped) side2 else side1
		
		
		//both features are sides, clip feature2 to feature1
		val clipped: Option[Segment] = None //incident clipped reference
		
		clipped match {
			case None => Array()
			case Some(Segment(point1, point2)) => Array(point1, point2) filter ((v: Vector2D) => ((v - reference.point1) dot reference.rightNormal0) <= 0)
		}
	}
	
	def sat(pair: (A, A), axes: Seq[Vector2D]): Option[Collision[A]] = {
		var min = Double.PositiveInfinity
		var n = axes(0)
		
		for (axis <- axes) {
			val overlap = pair._1.project(axis) overlap pair._2.project(axis)
			if (overlap < 0) return None
			if (overlap < min) {
				min = overlap
				n = axis
			}
		}
			
		val pts = getCollisionPoints(pair, n, min)

		if (pts.length == 0) return None
		
		Some(new Collision[A] {
			val item1 = pair._1
			val item2 = pair._2
			val normal = n
			val points = pts.toSeq
			val overlap = min
		})
		
		
			
	}
	
	
	
}

object SAT {
	
	def apply[A <: Collidable: ClassManifest] = new SAT[A]
	
}