/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math._

/**Common properties of all polygons.*/
trait Polygon extends Collidable {
	
	/**Returns positions of all vertices of this Polygon. Vertices are ordered counter-clockwise.
   * @return position vectors of the vertices*/
  def vertices: Seq[Vector2D]
  
  /**Returns all sides of this polygon. The sides are ordered counter-clockwise, the first vertex of the side
   * giving the side index.*/
  def sides: Seq[Segment] = for (i <- 0 until vertices.length) yield (new Segment(vertices(i), vertices((i + 1) % vertices.length)))
  
  /**Returns this polygon's axis aligned bounding box.
   * @see collision.AABB*/
  override def aabb = {
    val xs = vertices.view map (_.x)
    val ys = vertices.view map (_.y)
    new AABB(Vector2D(xs.min, ys.min),
             Vector2D(xs.max, ys.max))
  }

}