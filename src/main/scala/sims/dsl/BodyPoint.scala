/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dsl

import sims.dynamics._
import sims.math._

class BodyPoint(val body: Body, val point: Vector2D) {
	def this(body: Body) = this(body, body.position)
	
	def distance(bp: BodyPoint) = new DistanceJoint(body, point, bp.body, bp.point)
	
	def revolute(bp: BodyPoint) = new RevoluteJoint(body, bp.body, point)
	
}