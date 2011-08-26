/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import sims.math._

class Body(shapes0: Shape*) {
	
	val shapes: List[Shape] = shapes0.toList
	
	var force: Vector2D = Vector2D.Null
	
	var torque: Double = 0.0
	
	var linearVelocity: Vector2D = Vector2D.Null
	
	var angularVelocity: Double = 0.0 
	
	private var _position: Vector2D = 
		(Vector2D.Null /: shapes)((v: Vector2D, s: Shape) => v + s.position * s.mass) / shapes.map(_.mass).sum
	
	def position = _position
	
	def position_=(pos: Vector2D) = {
		val delta = pos - _position
		_position = pos
		for (s <- shapes) s.position += delta
	}
	
	private var _rotation: Double = 0.0
	
	def rotation = _rotation
	
	def rotation_=(r: Double) = {
		val delta = _rotation - r
		_rotation = r
		for (s <- shapes) {
			s.rotation += delta
			s.position = position + (s.local.get rotate r)
		} 
	}
	
	var fixed = false
	
	/**Returns the mass of this body. If the body is free, its mass is the sum of the masses of its shapes.
 	* If the body is fixed, its mass is infinite (`Double.PositiveInfinity`).
 	* @return this body's mass*/
 	lazy val mass: Double = if (!fixed) shapes.map(_.mass).sum else Double.PositiveInfinity
 	
 	/**Returns the moment of inertia for rotations about the COM of this body.
 	* It is calculated using the moments of inertia of this body's shapes and the parallel axis theorem.
 	* If the body is fixed, its moment of inertia is infinite (`Double.PositiveInfinity`).
 	* @return moment of inertia for rotations about the center of mass of this body*/
 	lazy val inertia: Double = if (!fixed) shapes.map((s: Shape) => s.inertia + s.mass * (s.local.get dot s.local.get)).sum else Double.PositiveInfinity
 	
 	/**Applies a force to the center of mass of this body.
   * @param force applied force*/
  def applyForce(force: Vector2D) = if (!fixed) this.force += force
  
  /**Applies a force to a point on this body. The point is considered to be contained within this body.
   * @param force applied force
   * @param point position vector of the point (in world coordinates)*/
  def applyForce(force: Vector2D, point: Vector2D) = if (!fixed) {this.force += force; torque += (point - position) cross force}
	
	/**Applies a torque to the center of mass.*/
	def applyTorque(torque: Double) = if (!fixed) this.torque += torque
	
	/**Applies an impulse to the center of mass of this body.
   * @param impulse applied impulse*/  
  def applyImpulse(impulse: Vector2D) = if (!fixed) linearVelocity += impulse / mass
  
  /**Applies an impulse to a point on this body. The point is considered to be contained within this body.
   * @param impulse applied impulse
   * @param point position vector of the point (in world coordinates)*/
  def applyImpulse(impulse: Vector2D, point: Vector2D) = if (!fixed) {linearVelocity += impulse / mass; angularVelocity += ((point - position) cross impulse) / inertia}
  
  /**Applies an angular impulse to the center of mass.*/
  def applyAngularImpulse(impulse: Double) = if (!fixed) angularVelocity += impulse / inertia
  
  /**Linear velocity of the given point on this body (in world coordinates).*/
  def velocityOfPoint(point: Vector2D) = linearVelocity + (angularVelocity cross (point - position))
  
  /**Linear momentum.*/
  def linearMomentum = linearVelocity * mass  
  
 	for (s0 <- shapes0) {
 		s0.local = Some(s0.position - _position)
 		s0.body = this
 	}
  
  def contains(point: Vector2D) = shapes.exists(_.contains(point))
  
  def info = {
  	"Body@" + hashCode + "(" + this.getClass() + ")\n" +  
  			"\tPosition: " + position + "\n" + 
  			"\tRotation: " + rotation + "\n" + 
  			"\tLinear velocity: " + linearVelocity + "\n" + 
  			"\tAngular velocity: " + angularVelocity + "\n" +
  			"\tForce: " + force + "\n" +
  			"\tTorque: " + torque + "\n" +
  			"\tMass: " + mass + "\n" +
  			"\tInertia: " + inertia + "\n" +
  			"\tFixed: " + fixed + "\n" +
  			"\tShape count" + shapes.length
  			
  }
 	
}