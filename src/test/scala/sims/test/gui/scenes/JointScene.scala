package sims.test.gui
package scenes

import sims.dynamics._
import sims.math._
import sims.dynamics._

object JointScene extends Scene {
	
	override def init() = {
		val b1 = new Body(new Circle(0.1)) {fixed = true}
		val b2 = new Body(new Rectangle(0.1, 0.5)) {position = Vector2D(2,2)}
		val j = new DistanceJoint(b1, b1.position, b2, b2.position + Vector2D(0, -0.5))
		world += b1
		world += b2
		world += j
		
		val chainBodies = for (i <- 0 until 10) yield new Body(new Rectangle(0.5, 0.1)){fixed = i == 0 || i == 9; position = Vector2D(i, 5)}
		val chainHinges = for (i <- 0 until chainBodies.length - 1) yield
			new RevoluteJoint(chainBodies(i), chainBodies(i + 1), Vector2D(i + 0.5, 5))
		for (b <- chainBodies) world += b
		for (j <- chainHinges) world += j
		
		import sims.dsl._
		val c = new Body(new Circle(0.1)) {position = Vector2D(4, 0)}
		world += c
		world += c distance chainBodies(4)
		
		val r = new Body(new Rectangle(2, 0.1)) {position = Vector2D(4, 0)}
		world += r
		world += c revolute r
		
		val c2 = new Body(new Circle(0.2)) {position = Vector2D(2, 2)}
		world += c2
		world += r :@@ (-2, 0) distance c2
		
		val r2 = new Body(new Rectangle(0.1, 0.2)) {position = Vector2D(6, 2)}
		world += r2
		world += r :@@ (2, 0) distance (0, -0.2) @@: r2
		
		val r3 = new Body(new Rectangle(0.3, 0.1)) {position = Vector2D(6.3, 2.2)}
		world += r3
		world += r2 :@ (6, 2.2) revolute r3
		
		// chaos pendulum
		{
			val c1 = new Body(new Circle(0.1)) {fixed = true; position = Vector2D(12, 2)}
			world += c1
			val r1 = new Body(new Rectangle(1, 0.1)) {position = Vector2D(13, 2)}
			world += r1
			val r2 = new Body(new Rectangle(1, 0.1)) {position = Vector2D(15, 2)}
			world += r2
			
			world += c1 revolute r1
			world += r1 :@@ (1, 0) revolute r2
			
		}
		
		// net
		{
			val w = 10
			val h = 10
			val d = 0.2
			
			val nodes = 
				for (i <- (0 until w).toArray) yield
					for (j <- (0 until h).toArray) yield
						new Body(new Circle(0.05)) {fixed = i == 0 && j == h - 1 ; position = Vector2D(i * d, j * d) + Vector2D(-3, 2)}
			
			for (n <- nodes.flatten) world += n
			
			val joints = {
				var r: List[DistanceJoint] = Nil
				for(i <- 0 to nodes.length - 1; j <- 0 to nodes(i).length - 1) {
					if (i > 0)
						r = (nodes(i-1)(j) distance nodes(i)(j)) :: r
					if (j > 0)
						r = (nodes(i)(j-1) distance nodes(i)(j)) :: r
				}
				r
			}
			
			for (j <- joints) world += j
			
		}
		
		
		world.collisionDetection = false
		world.iterations = 10
		world.errorReduction = 1
		
		
		/*
		val r1 = new Body(new Rectangle(0.5, 0.1)) {fixed = true; position = Vector2D(5, 5)}
		val r2 = new Body(new Rectangle(0.5, 0.1)) {position = Vector2D(6, 5)}
		val r3 = new Body(new Rectangle(0.5, 0.1)) {position = Vector2D(7, 5)}
		val j12 = new RevoluteJoint(r1, r2, Vector2D(5.5, 5))
		val j23 = new RevoluteJoint(r2, r3, Vector2D(6.5, 5))
		world += r1
		world += r2
		world += r3
		world += j12
		world += j23
		*/
	}

}