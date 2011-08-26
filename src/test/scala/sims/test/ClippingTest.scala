package sims.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import sims.math._
import sims.collision._

class ClippingTest extends FunSuite with ShouldMatchers {
	
	/*
	test("clipping segment to segment, outside clipping zone") {
		val A = new Segment((1, 1), (3, 0))
		val B = new Segment((0,0), (0, 1))
		val C = new Segment((4,-1), (10, 5))
		
		(B clipped A) should equal (None)
		(C clipped A) should equal (None)
	}
	
	test("clipping segment to segment, inside clipping zone") {
		val segments = List(
				new Segment((2, 3), (4.5, 8)),
				new Segment((3, -4), (2, -5)),
				new Segment((0, 0), (10, 0)),
				new Segment((1.1, -1234), (9.999, 0.001)),
				new Segment((5, 5), (5, -5))
			)
			
		val clippers = List(
				new Segment((0, 0), (10, 0)),
				new Segment((10, 0), (0, 0)),
				new Segment((120, -0.01), (-130, -0.01)),
				new Segment((-2.3, 10000), (14, 10000))
			)
			
		def translate(s: Segment, v: Vector2D) = {
			new Segment(s.point1 + v, s.point2 + v)
		}
		
		def rotate(s: Segment, r: Double) = {
			new Segment(s.point1 rotate r, s.point2 rotate r)
		}
		
		for (s <- segments; c <- clippers) {(s clipped c).get should equal (s)}
		
		for (i <- 0 until 1000) for(s <- segments; c <- clippers) {
			val r = new scala.util.Random
			def next = r.nextDouble * 1000 * (if (r.nextBoolean) -1 else 1)
			val x = (next, next)
			val y = next
			(translate(rotate(s, y), x) clipped translate(rotate(c, y), x)).get should equal (translate(rotate(s, y), x))
		}
		
	}
	*/

}