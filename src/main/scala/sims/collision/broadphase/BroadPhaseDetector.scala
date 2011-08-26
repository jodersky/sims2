/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision.broadphase

import sims.collision._
import scala.collection.mutable.ArrayBuffer

abstract class BroadPhaseDetector[A <: Collidable: ClassManifest] {
	
	protected var _items = new ArrayBuffer[A]
	
	/** Collidable items managed by this collision detector. */
	def items: Seq[A] = _items
	
	/** Adds an item to this collision detector. */
	def +=(item: A) = _items += item
	
	/** Adds a collection of items to this collision detector. */
	def ++=(items: Iterable[A]) = for (i <- items) this += i
	
	/**Removes an item from this collision detector. */
	def -=(item: A) = _items -= item
	
	/**Removes a collection of items from this collision detector. */
	def --=(items: Iterable[A]) = for (i <- items) this -= i
	
	/**Removes all items from this collision detector. */
	def clear() = _items.clear
	
	/** Applies a given function to every potentially colliding pair.
	  * @param f function applied to every potentially colliding pair */
	def foreach(f: ((A, A)) => Unit)

}