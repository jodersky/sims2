package sims.test.gui

import processing.core.PApplet
import sims.dynamics.Shape
import sims.dynamics.Joint

abstract class Graphical[+A](val physical: A) {
	val top: PApplet
	val render: () => Unit
}

abstract class GraphicalShape(val shape: Shape) extends Graphical[Shape](shape)
abstract class GraphicalJoint(val joint: Joint) extends Graphical[Joint](joint)