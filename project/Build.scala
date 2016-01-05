import sbt._
import sbt.Keys._
import android.Keys._

object Build extends Build {

  import Dependencies._
  import Settings._

  lazy val core = project.from("core")
    .setName("client.core")
    .setDescription("Talkie core components")
    .configureAsLibrary
    .configureModule

  lazy val domain = project.from("domain")
    .setName("client.domain")
    .setDescription("Talkie domain components")
    .configureAsLibrary
    .configureModule
    .dependsOnLibraries(core)

  lazy val app = project.from("app")
    .setName("client.app")
    .setDescription("Talkie application")
    .configureAsApplication
    .configureModule
    .dependsOnLibraries(core, domain)
}
