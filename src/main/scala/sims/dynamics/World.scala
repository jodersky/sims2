/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import scala.collection.mutable.ArrayBuffer
import sims.collision._
import sims.collision.broadphase.SAP
import sims.collision.narrowphase.SAT
import sims.math.Vector2D

class World {
	
	val detector = SAP[sims.dynamics.Shape] narrowedBy
                  new sims.collision.narrowphase.gjk.GJK2[sims.dynamics.Shape]
								 //SAT[sims.dynamics.Shape]
	
	var collisionDetection = true
	
	private val _bodies = new ArrayBuffer[Body]
	def bodies: Seq[Body] = _bodies
	
	def +=(b: Body): Unit = {
		_bodies += b
		detector ++= b.shapes
	}
	def -=(b: Body): Unit = {
		_bodies -= b
		detector --= b.shapes
	}
	
	def shapes: Seq[sims.dynamics.Shape] = for (b <- bodies; s <- b.shapes) yield s
	
	private val _joints = new ArrayBuffer[Joint]
	def joints: Seq[Joint] = _joints
	
	def +=(j: Joint): Unit = _joints += j
	def -=(j: Joint): Unit = _joints -= j
	
	def clear() = {
		detector.clear()
		for (b <- bodies) _bodies -= b
		for (j <- joints) _joints -= j
	}
	
	
	var h = 1.0 / 60
	
	var iterations = 10
	
	var errorReduction = 0.2
	
	var gravity = sims.math.Vector2D(0, -9.8)
	
	def preStep() = {}
	
	def step() = {
		
		preStep()
		
		for (b <- _bodies) {
			b applyForce gravity * b.mass
			b.linearVelocity += (b.force / b.mass) * h
			b.angularVelocity += (b.torque / b.inertia) * h
		}
		
		for (j <- joints) {
			j.preSolve()
		}
		for (i <- 0 until iterations)
			for (j <- joints) j.correctVelocity(h, errorReduction)
		
		if (collisionDetection) {
			import Collision._
			val physicalCollisions: Seq[Collision] = detector.collisions.map(_.toPhysical)
			for (c <- physicalCollisions) c.preSolve()
			for (i <- 0 until iterations)
				for (c <- physicalCollisions) c.correctVelocity(h, errorReduction)
			
		}
		
		
		for (b <- _bodies) {
			b.position += b.linearVelocity * h
			b.rotation += b.angularVelocity * h
			b.force = Vector2D.Null
		}
		
		postStep()
	}
	
	def postStep(): Unit = {
		detector.invalidate()
	}
	
	
}