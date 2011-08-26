package sims.test.gui

import scala.collection.mutable.ListBuffer
import sims.dynamics._

trait Event
case class BodyAdded(world: World, body: Body) extends Event
case class BodyRemoved(world: World, body: Body) extends Event
case class Stepped(wordl: World) extends Event
case class JointAdded(world: World, joint: Joint) extends Event
case class JointRemoved(world: World, joint: Joint) extends Event

object Reactions {
	class Impl extends Reactions {
		private val parts = new ListBuffer[Reaction]
		def isDefinedAt(e: Event) = parts exists (_ isDefinedAt e)
		def +=(r: Reaction) = parts += r
		def -=(r: Reaction) = parts -= r
		def clear() = parts.clear()
		def apply(e: Event) {
			for (p <- parts; if p isDefinedAt e) p(e)
		}
	}
	type Reaction = PartialFunction[Event, Unit]
}

abstract class Reactions extends Reactions.Reaction {
	def +=(r: Reactions.Reaction): Unit
	def -=(r: Reactions.Reaction): Unit
	def clear(): Unit
}

trait Reactor {
	val reactions: Reactions = new Reactions.Impl 
	
	def listenTo(ps: Publisher*) = for (p <- ps) p.subscribe(reactions)
	def deafTo(ps: Publisher*) = for (p <- ps) p.unsubscribe(reactions)
}

trait Publisher {
	import Reactions._
	
	private val listeners = new ListBuffer[Reaction]
	
	def subscribe(listener: Reaction) = listeners += listener
	def unsubscribe(listener: Reaction) = listeners -= listener
	
	def publish(e: Event) { for (l <- listeners) l(e) }
}