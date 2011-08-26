package sims.test.gui

import processing.core.PApplet
import scala.collection.mutable.ArrayBuffer
import sims.math._
import sims.test.gui.scenes._
import sims.test.gui.RichShape._
import sims.test.gui.RichJoint._

class SceneManager(implicit top: PApplet) {
	
	/* Contains objects that will be rendered on `draw()`. */
	private var _graphicals = new ArrayBuffer[Graphical[_]]
	def graphicals: Seq[Graphical[_]] = _graphicals
	
	/* Current scene. */
	private var _currentScene: Scene = EmptyScene
	
	/* Get current scene. */
	def currentScene = _currentScene
	
	/* Set current scene. */
	def currentScene_=(newScene: Scene) = {
		
		// remove reactions
		currentScene.deafTo(currentScene.world)
		currentScene.reactions.clear()
		
		// empty world
		currentScene.world.clear()
		
		// clear graphical objects
		_graphicals.clear()
		
		// custom exit behavior
		currentScene.exit()
		
		// add new reactions to create / remove graphical objects
		newScene.listenTo(newScene.world)
		newScene.reactions += {
			case BodyAdded(newScene.world, body) => for (s <- body.shapes) _graphicals += s.toGraphical
			case BodyRemoved(newScene.world, body) => for (s <- body.shapes) {
				val index = _graphicals.findIndexOf((g: Graphical[_]) => g match {
					case gs: GraphicalShape => gs.physical == s
					case _ => false
				})
				_graphicals.remove(index)
			}
			
			case JointAdded(newScene.world, joint) => _graphicals += joint.toGraphical
			case JointRemoved(newScene.world, joint) => {
				val index = _graphicals.findIndexOf((g: Graphical[_]) => g match {
					case gj: GraphicalJoint => gj.physical == joint
					case _ => false
				})
				_graphicals.remove(index)
			}
			
		}
		
		// custom initialization
		newScene.init()
		
		// set current scene
		_currentScene = newScene
		
		println("set scene to '" + currentScene.name + "'")
	}
	
	private var currentSceneIndex = 0
	val scenes = List(
			BasicScene,
			CollisionScene,
			LongCollisionScene,
			CloudScene,
			PyramidScene,
			ShiftedStackScene,
			JointScene
		)
		
	def nextScene() = {
		currentSceneIndex += 1
		currentScene = scenes(mod(currentSceneIndex, scenes.length))
	}
	
	def previousScene() = {
		currentSceneIndex -= 1
		currentScene = scenes(mod(currentSceneIndex, scenes.length))
	}
	
	def restartScene() = {
		currentScene = currentScene
	}
	
}