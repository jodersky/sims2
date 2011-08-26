/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims

/** @todo implement DSL properly */
package object dsl {
	
	implicit def body2Rich(b: sims.dynamics.Body) = new RichBody(b)
	
	implicit def body2BodyPoint(b: sims.dynamics.Body) = new BodyPoint(b)
	
}