/*    _____ _ __  ________    ___                                      *\
**   / ___/(_)  |/  / ___/   |__ \  Simple Mechanics Simulator 2       **
**   \__ \/ / /|_/ /\__ \    __/ /  copyright (c) 2011 Jakob Odersky   **
**  ___/ / / /  / /___/ /   / __/                                      **
** /____/_/_/  /_//____/   /____/                                      **
\*                                                                     */

package sims.dynamics.constraints

import sims.math.Vector2D

/** A Jacobian matrix used for solving constraints.
  * 
  * In SiMS 2 constraints are considered to remove one degree of freedom for two bodies.
  * Let '''v''',,1,,, w,,1,,, '''v''',,2,,, w,,2,, be the linear and angular velocities of both bodies
  * respectively.
  * Let v = ['''v''',,1,,, w,,1,,, '''v''',,2,,, w,,2,,].
  * The velocity constraint function is then given by 'Ċ='''Jv'''+b' and 
  * the Jacobian ('''J''') is given by '''J''' = ['''Jv''',,1,,, Jw,,1,,, '''Jv''',,2,,, Jw,,2,,] = [Jv,,1,x,,, Jv,,1,y,,, Jw,,1,,, Jv,,2,x,,, Jv,,2,y,,, Jw,,2,,]
  *  
  * @param v1 corresponds to '''Jv''',,1,, in the description above
  * @param w1 corresponds to Jw,,1,, in the description above
  * @param v2 corresponds to '''Jv''',,2,, in the description above
  * @param w2 corresponds to Jw,,2,, in the description above
  * @see sims.dynamics.constraints.Constraint */
class Jacobian(val v1: Vector2D, val w1: Double, val v2: Vector2D, val w2: Double)