package sims.test

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import sims.math._
import sims.collision._

class LinearOverlapTest extends FlatSpec with ShouldMatchers {
	
	"A segment" should "throw IllegalArgumentException if both of its " +
	"vertices degenerate into a single one" in {
		evaluating { val s1 = Segment(Vector2D(0,0), Vector2D(0,0)) } should produce [IllegalArgumentException]
	}
	
	it should "not intersect with itself" in {
		val s1 = Segment(Vector2D(2, 2), Vector2D(3, 5))
		s1 intersection s1 should equal (None)
	}
	
	"Two segments" should "have an intersection point if they intersect" in {
		val s1 = Segment(Vector2D(0, 0), Vector2D(3, 1))
		val s2 = Segment(Vector2D(0, 1), Vector2D(3, -2))
		s1 intersection s2 should not equal (None)
	}
	
	it should "have an intersection point if they share a vertice" in {
		val s1 = Segment(Vector2D(1, 2), Vector2D(3, 1))
		val s2 = Segment(Vector2D(3, 1), Vector2D(3, -2))
		s1 intersection s2 should not equal (None)
	}
	
	it should "have an intersection point if one contains one of the other's vertices" in {
		val s1 = Segment(Vector2D(2, 4), Vector2D(3, 100))
		val s2 = Segment(Vector2D(1, 3), Vector2D(3, 5))
		s1 intersection s2 should not equal (None)
	}
	
	it should "not have an intersection point if they are parallel" in {
		val s1 = Segment(Vector2D(0, 0), Vector2D(3, 1))
		val s2 = Segment(Vector2D(0, 1), Vector2D(3, 2))
		s1 intersection s2 should equal (None)
	}
		
	it should "not have an intersection point if they are parallel and lie on each other" in {
		val s1 = Segment(Vector2D(2, 2), Vector2D(6, 6))
		val s2 = Segment(Vector2D(3, 3), Vector2D(4, 4))
		s1 intersection s2 should equal (None)
	}
	
	
	"A ray and a segment" should "have an intersection point if they intersect" in {
		val r1 = Ray(Vector2D(3, 5), Vector2D(3, -1))
		val s1 = Segment(Vector2D(6.32, math.sqrt(4.0)), Vector2D(10, 15.5))
		r1 intersection s1 should not equal (None)
	}
	
	it should "have an intersection point if they share a vertice" in {
		val r1 = Ray(Vector2D(3, 4), Vector2D(2, 1))
		val s1 = Segment(Vector2D(0, 10), Vector2D(3, 4))
		r1 intersection s1 should not equal (None)
	}
	
	it should "have an intersection point if the ray contains one of the segment's vertices" in {
		val r1 = Ray(Vector2D(0, 0), Vector2D(1, 2))
		val s1 = Segment(Vector2D(2, 4), Vector2D(5, 4))
		r1 intersection s1 should not equal (None)
	}
	
	it should "have an intersection point if the segment contains the ray's vertice" in {
		val r1 = Ray(Vector2D(0, math.Pi), Vector2D(1, 2))
		val s1 = Segment(Vector2D(0, 0), Vector2D(0, 4))
		r1 intersection s1 should not equal (None)
		
		val r2 = Ray(Vector2D(2, 3), Vector2D(-2, -1))
		val s2 = Segment(Vector2D(0, 4), Vector2D(4, 2))
		r2 intersection s2 should not equal (None)
	}
	
	it should "not have an intersection point if they are parallel" in {
		val r1 = Ray(Vector2D(2, 3), Vector2D(3, 4))
		val s1 = Segment(Vector2D(1, 4), Vector2D(4, 8))
		r1 intersection s1 should equal (None)
	}
	
	it should "not have an intersection point if they lie on each other" in {
		val r1 = Ray(Vector2D(0, 1), Vector2D(2, 3))
		val s1 = Segment(Vector2D(-1, 0), Vector2D(4, 4))
		r1 intersection s1 should equal (None)
	}
	
}