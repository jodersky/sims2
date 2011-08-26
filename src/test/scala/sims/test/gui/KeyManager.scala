package sims.test.gui

import processing.core.PApplet

class KeyManager(implicit top: Main) {

	def keyPressed(keyCode: Int) = keyCode match {
		// ENTER
		case 10 => top.SceneManager.currentScene.world.step()
		
		// SPACE
		case 32 => top.paused = !top.paused
		
		// PAGE UP
		case 33 => top.viewScale += top.viewScale * 0.02f
		
		// PAGE DOWN
		case 34 => top.viewScale -= top.viewScale * 0.02f
		
		// 0
		case 36 => {top.offsetX = 0; top.offsetY = 0}
		
		// LEFT
		case 37 => top.offsetX += 50
		
		// UP
		case 38 => top.offsetY -= 50
		
		// RIGHT
		case 39 => top.offsetX -= 50
		
		// DOWN
		case 40 => top.offsetY += 50
		
		// , (<)
		case 44 => top.SceneManager.previousScene()
		
		// . (>)
		case 46 => top.SceneManager.nextScene()
		
		// b
		case 66 => top.SceneManager.currentScene.world.errorReduction += 0.1
		
		//v
		case 86 => top.SceneManager.currentScene.world.errorReduction -= 0.1
		
		case 45 => top.SceneManager.currentScene.world.iterations -= 1
		case 61 => top.SceneManager.currentScene.world.iterations += 1
			
		case x: Any => println("unknown key: " + x)
	}
	
}