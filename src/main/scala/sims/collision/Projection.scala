/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math._

/**Projection on an axis.
 * <p>
 * Projections are commonly used in SiMS for collision detection.
 * @param axis directional vector of the axis of the projection
 * @param lower lower value of the projection
 * @param upper upper value of the projection*/
case class Projection(axis: Vector2D,
                      lower: Double,
                      upper: Double) {
  require(axis != Vector2D.Null, "A projection's axis cannot be given by a null vector!")
  require(lower <= upper, "Invalid bounds. Lower must be less than or equal to upper.")
  
  /**Checks this projection for overlap with another projection.
   * @throws IllegalArgumentExcepion if both projections have different axes*/
  def overlaps(other: Projection): Boolean = {
    require(axis == other.axis, "Cannot compare two projections on different axes!")
      !((other.lower - this.upper) > 0 || (this.lower - other.upper) > 0)
  }
  
  /**Returns the overlap between this projection and another projection.
  * @throws IllegalArgumentExcepion if both projections have different axes*/
  def overlap(other: Projection): Double = {
    require(axis == other.axis, "Cannot compare two projections on different axes!")
         math.min(upper, other.upper) - math.max(lower, other.lower)
        
  }
}
