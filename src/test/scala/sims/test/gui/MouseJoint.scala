package sims.test.gui

import sims.dynamics._
import sims.dynamics.constraints._
import sims.math._

class MouseJoint(val body1: Body, var anchor: Vector2D) extends Joint {
	val body2 = new Body() {fixed = true}
	
	private val self = this
	
	private val local1 = anchor - body1.position
	
	private val rotation01 = body1.rotation
	
	def r1 = (local1 rotate (body1.rotation - rotation01))
	
	def x1 = body1.position + r1
	def x2 = anchor
	
	def x = x2 - x1
	
	val constraints = List(
		new Constraint {
			val body1 = self.body1
			val body2 = self.body2
			def value = x.x
			def jacobian = new Jacobian(-Vector2D(1, 0), r1.y, Vector2D.i, 0)
		},
		new Constraint {
			val body1 = self.body1
			val body2 = self.body2
			def value = x.y
			def jacobian = new Jacobian(-Vector2D(0, 1), -r1.x, Vector2D.j, 0)
		}
	)
	
}