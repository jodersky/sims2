/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics.constraints

import sims.dynamics._
import sims.math._

/** A base trait for implementing and solving constraints.
  * Constraints are solved using the 'sequential impulse' method presented by Erin Catto
  * at Game Developpers Conference 2008 (see http://www.box2d.org/documentation.html).
  * 
  * One instance of this trait represents one constraint for one degree of freedom between
  * two bodies.
  * @see sims.dynamics.constraints.Constrained */
trait Constraint {
	
	/** First constrained body. */
	def body1: Body
	
	/** Second constrained body. */
	def body2: Body
	
	/** Value of the position constraint function ('C' in the presentation). */
	def value: Double
	
	/** Jacobian for the velocity constraint ('J' in 'Ċ=Jv+b' in the presentation). */
	def jacobian: Jacobian
	
	/** Velocity bias ('b' in 'Ċ=Jv+b' in the presentation).*/
	def bias: Double = 0
	
	/** Lower and upper limits of the total corrective impulse allowed
	  * when solving this constraint.
	  * The first element represents the lower limit,
	  * the second the upper limit.
	  * None represents no limits. */
	def limit: Option[(Double, Double)] = None
	
	/** Defines whether or not this constraint should be treated as an inequality constraint.
	  * An inequality constraint will only be solved if the constraint function is less than
	  * zero (i.e. `value < 0`). */
	def inequality = false
	
	/** Position error used for Baumgarte sabilization. 
	  * Corresponds to `value` by default. */
	def error = value
	
	/** Accumulated impulse. */
	private var accumulated = 0.0
	
	/** Constraint mass cache, constant for all iterations. */
	private var m = 0.0
	
	/** Jacobian cache, constant for all iterations. */
	private var J: Jacobian = null
	
	/** Velocity bias cache, constant for all iterations. */
	private var b = 0.0
	
	/** Method that should be called before solving this constraint.
	  * Computes constraint masses and other invariable data for iterating over constraints. */
	def preSolve() = {
		//compute jacobian and bias for next solving
		J = jacobian
		b = bias
		
		//invMass=(J*invert(M)*transpose(J))
		val invMass: Double = (
				(J.v1.x * J.v1.x + J.v1.y * J.v1.y) / body1.mass + J.w1 * J.w1 / body1.inertia +
				(J.v2.x * J.v2.x + J.v2.y * J.v2.y) / body2.mass + J.w2 * J.w2 / body2.inertia
			)
		
		//if invMass == 0, both bodies have infinite mass (i.e. are fixed)
		m = if (invMass == 0.0) 0.0 else 1.0 / invMass
		
		//apply accumulated impulse from prevoius step
		body1.applyImpulse(J.v1 * accumulated)
		body1.applyAngularImpulse(J.w1 * accumulated)
		body2.applyImpulse(J.v2 * accumulated)
		body2.applyAngularImpulse(J.w2 * accumulated)
		
		//initialize accumulated impulse
		//accumulated = 0.0
	}
	
	/** Solves this constraint by applying corrective impulses to its constrained bodies.
	  * @todo implement error correction properly for collisiions (add slop tolerance, etc)
	  * @param h time interval over which the correction should be applied
	  * @param erp error reduction parameter. */
	def correctVelocity(h: Double, erp: Double): Unit = {
		
		//if m == 0, both bodies are fixed and there is no point in applying corrective impulse
		if (m == 0) return ()
		
		//C > 0 => ignore
		if (inequality && value > 0.0) return ()
		
		//lambda
		var lambda = -m * (
				J.v1.x * body1.linearVelocity.x + J.v1.y * body1.linearVelocity.y + J.w1 * body1.angularVelocity +
				J.v2.x * body2.linearVelocity.x + J.v2.y * body2.linearVelocity.y + J.w2 * body2.angularVelocity +
				b + (erp / h * error)
			)
			
		
		//clamp accumulated impulse
		if (limit != None) {
			val temp = accumulated
			accumulated = math.max(limit.get._1, math.min(accumulated + lambda, limit.get._2))
			lambda = accumulated - temp
		}
		
		
		//apply accumulated impulse
		body1.applyImpulse(J.v1 * lambda)
		body1.applyAngularImpulse(J.w1 * lambda)
		body2.applyImpulse(J.v2 * lambda)
		body2.applyAngularImpulse(J.w2 * lambda)	
	}
	
}