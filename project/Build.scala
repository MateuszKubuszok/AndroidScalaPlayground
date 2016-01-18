import android.Keys._
import sbt._

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

  lazy val views = project.from("views")
    .setName("client.views")
    .setDescription("Talkie views")
    .configureAsLibrary
    .configureModule
    .settings(typedResources := true)

  lazy val app = project.from("app")
    .setName("client.app")
    .setDescription("Talkie application")
    .configureAsApplication
    .configureModule
    .dependsOnLibraries(core, domain, views)
}
