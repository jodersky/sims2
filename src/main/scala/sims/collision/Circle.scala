/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math.Vector2D

/** Properties implemented by a collidable circle.
  * Note: this class does not define any physical properties, see sims.dynamics.Circle for that.
  * @see sims.dynamics.Circle */
trait Circle extends Collidable {
	
	/** Position of this circle. */
	def position: Vector2D
	
	/** Radius of this circle. */
	def radius: Double
	
	/** Returns this circle's axis aligned bounding box.
    * @see collision.AABB */
	override def aabb = new AABB(Vector2D(position.x - radius, position.y - radius), Vector2D(position.x + radius, position.y + radius))
	
	/** Checks if the point `point` is contained in this circle. */
	override def contains(point: Vector2D) = (point - position).length <= radius
	
	/** Returns the projection of this polygon onto the line given by the directional vector `axis`. */
	override def project(axis: Vector2D) = {
		val dir = axis.unit
		new Projection(
				axis,
				(position - dir * radius) dot dir,
				(position + dir * radius) dot dir
				)
	}
	
	override def support(direction: Vector2D) = position + direction.unit * radius
}