package sims.test.gui

class DebugWorld extends sims.dynamics.World with Publisher {
	
	override def +=(b: sims.dynamics.Body) = {
		super.+=(b)
		publish(BodyAdded(this, b))
	}
	
	override def -=(b: sims.dynamics.Body) = {
		super.-=(b)
		publish(BodyRemoved(this, b))
	}
	
	override def +=(j: sims.dynamics.Joint) = {
		super.+=(j)
		publish(JointAdded(this, j))
	}
	
	override def -=(j: sims.dynamics.Joint) = {
		super.-=(j)
		publish(JointRemoved(this, j))
	}
	
	override def step() = {
		super.step()
		publish(Stepped(this))
	}
}