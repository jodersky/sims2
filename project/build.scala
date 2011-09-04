import sbt._
import Keys._

object SiMS2 extends Build {
  
  val projectSettings = Defaults.defaultSettings ++ Seq(
	name := "SiMS2",
	normalizedName := "sims2",
	version := "2.0",
	scalaVersion := "2.9.1",
	libraryDependencies += "org.scalatest" % "scalatest_2.9.0" % "1.6.1"
  )
 
  def createDemoTask(t: TaskKey[Unit]) = fullRunTask(t, Test, "sims.test.gui.Main") 
  val demo = TaskKey[Unit]("demo", "Runs the demo.")
  val demoFast = TaskKey[Unit]("demo-fast", "Runs the demo with performance increasing jvm options.")
  
  val demoTask = Seq(createDemoTask(demo), fork in demo := true) 
  val demoFastTask = Seq(createDemoTask(demoFast), fork in demoFast := true, javaOptions in demoFast ++=
    List(
      "-server",
      "-Xms256m",
      "-Xmx1024m",
      "-Xss1M",
      "-XX:MaxPermSize=256m",
      "-XX:+AggressiveOpts",
      "-XX:-UseParallelGC",
      "-XX:+DoEscapeAnalysis",
      "-XX:+UseConcMarkSweepGC"
	)
  )
  
  val projectProguardSettings = Seq(
      ProguardPlugin.proguardInJars += 
      ProguardPlugin.proguardOptions ++=
        Seq(
          "-dontobfuscate",
          "-dontnote",
          "-dontwarn",
          "-dontoptimize",
      
          "-keep class sims.**",
          "-keep class processing.core.*",
          "-keepclasseswithmembers public class * {public sims.dynamics.Collision toPhysical();}",
          
          "-keepclasseswithmembers public class * {public static void main(java.lang.String[]);}",
          "-keep public class * extends java.applet.Applet",
          "-keepclassmembers enum  * {public static **[] values(); public static ** valueOf(java.lang.String);}",
          "-keep class * extends java.sql.Driver",
          "-keep class * extends javax.swing.plaf.ComponentUI { public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);}",
          "-keepclasseswithmembers,allowshrinking class * {native <methods>;}"
        ) 
      ) 
    
  

  lazy val project = Project (
    "sims2",
    file ("."),
    settings = projectSettings ++ demoTask ++ demoFastTask ++ ProguardPlugin.proguardSettings
  )
}