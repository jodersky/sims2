package sims.test.gui

import processing.core.PApplet
import processing.core.PConstants._
import sims.dynamics._

class RichJoint(joint: Joint) {
private implicit def double2Float(x: Double): Float = x.toFloat
	
	def toGraphical(implicit parent: PApplet) = new GraphicalJoint(joint) {
		
		val top = parent
		
		val render = joint match {
			
			case j: DistanceJoint => () => {
				top.pushMatrix()
				top.stroke(0, 0, 0)
				top.fill(0, 0, 0)
				top.line(j.x1.x, j.x1.y, j.x2.x, j.x2.y)
				top.stroke(100, 100, 100)
				top.line(j.body1.position.x, j.body1.position.y, j.x1.x, j.x1.y)
				top.line(j.body2.position.x, j.body2.position.y, j.x2.x, j.x2.y)
				top.popMatrix()
			}
			
			case j: RevoluteJoint => () => {
				top.pushMatrix()
				top.stroke(0, 0, 0)
				top.fill(0, 0, 0)
				top.line(j.x1.x, j.x1.y, j.x2.x, j.x2.y)
				top.stroke(100, 100, 100)
				top.line(j.body1.position.x, j.body1.position.y, j.x1.x, j.x1.y)
				top.line(j.body2.position.x, j.body2.position.y, j.x2.x, j.x2.y)
				top.popMatrix()
			}
			
			case j: MouseJoint => () => ()
			
			case _ => throw new IllegalArgumentException("Cannot create graphical joint: unknown joint.")
		}
	
	}
	
}

object RichJoint {
	
	implicit def jointToRichShape(j: Joint) = new RichJoint(j)
	
}