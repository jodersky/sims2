/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics.constraints

import sims.dynamics._

/**A base trait implemented by objects representing constraints (such as joints or collisions).
 * @see sims.dynamics.constraints.Constraint
 */
trait Constraining {
	
	/**All constraints represented by this constraining object.*/
	def constraints: Seq[Constraint]
	
	/**Invoke `preSolve()` on each constraint.
	 * @see sims.dynamics.constraints.Constraint*/
	def preSolve() = for (c <- constraints) c.preSolve() 
		
	/**Solves all constraints of this constraining object.
	 * @see sims.dynamics.constraints.Constraint*/
	def correctVelocity(h: Double, erp: Double): Unit = for (c <- constraints) c.correctVelocity(h, erp)
	
}