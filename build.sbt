import sbt._
import sbt.Keys._

import android._
import android.Keys.{ ProjectLayout => _ }

val app = Project(id = "app", base = file("app"))
  .settings(android.Plugin.androidBuild:_*)
  .settings(
    name         := "app",
    organization := "com.talkie",
    version      := "0.1.0-SNAPSHOT",
    description  := "Talkie Android client",

    javacOptions in Compile ++= Seq(
      "-source", "1.7",
      "-target", "1.7"
    ),

    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-Ywarn-dead-code",
      "-Ywarn-infer-any",
      "-Ywarn-unused-import",
      "-Xfatal-warnings",
      "-Xlint"
    ),

    packagingOptions in Android := PackagingOptions(Nil, Nil, Seq("META-INF/NOTICE.txt", "META-INF/LICENSE.txt")),

    resolvers ++= Seq(
      Resolver sonatypeRepo "public",
      Resolver typesafeRepo "releases",
      "https://jcenter.bintray.com/" at "https://jcenter.bintray.com/"
    ),

    libraryDependencies ++= Seq(
      // Android
      "com.android.support" % "support-v4" % "23.1.1" artifacts(Artifact("support-v4", "aar", "aar")),
      "com.android.support" % "appcompat-v7" % "23.1.1" artifacts(Artifact("appcompat-v7", "aar", "aar")),
      "com.android.support" % "design" % "23.1.1" artifacts(Artifact("design", "aar", "aar")),
      "com.android.support" % "recyclerview-v7" % "23.1.1" artifacts(Artifact("recyclerview-v7", "aar", "aar")),
      "com.android.support" % "support-annotations" % "23.1.1",
      "com.android.support" % "multidex" % "1.0.1" artifacts(Artifact("multidex", "aar", "aar")),

      // Scala 2.11.7
      "org.scala-lang" % "scala-library" % "2.11.7",

      // Databases
      "com.typesafe.slick" %% "slick" % "2.1.0",
      "org.sqldroid" % "sqldroid" % "1.0.3",
      "net.danlew" % "android.joda" % "2.9.1" artifacts(Artifact("android.joda", "aar", "aar")),
      "org.joda" % "joda-convert" % "1.8.1",
      "com.chuusai" %% "shapeless" % "2.2.5",

      // Facebook SDK
      "com.facebook.android" % "facebook-android-sdk" % "4.8.2" artifacts(Artifact("facebook-android-sdk", "aar", "aar"))
    ),

    platformTarget in Android := "android-23",

    debugIncludesTests in Android := false,

    buildConfigOptions in Android ++= Nil,
    resValues in Android ++= Nil,
    proguardOptions in Android ++= Nil,
    manifestPlaceholders in Android ++= Map(),
    dexMulti in Android := dexMulti.value || true,
    applicationId in Android := "com.talkie.client",
    versionCode in Android := Some(1),
    versionName in Android := Some("1.0"),
    targetSdkVersion in Android := "23"
  )
  .settings(
    buildTypes += (("debug", Seq(
      buildConfigOptions in Android ++= Nil,
      resValues in Android ++= Nil,
      proguardOptions in Android ++= Nil,
      manifestPlaceholders in Android ++= Map(),
      apkbuildDebug in Android := {
        val debug = apkbuildDebug.value
        debug(true)
        debug
      },
      rsOptimLevel in Android := 3,
      useProguardInDebug in Android := false
    )))
  )
  .settings(
    buildTypes += (("release", Seq(
      buildConfigOptions in Android ++= Nil,
      resValues in Android ++= Nil,
      proguardOptions in Android ++= Nil,
      manifestPlaceholders in Android ++= Map(),
      apkbuildDebug in Android := {
        val debug = apkbuildDebug.value
        debug(false)
        debug
      },
      rsOptimLevel in Android := 3,
      useProguard in Android := false
    )))
  )
  .settings(
    flavors += (("prod", Seq(
      buildConfigOptions in Android ++= Nil,
      resValues in Android ++= Nil,
      proguardOptions in Android ++= Nil,
      manifestPlaceholders in Android ++= Map(),
      minSdkVersion in Android := "23"//,
      //minSdkVersion in Android := "15"
    )))
  )
  .settings(
    flavors += (("dev", Seq(
      buildConfigOptions in Android ++= Nil,
      resValues in Android ++= Nil,
      proguardOptions in Android ++= Nil,
      manifestPlaceholders in Android ++= Map(),
      minSdkVersion in Android := "23"//,
      //minSdkVersion in Android := "21"
    )))
  )

android.Plugin.withVariant("app", Some("debug"), Some("prod"))
