/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.collision

import sims.math.Vector2D

/** Contains information on the collision between two collidable items. */
abstract class Collision[A <: Collidable] {
	val item1: A
	val item2: A
	def normal: Vector2D
	def points: Seq[Vector2D]
	def overlap: Double
}