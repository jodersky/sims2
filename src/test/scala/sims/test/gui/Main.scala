package sims.test.gui

import processing.core.PApplet
import processing.core.PConstants._
import scala.collection.mutable.ArrayBuffer

import sims.math._
import sims.test.gui.RichShape._
import sims.test.gui.scenes._
import sims.dynamics.Shape

class Main extends PApplet {
	implicit val top = this
	
	val SceneManager = new SceneManager
	import SceneManager._
	
	val KeyManager = new KeyManager
	import KeyManager._
	
	var (offsetX, offsetY) = (200.0f, 100.0f)
	val PPM = 39.37f * 96
	var viewScale: Float = 1.0f / 80
	
	private val fontSize = 16
	private val f = createFont("Monospaced.plain", fontSize)
	private def displayText(lines: String*) = {
		val size = 16
		val indent = 10
		
		fill(0, 0, 0)
		textMode(SCREEN)
		textFont(f)
		
		for (i <- 0 until lines.length) text(lines(i), indent, (i + 1) * size)
	}
	
	
	override def setup() = {
		size(screenWidth * 2 / 3, screenHeight * 2 / 3, P2D)
		background(255,255,255)
		frameRate(60)
		//frame.setResizable(true)
		currentScene = scenes(0)
	}
	
	var paused = true
	override def draw() = {
		smooth()
		background(255,255,255)
		
		translate(offsetX, height - offsetY)
		scale(viewScale * PPM, -viewScale * PPM)
		
		val t0 = System.nanoTime()
		if (!paused) currentScene.world.step()
		val collisions = if (currentScene.world.collisionDetection) SceneManager.currentScene.world.detector.collisions() else Seq()
		val dStep = System.nanoTime() - t0
		
		for (g <- graphicals) g.render()
		fill(255, 0, 0)
		stroke(20, 0, 0)
		for (c <- collisions; p <- c.points) {
			ellipse(p.x.toFloat, p.y.toFloat, 0.1f, 0.1f)
			stroke(0, 255, 0)
			val s = p
			val e = p + c.normal
			line(s.x.toFloat, s.y.toFloat, e.x.toFloat, e.y.toFloat)
		}
				
				//_.points.foreach((v) => ellipse(v.x.toFloat, v.y.toFloat, 0.1f, 0.1f)))
		
		val dRender = System.nanoTime() - t0 - dStep
		
		displayText(
								"status     : " + (if (paused) "paused" else "running"),
								"------------",
								"fps    [Hz]: " + frameRate,
								"------------",
								"step   [ms]: " + (dStep / 1E6f),
								"       [%] : " + (dStep.toFloat / (dStep + dRender) * 100),
								"render [ms]: " + (dRender / 1E6f),
								"       [%] : " + (dRender.toFloat / (dStep + dRender) * 100),
								"------------",
								"memory [MB]: " + java.lang.Runtime.getRuntime.totalMemory / 1E6,
								"load       : " + java.lang.management.ManagementFactory.getOperatingSystemMXBean.getSystemLoadAverage(),
								"------------",
								"bodies     : " + currentScene.world.bodies.length,
								"shapes     : " + currentScene.world.shapes.length,
								"joints     : " + currentScene.world.joints.length,
								"constraints: " + currentScene.world.joints.map(_.constraints.length).sum,
								"collisions : " + collisions.length,
								"it     [1] : " + currentScene.world.iterations,
								"dt     [ms]: " + currentScene.world.h.toFloat,
								"erp    [ms]: " + currentScene.world.errorReduction.toFloat,
								"------------",
								"(" + scaledMouseX + ", " + scaledMouseY + ")"
				)
	}
	
	def drawGrid() = {
		
	}
	
	override def keyPressed() = KeyManager.keyPressed(keyCode)
	
	def scaledMouseX = (mouseX - offsetX) / viewScale / PPM 
	def scaledMouseY = (height - mouseY - offsetY) / viewScale / PPM
	
	var mouseJoint: Option[MouseJoint] = None
	override def mousePressed(): Unit = {
		import Vector2D._
		val body = currentScene.world.bodies.find(_.contains((scaledMouseX, scaledMouseY)))
		if (body.isEmpty) return ()
		val mj = new MouseJoint(body.get, (scaledMouseX, scaledMouseY))
		currentScene.world += mj
		mouseJoint = Some(mj)
	}
	
	override def mouseReleased(): Unit = {
		if (mouseJoint.isEmpty) return ()
		currentScene.world -= mouseJoint.get
		mouseJoint = None
	}
	
	override def mouseDragged(): Unit = {
		import Vector2D._
		
		if (mouseJoint.isEmpty) return ()
		
		mouseJoint.get.anchor = (scaledMouseX, scaledMouseY)
		
	}

	

}

object Main {
  def main(args : Array[String]) : Unit = {
  	PApplet.main(args ++ Array("sims.test.gui.Main"))
  }
}