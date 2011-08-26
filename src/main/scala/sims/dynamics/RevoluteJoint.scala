/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import sims.dynamics.constraints._
import sims.math._

/** A revolute joint, used to keep two bodies fixed relatively at one point.
 	* 
 	* This joint removes two degrees of freedom between two bodies,
 	* hence it contains two [[sims.dynamics.constraints.Constraint]]s. 
 	* 
 	* @param body1 first body connected by this joint
 	* @param body2 second body connected by this joint 
 	* @param anchor anchor point where bodies are fixed relatively */
class RevoluteJoint(val body1: Body, val body2: Body, val anchor: Vector2D) extends Joint {
	def this(body1: Body, body2: Body) = this(body1, body2, body1.position)
	
	private val self = this
	
	private val local1 = anchor - body1.position
	private val local2 = anchor - body2.position
	
	private val rotation01 = body1.rotation
	private val rotation02 = body2.rotation
	
	def r1 = (local1 rotate (body1.rotation - rotation01))
	def r2 = (local2 rotate (body2.rotation - rotation02))
	
	def x1 = body1.position + r1
	def x2 = body2.position + r2
	
	def x = x2 - x1
	
	val constraints = List(
		new Constraint {
			val body1 = self.body1
			val body2 = self.body2
			def value = x.x
			def jacobian = new Jacobian(-Vector2D(1, 0), r1.y, Vector2D(1, 0), -r2.y)
		},
		new Constraint {
			val body1 = self.body1
			val body2 = self.body2
			def value = x.y
			def jacobian = new Jacobian(-Vector2D(0, 1), -r1.x, Vector2D(0, 1), r2.x)
		}
	)
	
}