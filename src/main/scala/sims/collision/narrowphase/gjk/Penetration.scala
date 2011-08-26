package sims.collision.narrowphase.gjk

import sims.collision._
import sims.math._

class Penetration(
		val normal: Vector2D,
		val overlap: Double
	)