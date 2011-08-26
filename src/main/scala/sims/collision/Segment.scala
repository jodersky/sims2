/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import scala.math._
import sims.math._

/** A segment passing through two points.
  *  
  * @param point1 position vector of the first point
  * @param point2 position vector of the second point
  * @throws IllegalArgumentException if both vertices are equal */
case class Segment(point1: Vector2D, point2: Vector2D)
	extends Linear
	with Intersectable[Segment] {
	
  require(point1 != point2, "A segment must have two distinct vertices.")
  
  val point = point1
  
  /**Vector from <code>vertex1</code> to <code>vertex2</code>.*/
  val direction = point2 - point
  
  /**Length of this segment.*/
  val length = direction.length
  

  def closest(point: Vector2D) = {
  	var t = ((point - point1) dot (direction)) / (direction dot direction)
  	if (t < 0) t = 0
  	if (t > 1) t = 1
  	point1 + direction * t
    }
  
  def distance(p: Vector2D) = {
  	// For more concise code, the following substitutions are made:
  	// * point1 -> a
  	// * point2 -> b
  	// * p      -> c
  	
  	val ab = direction
  	val ac = p - point1
  	val bc = p - point2
  	
  	val e = ac dot ab
  	val distanceSquare = 
  		// Handle cases where c projects outside ab
  		if (e <= 0) ac dot ac
  		else if (e >= (ab dot ab)) bc dot bc
  		// Handle cases where c projects onto ab
  		else (ac dot ac) - e * e / (ab dot ab)
  	
  	math.sqrt(distanceSquare)
  }
 
  def clipped(reference: Segment): List[Vector2D] = {
		val clipped = Linear.clip(this.point1, this.point2, reference.point1, reference.direction)
		if (clipped.length == 0) Nil
		else Linear.clip(clipped(0), clipped(1), reference.point2, -reference.direction)
	}
	
  
  def intersection(segment: Segment): Option[Vector2D] = {
  	val n = segment.leftNormal
  	
  	// Handle case when two segments parallel
  	if ((n dot direction) == 0) None
  	else {
  		val t = (n dot (segment.point1 - point1)) / (n dot direction)
  		val i = point + direction * t
  		if (0 <= t && t <= 1 && (i - segment.point1).length <= segment.length) Some(i)
  		else None
  	}
	 /*
  	// Returns 2 times the signed triangle area. The result is positive if
  	// abc is ccw, negative if abc is cw, zero if abc is degenerate.
  	def signed2DTriArea(a: Vector2D, b: Vector2D, c: Vector2D) = {
  		(a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
  	}
  	
  	val a = point1; val b = point2; val c = segment.point; val d = segment.point2
	
    // Sign of areas correspond to which side of ab points c and d are
    val a1 = signed2DTriArea(a, b, d); // Compute winding of abd (+ or -)
    val a2 = signed2DTriArea(a, b, c); // To intersect, must have sign opposite of a1

    // If c and d are on different sides of ab, areas have different signs
    if (a1 * a2 < 0.0f) {
        // Compute signs for a and b with respect to segment cd
        val a3 = signed2DTriArea(c, d, a); // Compute winding of cda (+ or -)
        // Since area is constant a1-a2 = a3-a4, or a4=a3+a2-a1
        // float a4 = Signed2DTriArea(c, d, b); // Must have opposite sign of a3
        val a4 = a3 + a2 - a1;
        // Points a and b on different sides of cd if areas have different signs
        if (a3 * a4 < 0.0f) {
            // Segments intersect. Find intersection point along L(t)=a+t*(b-a).
            // Given height h1 of a over cd and height h2 of b over cd,
            // t = h1 / (h1 - h2) = (b*h1/2) / (b*h1/2 - b*h2/2) = a3 / (a3 - a4),
            // where b (the base of the triangles cda and cdb, i.e., the length
            // of cd) cancels out.
            val t = a3 / (a3 - a4);
            val p = a + (b - a) * t;
            return Some(p);
        }
    }
    // Segments not intersecting (or collinear)
    return None;
    */
  }
  
  
}
