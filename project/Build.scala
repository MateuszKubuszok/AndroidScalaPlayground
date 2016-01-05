import sbt._
import sbt.Keys._
import android.Keys._

object Build extends Build {

  import Dependencies._
  import Settings._

  lazy val core = project.from("core")
    .configureAsLibrary
    .configureModule

  lazy val domain = project.from("domain")
    .configureAsLibrary
    .configureModule
    .dependsOnLibraries(core)

  lazy val app = project.from("app")
    .configureAsApplication
    .dependsOnLibraries(core, domain)
    .configureModule
}
