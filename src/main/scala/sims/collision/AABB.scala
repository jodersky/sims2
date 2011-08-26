/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math._

/* 
 * y
 * ^
 * |
 * |  +-------+
 * |  |    max|
 * |  |       |
 * |  |min    |
 * |  +-------+
 * |
 * 0-------------->x
 * 
 */

/** Axis Aligned Bounding Boxes (AABBs) are rectangles that frame a shape.
  * Their X-axis and Y-axis orientation makes it easy to test two AABBs for overlap.
  * @param minVertex Position vector of the bottom-left vertex
  * @param maxVertex Position vector of the upper-right vertex */
case class AABB(val minVertex: Vector2D,
                val maxVertex: Vector2D) {
	
	/** Diagonal vector from `minVertex` to `maxVertex`. */
	def diagonal = maxVertex - minVertex
	
	/** Width of this AABB. */
	def width = maxVertex.x - minVertex.x
	
	/** Height of this AABB. */
	def height = maxVertex.y - minVertex.y
	
	/** Checks if the given point is located within this AABB. */
	def contains(point: Vector2D): Boolean = minVertex.x <= point.x && point.x <= maxVertex.x && minVertex.y <= point.y && point.y <= maxVertex.y
	
	/** Checks if the given AABB is located within this AABB. */
	def contains(box: AABB): Boolean = contains(box.minVertex) && contains(box.maxVertex)
	
  /** Checks this AABB with <code>box</code> for overlap.
    * @param box AABB with which to check for overlap */
  def overlaps(box: AABB): Boolean = {
    val d1 = box.minVertex - maxVertex
    val d2 = minVertex - box.maxVertex
    !(d1.x > 0 || d1.y > 0 || d2.x > 0 || d2.y > 0)
  }
}
