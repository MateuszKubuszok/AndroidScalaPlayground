import sbt._
import sbt.Keys._

import Common._

val app = Project(id = "app", base = file("modules/app"))
  .settings(android.Plugin.androidBuild:_*)
  .settings(Common.moduleSettings:_*)
  .settings(
    name         := "app",
    organization := "com.talkie",
    version      := "0.1.0-SNAPSHOT",
    description  := "Talkie Android client",

    applicationId in Android := "com.talkie.client",
    versionCode   in Android := Some(1),
    versionName   in Android := Some("0.1.0-SNAPSHOT")
  )
  .settings(debugBuild:_*)
  .settings(releaseBuild:_*)
  .settings(prodFlavor:_*)
  .settings(devFlavor:_*)

android.Plugin.withVariant("app", Some("debug"), Some("prod"))
