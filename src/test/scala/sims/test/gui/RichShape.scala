package sims.test.gui

import processing.core.PApplet
import processing.core.PConstants._
import sims.dynamics._

class RichShape(shape: Shape) {
	private implicit def double2Float(x: Double): Float = x.toFloat
	
	def toGraphical(implicit parent: PApplet) = new GraphicalShape(shape) {
		
		val top = parent
		
		val render = shape match {
			
			case c: Circle => () => {
				top.pushMatrix()
				top.stroke(0, 0, 0)
				top.fill(0, 0, 255, 200)
				top.translate(c.position.x, c.position.y)
				top.rotate(-c.rotation)
				top.ellipseMode(CENTER)
				top.ellipse(0, 0, c.radius * 2, c.radius * 2)
				top.line(0,0, c.radius, 0)
				top.popMatrix()
			}
			
			case r: Rectangle => () => {
				top.pushMatrix()
				top.translate(r.position.x, r.position.y)
				top.rotate(-r.rotation)
				top.fill(255, 0, 0, 200)
				top.rectMode(CENTER)
				top.rect(0, 0, r.halfWidth * 2, r.halfHeight * 2)
				top.popMatrix()
			}
			
			case _ => throw new IllegalArgumentException("Cannot create graphical shape: unknown shape.")
		}
	
	}
	
}

object RichShape {
	
	implicit def shapeToRichShape(s: Shape) = new RichShape(s)
	
}