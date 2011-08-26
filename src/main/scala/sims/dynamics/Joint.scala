/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import sims.dynamics.constraints.Constraining

/** A base trait for all joints between two bodies. */
trait Joint extends Constraining {
	
	/** First body connected by this joint. */
	def body1: Body
	
	/** Second body connected by this joint. */
	def body2: Body
	
}