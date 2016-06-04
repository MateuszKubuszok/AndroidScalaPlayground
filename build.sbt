import Dependencies._
import Settings._

lazy val common = project.from("common")
  .setName("client.common")
  .setDescription("Talkie common components")
  .configureModule(asLibrary = true)

lazy val core = project.from("core")
  .setName("client.core")
  .setDescription("Talkie core components")
  .configureModule(asLibrary = true)
  .dependsOnLibraries(common)

lazy val domain = project.from("domain")
  .setName("client.domain")
  .setDescription("Talkie domain components")
  .configureModule(asLibrary = true)
  .dependsOnLibraries(core)

lazy val views = project.from("views")
  .setName("client.views")
  .setDescription("Talkie views")
  .configureModule(asLibrary = true, withViews = true)
  .dependsOnLibraries(common)

lazy val app = project.from("app")
  .setName("client.app")
  .setDescription("Talkie application")
  .configureModule(withViews = true)
  .dependsOnLibraries(domain, views)
  .aggregate(common, core, domain, views)

lazy val root = (project in file("."))
  .aggregate(common, core, domain, views, app)
