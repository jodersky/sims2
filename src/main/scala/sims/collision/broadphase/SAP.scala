/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision.broadphase

import sims.collision._
import scala.collection.mutable.ArrayBuffer

/** A broadphase collision detector implementing the "Sweep and Prune" algorithm.
  *  
  * The implementation of the broadphase algorithm was adapted from
  * Real-Time Collision Detection by Christer Ericson, published by Morgan Kaufmann Publishers, (c) 2005 Elsevier Inc */
class SAP[A <: Collidable: ClassManifest] extends BroadPhaseDetector[A]{

	/*ordering along `axis`
	 * x axis => 0
	 * y axis => 1 */
	private var axis = 0
	
	//use insert sort
	private var almostSorted = false
	private var sortedCount = 0
	private val threshold = 3
	
	private implicit def ordering: Ordering[A] = new Ordering[A] {
		def compare(x: A, y: A) = {
			val delta = x.aabb.minVertex.components(axis) - y.aabb.minVertex.components(axis)
			if (delta < 0) -1
			else if(delta > 0) 1
			else 0
		}
	}
	
	private def insertionSort(a: ArrayBuffer[A])(implicit ord: Ordering[A]) = {
    import ord._
		val length = a.length
		var i = 1; while(i < length) {
      var j = i
      val t = a(j);
      while (j>0 && a(j-1) > t) {
      	a(j)=a(j-1)
      	j -= 1
      }
      a(j)=t;
      i += 1
		}
	}
	
	def foreach(f: ((A, A)) => Unit): Unit = {
		
		if (almostSorted)
			insertionSort(_items)
		else
			_items = _items.sorted //quicksort
	
		var sumX, sumY = 0.0
		var sumX2, sumY2 = 0.0
		var varianceX, varianceY = 0.0
	
		var i = 0; while (i < _items.length) {
		
			//center point
			val px = (_items(i).aabb.minVertex.x + _items(i).aabb.maxVertex.x) / 2
			val py = (_items(i).aabb.minVertex.y + _items(i).aabb.maxVertex.y) / 2
		
			//update sum and sum2 for computing variance of AABB centers
			sumX += px; sumY += py
			sumX2 += px * px; sumY2 += py * py
		
			//collision test
			var j = i + 1; var break = false; while(!break && j < _items.length) {
			
				//stop when tested AABBs are beyond the end of current AABB
				if (_items(j).aabb.minVertex.components(axis) > _items(i).aabb.maxVertex.components(axis))
				break = true
				
				//collision test here
				else if (
						(_items(i).fixed == false || _items(j).fixed == false) &&
						(_items(i).aabb overlaps _items(j).aabb)
				) f(_items(i), _items(j))
			
				j += 1
			}
			
			i += 1
		}
	
		varianceX = sumX2 - sumX * sumX
		varianceY = sumY2 - sumY * sumY
	
		//choose sorting axis with greatest variance
		var newAxis = 0
		if (varianceX < varianceY) newAxis = 1
		
		if (axis == newAxis)
			sortedCount += 1
		else sortedCount = 0
		
		almostSorted = sortedCount > threshold 
		//if sorting axis changes, items will no longer be almost sorted
		//and thus quicksort should be used to reorder them
		//almostSorted = axis == newAxis
		
		//update sorting axis
		axis = newAxis
		
	}
	
}

object SAP {
	
	def apply[A <: Collidable: ClassManifest] = new SAP[A]
	
}