import sbt._

import com.github.play2war.plugin._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-webapp"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(Play2WarPlugin.play2WarSettings: _*)
  	.settings(
    // Add your own project settings here      
  	Play2WarKeys.servletVersion := "3.0"
  )

}
