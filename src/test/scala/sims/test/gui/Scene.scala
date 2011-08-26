package sims.test.gui

trait Scene extends Reactor {
	def name: String = this.getClass().getName()
	def description: String = ""
	
	val world = new DebugWorld
	
	def init(): Unit
	
	def exit(): Unit = {}
	
}