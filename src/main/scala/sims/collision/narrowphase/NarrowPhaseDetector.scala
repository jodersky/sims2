/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision.narrowphase

import sims.collision._

abstract class NarrowPhaseDetector[A <: Collidable: ClassManifest] {
	
	def collision(pair: (A, A)): Option[Collision[A]]
	
}