//scalaVersion := "2.11.7" // Plugins are not yet ready for Scala 2.11

addSbtPlugin("org.scala-android" % "sbt-android" % "1.6.18")

addSbtPlugin("org.scala-android" % "sbt-android-protify" % "1.3.7")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.5.1")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.3")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
