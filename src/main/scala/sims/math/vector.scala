/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.math

import scala.math._

/** A 2D vector.
  * @param x 1st component
  * @param y 2nd component */
case class Vector2D(x: Double, y: Double) {
	
	/** Components of this vector. */
	lazy val components = List(x, y)
    
  /** Vector addition. */
  def +(v: Vector2D): Vector2D = new Vector2D(x + v.x, y + v.y)
  
  /** Scalar multiplication. */
  def *(n: Double): Vector2D = new Vector2D(x * n, y * n)
  
	/** Inverse of this vector. */
  lazy val unary_- = this * (-1)
  
  /** Add inverse of another vector. */
  def -(v: Vector2D) = this + -v
  
  /** Multiply by inverse of scalar. */
  def /(n: Double) = this * (1 / n)
  
  /** Dot product. */
  def dot(v: Vector2D): Double = x * v.x + y * v.y
  
  /** Cross product. Length only because in 2D. The direction would be given by the x3-axis. */
  def cross(v: Vector2D): Double = x * v.y - y * v.x
  
  /** Cross product with an imaginary vector parallel to the x3-axis.
    * Its magnitude is given by |p| and its direction sign(p). */
  def cross(p: PseudoVector3D): Vector2D = rightNormal * p.x3
  
  /** Magnitude of this vector. */
  lazy val length: Double = math.sqrt(lengthSquare)

  lazy val lengthSquare: Double = x * x + y * y
  
  /** Unit vector. */
  lazy val unit: UnitVector2D =
  	if (!(x == 0.0 && y == 0.0)) new UnitVector2D(x / length, y / length) 
    else throw new UnsupportedOperationException("Null vector does not have a unit vector.")
  
  /** Returns the projection of this vector onto the vector `v`. */
  def project(v: Vector2D): Vector2D = {
    if (v != Vector2D.Null)
      v * ((this dot v) / (v.lengthSquare))
    else
      Vector2D.Null
   }
	
	/** Returns the projection of this vector onto the unit vector `v`. */
  def project(v: UnitVector2D): Vector2D = {
    if (v != Vector2D.Null)
      v * (this dot v)
    else
      Vector2D.Null
   }
  
  /** Returns this vector rotated by `angle` radians. */
  def rotate(angle: Double): Vector2D = {
    Vector2D(cos(angle) * x - sin(angle) * y,
             cos(angle) * y + sin(angle) * x)
  }
  
  /** Left normal vector. (-y, x) */
  lazy val leftNormal: Vector2D = Vector2D(-y, x)
  
  /** Right normal vector. (y, -x) */
  lazy val rightNormal: Vector2D = Vector2D(y, -x)
  
  /** Checks if this vector is the null vector. */
  def isNull: Boolean = this == Vector2D.Null
  
  /** Colinearity check. */
  def ~(v: Vector2D): Boolean = x * v.y - v.x * y == 0
  
  
  def leftOf(v: Vector2D): Boolean = (this dot v.leftNormal) > 0
  def rightOf(v: Vector2D): Boolean = (this dot v.rightNormal) > 0
  def directionOf(v: Vector2D): Boolean = (this dot v) > 0
  
}
	
object Vector2D {
	
	/**Null vector.*/
  val Null = Vector2D(0,0)
  
  /**Horizontal unit vector. (1,0)*/
  val i = new UnitVector2D(1,0)
  
  /**Vertical unit vector. (0,1)*/
  val j = new UnitVector2D(0,1)
	
}

/** A two dimensional vector considered having a magnitude of one.
  * Unit vectors are used internally for increasing performance
  * (i.e. the length of a unit vector is always one, the unit vector of
  * a unit vector is always itself). */
class UnitVector2D(x: Double, y: Double) extends Vector2D(x, y) {
	
	def isValid = x * x + y * y == 1
	
	override lazy val unit = this
	
	override lazy val length = 1.0

  override lazy val lengthSquare = 1.0
	
  override lazy val leftNormal = new UnitVector2D(-y, x)
  
  override lazy val rightNormal = new UnitVector2D(y, -x)
	
}