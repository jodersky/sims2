/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.collision.broadphase._
import sims.collision.narrowphase._
import scala.collection.mutable.ArrayBuffer

/** Collision detectors are used to compute collisions between
  * a given collection of items.
  * They use a [[sims.collision.BroadPhaseDetector]] to determine potentially
  * colliding pairs of items.
  * These pairs are then examined with a [[sims.collision.NarrowPhaseDetector]]
  * to compute the final collisions.
  * 
  * @param broadphase a broadphase collision detector
  * @param narrowphase a narrowphase collision detector */
class Detector[A <: Collidable: ClassManifest](
		broadphase: BroadPhaseDetector[A],
		narrowphase: NarrowPhaseDetector[A]
		) {

	/** Collidable items managed by this collision detector. */
	def items: Seq[A] = broadphase.items
	
	/** Adds an item to this collision detector. */
	def +=(item: A): Unit = broadphase +=  item
	
	/** Adds a collection of items to this collision detector. */
	def ++=(items: Iterable[A]) = for (i <- items) this += i
	
	/** Removes an item from this collision detector. */
	def -=(item: A): Unit = broadphase -= item
	
	/** Removes a collection of items from this collision detector. */
	def --=(items: Iterable[A]) = for (i <- items) this -= i
	
	/** Removes all items from this collision detector. */
	def clear(): Unit = broadphase.clear
	
	/** Indicates the valid state of this collision detector. */
	private var valid = false
	
	/** Invalidates this detector. The next time `collisions()` is called, all collisions will be
	  * recomputed. */
	def invalidate() = valid = false
	
	/** Cache of collisions. */
	private var _collisions = new ArrayBuffer[Collision[A]]
	
	/** Returns a cached sequence of collisions between all items managed by this collision detector.
	  * If no collisions were calculated since the last time `invalidate()` was called, the collisions
	  * will be calculated. */
	def collisions(): Seq[Collision[A]] = {
		
		if (!valid) {
			
			_collisions.clear()
			
			for (pair <- broadphase) {
				val collision = narrowphase.collision(pair)
				if (collision != None) _collisions += collision.get
			}
			
			valid = true
		}
		
		_collisions
		
	}
	
}

class DetectorConstructor[A <: Collidable: ClassManifest] (broadphase: BroadPhaseDetector[A]) {
	def narrowedBy(narrowphase: NarrowPhaseDetector[A]) = new Detector(broadphase, narrowphase)
}