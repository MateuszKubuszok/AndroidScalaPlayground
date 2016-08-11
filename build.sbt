import Dependencies._
import Settings._

lazy val common = project.from("common")
  .setName("client.common")
  .setDescription("Talkie common components")
  .configureModule(asLibrary = true)()

lazy val core = project.from("core")
  .setName("client.core")
  .setDescription("Talkie core components")
  .configureModule(asLibrary = true)(common)

lazy val domain = project.from("domain")
  .setName("client.domain")
  .setDescription("Talkie domain components")
  .configureModule(asLibrary = true)(core)

lazy val views = project.from("views")
  .setName("client.views")
  .setDescription("Talkie views")
  .configureModule(asLibrary = true, withViews = true)(common)

lazy val app = project.from("app")
  .setName("client.app")
  .setDescription("Talkie application")
  .configureModule(withViews = true)(domain, views)
