/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import sims.dynamics.constraints._
import sims.math._

/** A distance joint, used to keep two bodies at a fixed distance.
 	* The distance is given by the initial distance of the anchor points.
 	* 
 	* This joint removes one degree of freedom between two bodies,
 	* hence it contains one [[sims.dynamics.constraints.Constraint]]. 
 	* 
 	* @param body1 first body connected by this joint
 	* @param anchor1 anchor point on first body (in world coordinates)
 	* @param body2 second body connected by this joint 
 	* @param anchor2 anchor point on second body (in world coordinates) */
class DistanceJoint(val body1: Body, val anchor1: Vector2D, val body2: Body, val anchor2: Vector2D) extends Joint {
	def this(body1: Body, body2: Body) = this(body1, Vector2D.Null, body2, Vector2D.Null)
	
	private val self = this
	
	val local1 = anchor1 - body1.position
	val local2 = anchor2 - body2.position
	private val l = (anchor2 - anchor1).length //(body2.position + local2 - body1.position - local1).length
	private val rotation01 = body1.rotation
	private val rotation02 = body2.rotation
	
	def r1 = (local1 rotate (body1.rotation - rotation01))
	def r2 = (local2 rotate (body2.rotation - rotation02))
	
	def x1 = body1.position + r1
	def x2 = body2.position + r2
	
	private def v1 = body1 velocityOfPoint x1
	private def v2 = body2 velocityOfPoint x2
	
	private def x = x2 - x1
	private def v = v2 - v1
	
	val constraints = List(new Constraint{
		val body1 = self.body1
		val body2 = self.body2
		def value = x.length - l
		def jacobian = new Jacobian(-x.unit, -(r1 cross x.unit), x.unit, (r2 cross x.unit))
	})
	
	
}