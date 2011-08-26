/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

/** Implements features of a collidable object to be evaluated lazily and only
  * to be be recomputed when the collidable object moves. */
trait CachedCollidable extends Collidable {
	
	/** Invalidates all features and forces their evaluation on next call.
	  * Should be called by clients when this object moves. */
	def move() = {
		_aabbValid = false
	}
	
	private var _aabb: AABB = null
	private var _aabbValid = false
	abstract override def aabb = {
		if (! _aabbValid) {
			_aabb = super.aabb
			_aabbValid = true
		}
		_aabb
	}

}