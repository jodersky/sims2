package sims.test.gjk

import processing.core.PApplet
import processing.core.PConstants._

class GJKTest extends PApplet {
	implicit val top = this
		
	import sims.dynamics._
	import sims.math._
	import sims.test.gui._
	import sims.test.gui.RichShape._
	
	var s1: GraphicalShape = _ 
	var s2: GraphicalShape = _
	
	override def setup() = {
		size(600, 600, P2D)
		background(255,255,255)
		frameRate(60)
		
		
		s1 = (new Rectangle(1, 3) {position = Vector2D(5,5)}).toGraphical
		s2 = (new Rectangle(1, 2) {position = Vector2D(8,8); rotation = 0.2}).toGraphical
		
	}
	
	val PPM = 39.37f * 96
	var viewScale: Float = 1.0f / 80
	
	val GJK = new GJK2[Shape]
	
	var invert = false
	def pair = if (!invert) (s1, s2) else (s2, s1)
	
	override def draw() = {
		smooth()
		background(255,255,255)
		translate(0, height)
		scale(viewScale * PPM, -viewScale * PPM)
		
		if (keyCode == 32) invert = true
		else invert = false
		
		val collision = GJK.collision(pair._1.shape, pair._2.shape)
		/*if (collision != None) {
			pushMatrix()
			rectMode(CORNER)
			stroke(255, 0, 50)
			strokeWeight(10)
			fill(0, 0, 0, 0)
			rect(0, 0, 600, 600)
			strokeWeight(1)
			popMatrix()
		}*/
    //val separation = GJK.collision(pair._1.shape, pair._2.shape)
    //if (!separation.isEmpty)
      //List(separation.get.point1, separation.get.point2) foreach (p => ellipse(p.x.toFloat, p.y.toFloat, 0.1f, 0.1f))


	
		label()
		s2.shape.position = Vector2D(mouseX / viewScale / PPM, -(mouseY - height) / viewScale / PPM)
		
		s1.render()
		s2.render()
		
		
		
		collision match {
			case Some(c) => {
				stroke(0, 255, 0)
				for (p <- c.points) {
					ellipse(p.x.toFloat, p.y.toFloat, 0.1f, 0.1f)
					val s = p
					val e = p + c.normal
					line(s.x.toFloat, s.y.toFloat, e.x.toFloat, e.y.toFloat)
					println(c.overlap)
				}
			}
			case _ => ()
    }
		
		/*stroke(255, 0, 255)
		val f = FeatureManifold.farthestFeature(pair._1.shape, Vector2D.j + Vector2D.i)
		f match {
			case Left(p) => ellipse(p.x.toFloat, p.y.toFloat, 0.1f, 0.1f)
			case Right(s) => line(s.point1.x.toFloat, s.point1.y.toFloat, s.point2.x.toFloat, s.point2.y.toFloat)
		}*/
		
	}
	
	private val fontSize = 16
	private val f = createFont("Monospaced.plain", fontSize)
	private def label() = {
		val size = 16
		fill(0, 0, 0)
		textMode(SCREEN)
		textFont(f)
		
		val p1 = pair._1.shape
		val p2 = pair._2.shape
		text("1", (p1.position.x * PPM * viewScale).toFloat, (height - p1.position.y * PPM * viewScale).toFloat)
		text("2", (p2.position.x * PPM * viewScale).toFloat, (height - p2.position.y * PPM * viewScale).toFloat)
	}

}

object GJKTest {
	
	def main(args: Array[String]): Unit = {
		PApplet.main(args ++ Array("sims.test.gjk.GJKTest"))
	}
	
}