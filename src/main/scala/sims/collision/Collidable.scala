/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math._

/** A base trait for all collidable objects. */
trait Collidable extends AnyRef {
	
	/** Returns an axis aligned box, bounding this collidable object. */
	def aabb: AABB
	
	/** Projects this collidable object onto the given axis. */
	def project(axis: Vector2D): Projection
	
	/** Checks if the point `point` is contained in this collidable object. */
	def contains(point: Vector2D): Boolean
	
	/** Returns the farthest vertex of this collidable in the given direction. */
	def support(direction: Vector2D): Vector2D
		
	/** A fixed collidable object cannot collide with other fixed collidable objects.
	  * This is useful in improving collision detection performance, since a pair of fixed objects will
	  * be eliminated in the broadphase. */
	def fixed = false
	
}