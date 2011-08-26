/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics

import sims.collision._
import sims.math._


/** 
	* @define shape shape */
trait Shape extends AnyRef with Collidable with CachedCollidable {
	
	override def equals(that: Any) = super.equals(that)
	override def hashCode = super.hashCode
	
	/** (temporary solution) Local position of $shape to body.
	  * @deprecated find solution avoiding NullPointers
	  * @todo find solution avoiding NullPointers */
	var local: Option[Vector2D] = None
	
	var body: sims.dynamics.Body = null
	
	override def fixed = body.fixed
	
	var restitution = 1.0
	
	/**Position of this $shape's center of mass in world coordinates.*/
	private var _position: Vector2D = Vector2D.Null
	def position = _position
	def position_=(pos: Vector2D) = {
		_position = pos
		move()
	}
	
  /**Rotation of this shape about its center of mass.*/
	var rotation: Double = 0.0
	
	/** Mass per area. */
  def density: Double = 1.0
  
  /** Area of this $shape. */
  def area: Double
  
  /** Mass of this $shape. The mass is given by volume times density. */
  def mass: Double = area * density
 
  /**Moment of inertia for a rotation about this $shape's center of mass.*/
  def inertia: Double
  
  /**Returns this $shape's axis aligned bounding box.*/
  def aabb: sims.collision.AABB
  
  /**Returns the projection of this $shape onto the line given by the directional vector <code>axis</code>.
   * @param axis directional vector of the line
   * @return projection of this shape*/
  def project(axis: Vector2D): Projection
  
  /**Checks if the point <code>point</code> is contained in this $shape.*/
  def contains(point: Vector2D): Boolean
 
  def asBody = new sims.dynamics.Body(this)
}