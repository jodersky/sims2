package sims.collision

import sims.math._

class ContactPoint[A <: Collidable](
		val item1: A,
		val item2: A,
		val normal: Vector2D,
		val overlap: Double,
		val point: Vector2D
	)