/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims

import sims.collision.broadphase._
import sims.collision.narrowphase._

package object collision {
	
	implicit def broadphaseToConstructor[A <: Collidable: ClassManifest](b: BroadPhaseDetector[A]) =
		new DetectorConstructor(b) 

}